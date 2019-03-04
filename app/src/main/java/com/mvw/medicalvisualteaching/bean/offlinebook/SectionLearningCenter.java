package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.Map;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 学习园地
 */
@Entity(
    nameInDb = "Section_LearningCenter",
    createInDb = false
)
public class SectionLearningCenter implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
   private String id;
    @Transient
    private Map<String,Object> reference;
    @Transient
    private Map<String,Object> shorthand;

    @Generated(hash = 2026069407)
    public SectionLearningCenter(String id) {
        this.id = id;
    }

    @Generated(hash = 305165358)
    public SectionLearningCenter() {
    }

  public Map<String, Object> getReference() {
    return reference;
  }

  public void setReference(Map<String, Object> reference) {
    this.reference = reference;
  }

  public Map<String, Object> getShorthand() {
    return shorthand;
  }

  public void setShorthand(Map<String, Object> shorthand) {
    this.shorthand = shorthand;
  }

  public String getId() {
      return this.id;
  }

  public void setId(String id) {
      this.id = id;
  }
}
