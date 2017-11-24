package com.bus.chelaile.mvc;

public class MeetingParam {

	private String date;
	private int roomId;
	private int start;
	private int end;
	private int newState;
	private String user;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getNewState() {
		return newState;
	}
	public void setNewState(int newState) {
		this.newState = newState;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
}
