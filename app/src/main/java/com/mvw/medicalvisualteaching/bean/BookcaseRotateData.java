package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;

public class BookcaseRotateData implements Serializable{
	/*
	"id":"585027d8c91d1c0337570559",
	"category":"2",
	"type":"0",
	"sequence":1,
	"linkAddress":"",
	"titleName":"签到",
	"imagePath":"adverImages/7b69658b-12ae-4ace-a89f-690d0e4a3ec9.png"

	 */
	private String id;
	private String category;
	private String type;
	private String sequence;
	private String linkAddress;
	private String titleName;
	private String imagePath;
	private String isGoback;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getIsGoback() {
		return isGoback;
	}

	public void setIsGoback(String isGoback) {
		this.isGoback = isGoback;
	}
}
