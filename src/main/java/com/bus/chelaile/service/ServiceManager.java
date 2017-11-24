package com.bus.chelaile.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bus.chelaile.common.MyDateUtils;
import com.bus.chelaile.dao.AMeetingBookMapper;
import com.bus.chelaile.dao.DateBookMapper;
import com.bus.chelaile.model.DateInfo;
import com.bus.chelaile.model.MeetingInfo;
import com.bus.chelaile.model.RoomState;
import com.bus.chelaile.model.RoomsInfo;
import com.bus.chelaile.model.client.JsonStr;
import com.bus.chelaile.mvc.MeetingParam;

public class ServiceManager {
	
	@Autowired
	private AMeetingBookMapper aMeetingBookMapper;
	@Autowired
	private DateBookMapper dateBookMapper;

	protected static final Logger logger = LoggerFactory.getLogger(ServiceManager.class);
//	private static final String toutiaoINfoUrl = PropertiesUtils.getValue(PropertiesName.PUBLIC.getValue(),
//			"toutiao.article.url", "http://open.snssdk.com/data/stream/v3/");
	
	static HashMap<Integer, String> rooms = new HashMap<Integer, String>();
	static {
		rooms.put(1, "智慧工厂");
		rooms.put(2, "创薪中心");
		rooms.put(3, "老司机");
		rooms.put(4, "公交迷");
		rooms.put(5, "调度员");
	}
	private static int SIZE = 18;		// 工作时段，早上10点到下午7点，18个‘半小时’


	/**
	 * - 单，双栏，首页浮层： - iOS>=5.2.0 - Android>=3.3.0
	 */

	public String getClienSucMap(Object obj, String status) {
		JsonStr jsonStr = new JsonStr();
		jsonStr.setData(obj);
		jsonStr.setStatus(status);
		try {
			String json = JSON.toJSONString(jsonStr, SerializerFeature.BrowserCompatible);
			// JsonBinder.toJson(clientDto, JsonBinder.always);
			return json;
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
			return "";
		}
	}

	public String getClientErrMap(String errmsg, String status) {
		JsonStr jsonStr = new JsonStr();
		jsonStr.setStatus(status);
		jsonStr.setErrmsg(errmsg);
		try {
			String json = JSON.toJSONString(jsonStr, SerializerFeature.BrowserCompatible);
			return json;
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
			return "";
		}
	}

	
	
	/*
	 * 查询近7天
	 */
	public String getServenDays() {

		ArrayList<String> futureDaysList = new ArrayList<>();
		ArrayList<String> futureDaysOutList = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			futureDaysList.add(MyDateUtils.getFutureDate(i));
		}
		
		// 查询近11天的日期，保存七日星期对应关系
		List<DateInfo> dateInfors= dateBookMapper.listValidDates(futureDaysList.get(0), futureDaysList.get(10));
		Map<String, String> dateDayMap = new HashMap<String, String>();
		for(DateInfo dateInfo : dateInfors) {
			dateDayMap.put(dateInfo.getDate(), dateInfo.getDay());
		}
		
		//去除周六和周日
		Iterator<String> entryDay = futureDaysList.iterator();
		int i = 0;
		while(i < 7 && entryDay.hasNext()) {
			String date = entryDay.next();
			if(dateDayMap.get(date).equals("周六") || dateDayMap.get(date).equals("周日")) {
//				entryDay.remove();
			} else {
				futureDaysOutList.add(date);
				i ++;
			}
		}
		
		
		System.out.println(JSONObject.toJSONString(dateInfors));
		
		return getClienSucMap(futureDaysOutList, "00");

	}

	
	
	/*
	 * 查询所有预定
	 */
	public String getAllPlan(MeetingParam param) {
		
		List<RoomsInfo> roomsInfos = new ArrayList<RoomsInfo>();
		
		logger.info("request for all reservation, request date={}", param.getDate());
		List<MeetingInfo> allMeetings = aMeetingBookMapper.listValidReservation(param.getDate());
		logger.info("****get reservations size*****   {}", allMeetings.size());
		logger.info("****get all reservations info *****   {}", JSONObject.toJSONString(allMeetings));
		
		// 存放各个房间对应的所有预定
		Map<Integer, ArrayList<MeetingInfo>> roomsIdPlan = new HashMap<Integer, ArrayList<MeetingInfo>>();
		for(MeetingInfo meet : allMeetings) {
			int roomId = meet.getRoom_id();
			
			if(roomsIdPlan.containsKey(roomId)) {
				roomsIdPlan.get(roomId).add(meet);
				
			} else {
				ArrayList<MeetingInfo> meets = new ArrayList<MeetingInfo>();
				meets.add(meet);
				roomsIdPlan.put(roomId, meets);
			}
			
			
		}
		logger.info("all valid roomsIdPlan = {}", JSONObject.toJSONString(roomsIdPlan));
		
		// 遍历所有会议室
		for(Integer roomId : rooms.keySet()) {
			
			if (roomsIdPlan.containsKey(roomId)) {
				//该会议室当天有预定
				createHasPlan(roomsInfos, roomId, roomsIdPlan.get(roomId));
				
			} else {
				//该会议室当天没有预定
				createNullPlan(roomsInfos, roomId);
			}
		}

		return getClienSucMap(roomsInfos, "00");
	}

	
	/*
	 * 该会议室当天有预定计划，构造对应的结构
	 */
	private void createHasPlan(List<RoomsInfo> roomsInfos, Integer roomId, ArrayList<MeetingInfo> roomMeetings) {

		RoomsInfo roomsInfo = new RoomsInfo();
		roomsInfo.setId(roomId);
		roomsInfo.setName(rooms.get(roomId));
		ArrayList<RoomState> roomStatList = new ArrayList<RoomState>();

		int i = 0;
		while (i < SIZE) {
			boolean hasPlan = false;
			for (MeetingInfo meet : roomMeetings) {
				// 遍历该room的所有meet，找出符合当前i对应的时间内预定。
				if (i == meet.getTime_index()) {
					roomStatList.add(new RoomState(1, meet.getUser()));
					hasPlan = true;
					break;
				}
			}
			if (!hasPlan) {
				// 没有找到预定，所以该时段置空
				roomStatList.add(new RoomState(0, null));
			}
			i ++;
		}
		roomsInfo.setStateList(roomStatList);
		roomsInfos.add(roomsInfo);
	}

	/*
	 * 该会议室当天没有预定计划，构造对应的结构
	 */
	private void createNullPlan(List<RoomsInfo> roomsInfos, Integer roomId) {
		RoomsInfo roomsInfo = new RoomsInfo();
		roomsInfo.setId(roomId);
		roomsInfo.setName(rooms.get(roomId));
		ArrayList<RoomState> roomStatList = new ArrayList<RoomState>();
		for(int i = 0; i < SIZE; i ++) {
			roomStatList.add(new RoomState(0, null));
		}
		
		roomsInfo.setStateList(roomStatList);
		roomsInfos.add(roomsInfo);
	}

	
	/*
	 * 更新预定信息
	 */
	public String updatePlan(MeetingParam param) {
		
		logger.info("请求更新预定信息,param={}", JSONObject.toJSONString(param));
		List<MeetingInfo> allMeetings = aMeetingBookMapper.listAllReservation(param.getDate());
		Set<String> meetingKeys = new HashSet<String>();
		for(MeetingInfo meet : allMeetings) {
			String key = meet.getDate() + "#" + meet.getRoom_id() + "#" + meet.getTime_index();
			meetingKeys.add(key);
		}

		/*
		 * 从开始到结束所有时段，挨个进行处理
		 */
		for(int i = param.getStart(); i <= param.getEnd(); i ++) {
			String meetingKey = param.getDate() + "#" + param.getRoomId() + "#" + i;
			if(meetingKeys.contains(meetingKey)) {
				logger.info("更新会议室预约，meetingKey={}, newState={}", meetingKey, param.getNewState());
				aMeetingBookMapper.updateReservation(param.getDate(),param.getRoomId(), param.getUser(), i, param.getNewState());
			} else {
				logger.info("增加会议室预约，meetingKey={}, newState={}", meetingKey, param.getNewState());
				aMeetingBookMapper.addReservation(param.getDate(), param.getRoomId(), param.getUser(), i, param.getNewState());
			}
		}
		
		logger.info("更新完成!");
		JSONObject j = new JSONObject();
		j.put("msg", "success");
		return getClienSucMap(j, "00");
	}
	
	
	public static void main(String args[]) {
		
		
	}

}
