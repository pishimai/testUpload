package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 存储用户信息
 */
@Entity(
    nameInDb = "User"
)
public class User implements Serializable{
	@Transient
	public static final long serialVersionUID = 1l;
  @Unique
  @Property(nameInDb = "id")
	private String id;
  @Property(nameInDb = "token")
  private String token;
	@Property(nameInDb = "college")
  private String college;
  @Property(nameInDb = "city")
  private String city;
  @Property(nameInDb = "idNo")
  private String idNo;
  @Property(nameInDb = "major")
  private String major;
  @Property(nameInDb = "department")
  private String department;
  @Property(nameInDb = "email")
  private String email;
  @Property(nameInDb = "area")
  private String area;
  @Property(nameInDb = "qq")
  private String qq;
  @Property(nameInDb = "address")
  private String address;
  @Property(nameInDb = "nickName")
  private String nickName;
  @Property(nameInDb = "portrait")
  private String portrait;
  @Property(nameInDb = "unit")
  private String unit;
  @Property(nameInDb = "phone")
  private String phone;
  @Property(nameInDb = "caId")
  private String caId;
  @Property(nameInDb = "classNo")
  private String classNo;
  @Property(nameInDb = "name")
  private String name;
  @Property(nameInDb = "studentNo")
  private String studentNo;
//  @Property(nameInDb = "userType")
//  private String userType;
  @Property(nameInDb = "status")
  private String status;
//  @Property(nameInDb = "birthday")
//  private String birthday;
  @Property(nameInDb = "instituteNumber")
  private String instituteNumber;
  @Property(nameInDb = "comments")
  private String comments;
  @Property(nameInDb = "roles")
  private String roles;
  @Property(nameInDb = "genderCode")
  private String genderCode;
  @Property(nameInDb = "deleted")
  private String deleted;
  @Property(nameInDb = "cellphone")
  private String cellphone;
  @Property(nameInDb = "identificationNumber")
  private String identificationNumber;
  @Property(nameInDb = "pwd")
  private String pwd;
  @Property(nameInDb = "account")
  private String account;
  @Property(nameInDb = "guest")
  private String guest;
@Generated(hash = 1301410212)
public User(String id, String token, String college, String city, String idNo,
        String major, String department, String email, String area, String qq,
        String address, String nickName, String portrait, String unit,
        String phone, String caId, String classNo, String name,
        String studentNo, String status, String instituteNumber,
        String comments, String roles, String genderCode, String deleted,
        String cellphone, String identificationNumber, String pwd,
        String account, String guest) {
    this.id = id;
    this.token = token;
    this.college = college;
    this.city = city;
    this.idNo = idNo;
    this.major = major;
    this.department = department;
    this.email = email;
    this.area = area;
    this.qq = qq;
    this.address = address;
    this.nickName = nickName;
    this.portrait = portrait;
    this.unit = unit;
    this.phone = phone;
    this.caId = caId;
    this.classNo = classNo;
    this.name = name;
    this.studentNo = studentNo;
    this.status = status;
    this.instituteNumber = instituteNumber;
    this.comments = comments;
    this.roles = roles;
    this.genderCode = genderCode;
    this.deleted = deleted;
    this.cellphone = cellphone;
    this.identificationNumber = identificationNumber;
    this.pwd = pwd;
    this.account = account;
    this.guest = guest;
}
@Generated(hash = 586692638)
public User() {
}
public String getId() {
    return this.id;
}
public void setId(String id) {
    this.id = id;
}
public String getToken() {
    return this.token;
}
public void setToken(String token) {
    this.token = token;
}
public String getCollege() {
    return this.college;
}
public void setCollege(String college) {
    this.college = college;
}
public String getCity() {
    return this.city;
}
public void setCity(String city) {
    this.city = city;
}
public String getIdNo() {
    return this.idNo;
}
public void setIdNo(String idNo) {
    this.idNo = idNo;
}
public String getMajor() {
    return this.major;
}
public void setMajor(String major) {
    this.major = major;
}
public String getDepartment() {
    return this.department;
}
public void setDepartment(String department) {
    this.department = department;
}
public String getEmail() {
    return this.email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getArea() {
    return this.area;
}
public void setArea(String area) {
    this.area = area;
}
public String getQq() {
    return this.qq;
}
public void setQq(String qq) {
    this.qq = qq;
}
public String getAddress() {
    return this.address;
}
public void setAddress(String address) {
    this.address = address;
}
public String getNickName() {
    return this.nickName;
}
public void setNickName(String nickName) {
    this.nickName = nickName;
}
public String getPortrait() {
    return this.portrait;
}
public void setPortrait(String portrait) {
    this.portrait = portrait;
}
public String getUnit() {
    return this.unit;
}
public void setUnit(String unit) {
    this.unit = unit;
}
public String getPhone() {
    return this.phone;
}
public void setPhone(String phone) {
    this.phone = phone;
}
public String getCaId() {
    return this.caId;
}
public void setCaId(String caId) {
    this.caId = caId;
}
public String getClassNo() {
    return this.classNo;
}
public void setClassNo(String classNo) {
    this.classNo = classNo;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getStudentNo() {
    return this.studentNo;
}
public void setStudentNo(String studentNo) {
    this.studentNo = studentNo;
}
public String getStatus() {
    return this.status;
}
public void setStatus(String status) {
    this.status = status;
}
public String getInstituteNumber() {
    return this.instituteNumber;
}
public void setInstituteNumber(String instituteNumber) {
    this.instituteNumber = instituteNumber;
}
public String getComments() {
    return this.comments;
}
public void setComments(String comments) {
    this.comments = comments;
}
public String getRoles() {
    return this.roles;
}
public void setRoles(String roles) {
    this.roles = roles;
}
public String getGenderCode() {
    return this.genderCode;
}
public void setGenderCode(String genderCode) {
    this.genderCode = genderCode;
}
public String getDeleted() {
    return this.deleted;
}
public void setDeleted(String deleted) {
    this.deleted = deleted;
}
public String getCellphone() {
    return this.cellphone;
}
public void setCellphone(String cellphone) {
    this.cellphone = cellphone;
}
public String getIdentificationNumber() {
    return this.identificationNumber;
}
public void setIdentificationNumber(String identificationNumber) {
    this.identificationNumber = identificationNumber;
}
public String getPwd() {
    return this.pwd;
}
public void setPwd(String pwd) {
    this.pwd = pwd;
}
public String getAccount() {
    return this.account;
}
public void setAccount(String account) {
    this.account = account;
}
public String getGuest() {
    return this.guest;
}
public void setGuest(String guest) {
    this.guest = guest;
}



}
