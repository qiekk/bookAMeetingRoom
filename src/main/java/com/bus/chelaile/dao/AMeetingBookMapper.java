package com.bus.chelaile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bus.chelaile.model.MeetingInfo;



public interface AMeetingBookMapper {
   // List<AdContent> list();
    List<MeetingInfo> listValidReservation(@Param("date") String date);
    List<MeetingInfo> listAllReservation(@Param("date") String date);
    boolean updateReservation(@Param("date") String date, @Param("room_id") int roomId, @Param("user") String user, @Param("time_index") int timeIndex, @Param("state") int newState);
	boolean addReservation(@Param("date") String date, @Param("room_id") int roomId, @Param("user") String user, @Param("time_index") int timeIndex, @Param("state") int newState);
}
