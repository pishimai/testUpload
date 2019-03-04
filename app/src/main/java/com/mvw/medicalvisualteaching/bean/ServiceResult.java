package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;

public class ServiceResult implements Serializable{
		private String flag;
		private String result;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
