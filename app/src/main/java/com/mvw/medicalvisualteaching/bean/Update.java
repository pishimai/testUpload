package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;

public class Update implements Serializable{
	private static final long serialVersionUID = 1L;
	private String path;
	private String action;
	private String introduce;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
}
