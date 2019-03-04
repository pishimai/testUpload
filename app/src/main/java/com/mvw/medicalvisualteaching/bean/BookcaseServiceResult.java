package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;

public class BookcaseServiceResult implements Serializable{
	private String flag;
	private BookcaseResult result;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BookcaseResult getResult() {
		return result;
	}

	public void setResult(BookcaseResult result) {
		this.result = result;
	}
}
