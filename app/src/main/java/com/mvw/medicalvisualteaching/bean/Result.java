package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;

public class Result implements Serializable{
	//结果json
	private String response;
	//流水
	private String sn;
	//协议
	private String command;
	private boolean success;
	private Object data;


	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
