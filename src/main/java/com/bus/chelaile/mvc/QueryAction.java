package com.bus.chelaile.mvc;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bus.chelaile.service.ServiceManager;

/**
 * 广告相关接口
 * 
 * @author zzz
 * 
 */

@Controller
@RequestMapping("")
public class QueryAction extends AbstractController {

	@Resource
	private ServiceManager serviceManager;

	private static final Logger log = LoggerFactory
			.getLogger(QueryAction.class);

	/*
	 * 获取符合条件的所有会议室预定
	 */
	@ResponseBody
	@RequestMapping(value = "bmr!getAllPlans.action", produces = "Content-Type=text/plain;charset=UTF-8")
	public String getAllReservation(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		MeetingParam param = getActionParam(request);

		return serviceManager.getAllPlan(param);
	}
	
	/**
	 * 获取最近7天
	 */
	@ResponseBody
	@RequestMapping(value = "bmr!sevenDays.action", produces = "Content-Type=text/plain;charset=UTF-8")
	public String sevenDays(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		log.info("request for seven days");
		
		return serviceManager.getServenDays();
	}
	
	
	/*
	 * 修改会议室预定计划
	 */
	@ResponseBody
	@RequestMapping(value = "bmr!updatePlan.action", produces = "Content-Type=text/plain;charset=UTF-8")
	public String updatePlan(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {

		MeetingParam param = getActionParam(request);

		log.info("update plan, user={}, date={}, roomId={}, starIndex={}, endIndex={}, newState={}", 
				param.getUser(), param.getDate(), param.getRoomId(), param.getStart(), param.getEnd(), param.getNewState());
		
		return serviceManager.updatePlan(param);
	}

}
