package com.mvw.medicalvisualteaching.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 * Created by zhaomengen on 2017/2/15.
 */
@Entity(
    nameInDb = "UserBook"
)
public class UserBook {
  @Id
  @Index(unique = true)
  @Property(nameInDb = "id")
  private String id;
  @Property(nameInDb = "uId")
  private String uId;
  @Property(nameInDb = "bIsbn")
  private String bIsbn;
  @Property(nameInDb = "state")
  private String state;//书籍状态
  @Property(nameInDb = "buyStatus")
  private String buyStatus;//购买状态 0:未购买,1:已购买(免费书籍为已购买)
  @Property(nameInDb = "type")
  private String type;//0:正常购买,1:免费书籍的下载(相当于购买),2:借阅
  @Property(nameInDb = "day")
  private String day;//借阅剩余天数
  @Property(nameInDb = "bookDeadline")
  private String bookDeadline;//借阅到期日期
  @Property(nameInDb = "isExpired")
  private String isExpired;//是否已过期	0:未过期,1:已过期,2:已购买不过期
  @Property(nameInDb = "order")
  private int order;
  @Property(nameInDb = "sectionId")
  private String sectionId;
  @Property(nameInDb = "patchVersion")
  private String patchVersion;//当前下载的书籍的版本
  @Property(nameInDb = "nonWifi")
  private String nonWifi = "0";//是否4g下下载书籍
@Generated(hash = 1668298134)
public UserBook(String id, String uId, String bIsbn, String state,
        String buyStatus, String type, String day, String bookDeadline,
        String isExpired, int order, String sectionId, String patchVersion,
        String nonWifi) {
    this.id = id;
    this.uId = uId;
    this.bIsbn = bIsbn;
    this.state = state;
    this.buyStatus = buyStatus;
    this.type = type;
    this.day = day;
    this.bookDeadline = bookDeadline;
    this.isExpired = isExpired;
    this.order = order;
    this.sectionId = sectionId;
    this.patchVersion = patchVersion;
    this.nonWifi = nonWifi;
}
@Generated(hash = 234160182)
public UserBook() {
}
public String getId() {
    return this.id;
}
public void setId(String id) {
    this.id = id;
}
public String getUId() {
    return this.uId;
}
public void setUId(String uId) {
    this.uId = uId;
}
public String getBIsbn() {
    return this.bIsbn;
}
public void setBIsbn(String bIsbn) {
    this.bIsbn = bIsbn;
}
public String getState() {
    return this.state;
}
public void setState(String state) {
    this.state = state;
}
public String getBuyStatus() {
    return this.buyStatus;
}
public void setBuyStatus(String buyStatus) {
    this.buyStatus = buyStatus;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getDay() {
    return this.day;
}
public void setDay(String day) {
    this.day = day;
}
public String getBookDeadline() {
    return this.bookDeadline;
}
public void setBookDeadline(String bookDeadline) {
    this.bookDeadline = bookDeadline;
}
public String getIsExpired() {
    return this.isExpired;
}
public void setIsExpired(String isExpired) {
    this.isExpired = isExpired;
}
public int getOrder() {
    return this.order;
}
public void setOrder(int order) {
    this.order = order;
}
public String getSectionId() {
    return this.sectionId;
}
public void setSectionId(String sectionId) {
    this.sectionId = sectionId;
}
public String getPatchVersion() {
    return this.patchVersion;
}
public void setPatchVersion(String patchVersion) {
    this.patchVersion = patchVersion;
}
public String getNonWifi() {
    return this.nonWifi;
}
public void setNonWifi(String nonWifi) {
    this.nonWifi = nonWifi;
}
public void setOrder(Integer order) {
    this.order = order;
}
}
