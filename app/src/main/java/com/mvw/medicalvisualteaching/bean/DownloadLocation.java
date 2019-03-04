package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;

public class DownloadLocation implements Serializable{
	private int id;
	private String name;
	private long availableSize;
	private String selected;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAvailableSize() {
		return availableSize;
	}

	public void setAvailableSize(long availableSize) {
		this.availableSize = availableSize;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
