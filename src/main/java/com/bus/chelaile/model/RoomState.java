package com.bus.chelaile.model;

public class RoomState {

	private int state;
	private String user;
	
	public RoomState() {
		super();
	}
	public RoomState(int state, String user) {
		super();
		this.state = state;
		this.user = user;
	}
	public int getState() {
		return state;
	}
	public void setState(int index) {
		this.state = index;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	
}
