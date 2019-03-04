package com.mvw.medicalvisualteaching.bean;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by zhaomengen on 2017/10/25.
 * 用户考试表
 */
@Entity(
    nameInDb = "Exam"
)

public class Exam implements Serializable {

  @Transient
  private static final long serialVersionUID = -1703218985275056126L;
  @Id
  @Unique
  @Property(nameInDb = "id")
  private String id;//主建
  @Property(nameInDb = "examId")
  private String examId;//考试唯一标识
  @Property(nameInDb = "questionId")
  private String questionId;//试题id
  @Property(nameInDb = "answer")
  private String answer;//答案
  @Property(nameInDb = "remark1")
  private String remark1;//预留字段
  @Property(nameInDb = "remark2")
  private String remark2;//预留字段
  @Property(nameInDb = "leftSeconds")
  private long leftSeconds;
  @Property(nameInDb = "createTime")
  private Date createTime;
@Generated(hash = 18403158)
public Exam(String id, String examId, String questionId, String answer,
        String remark1, String remark2, long leftSeconds, Date createTime) {
    this.id = id;
    this.examId = examId;
    this.questionId = questionId;
    this.answer = answer;
    this.remark1 = remark1;
    this.remark2 = remark2;
    this.leftSeconds = leftSeconds;
    this.createTime = createTime;
}
@Generated(hash = 945526930)
public Exam() {
}
public String getExamId() {
    return this.examId;
}
public void setExamId(String examId) {
    this.examId = examId;
}
public String getQuestionId() {
    return this.questionId;
  }
public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }
public String getAnswer() {
    return this.answer;
}
public void setAnswer(String answer) {
    this.answer = answer;
}
public String getRemark1() {
    return this.remark1;
}
public void setRemark1(String remark1) {
    this.remark1 = remark1;
}
public String getRemark2() {
    return this.remark2;
}
public void setRemark2(String remark2) {
    this.remark2 = remark2;
}
public String getId() {
    return this.id;
}
public void setId(String id) {
    this.id = id;
}
public long getLeftSeconds() {
    return this.leftSeconds;
}
public void setLeftSeconds(long leftSeconds) {
    this.leftSeconds = leftSeconds;
}
public Date getCreateTime() {
    return this.createTime;
}
public void setCreateTime(Date createTime) {
    this.createTime = createTime;
}
}
