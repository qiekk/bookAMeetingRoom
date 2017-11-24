package com.bus.chelaile.mvc;

import javax.servlet.http.HttpServletRequest;


public class AbstractController {

	public MeetingParam getActionParam(HttpServletRequest request) {
		MeetingParam param = new MeetingParam();
	
		param.setDate(request.getParameter("date"));
		param.setRoomId(getInt(request, "roomId"));
		param.setStart(getInt(request, "start"));
		param.setEnd(getInt(request, "end"));
		param.setNewState(getInt(request, "newState"));
		param.setUser(request.getParameter("user"));
		
		
		return param;
	}
	
	protected static int getInt(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0) {
			return -1;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				return -1;
			}
		}
	}
}
