package com.mvw.medicalvisualteaching.bean;

import android.os.Build;
import android.os.Build.VERSION;

/**
 * 用户埋点
 */
public class Log {
  //用户ID	 设备型号	系统版本	 APP版本 	行为 	在线状态	0在线，1离线  教材ID	章ID	 节ID	时间 URL

  private String userId = "";
  private String deviceModel = Build.MODEL;
  private String device = "androidPhone";
  private String systemVersion = VERSION.RELEASE;
  private String appVersion = "";
  private String action = "";
  private String actionType = "";//行为类型  0:进入  1：退出
  private String online = "";
  private String isbn = "";
  private String chapterId = "";
  private String sectionId = "";
  private long date ;
  private String url = "";

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getSystemVersion() {
    return systemVersion;
  }

  public void setSystemVersion(String systemVersion) {
    this.systemVersion = systemVersion;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }


  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  public String getOnline() {
    return online;
  }

  public void setOnline(String online) {
    this.online = online;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getChapterId() {
    return chapterId;
  }

  public void setChapterId(String chapterId) {
    this.chapterId = chapterId;
  }

  public String getSectionId() {
    return sectionId;
  }

  public void setSectionId(String sectionId) {
    this.sectionId = sectionId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }
}
