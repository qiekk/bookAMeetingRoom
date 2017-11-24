package com.bus.chelaile.model;

import java.util.ArrayList;

public class RoomsInfo {
	
	private int id;
	private String name;
	ArrayList<RoomState> stateList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<RoomState> getStateList() {
		return stateList;
	}
	public void setStateList(ArrayList<RoomState> stateList) {
		this.stateList = stateList;
	}

	
}
