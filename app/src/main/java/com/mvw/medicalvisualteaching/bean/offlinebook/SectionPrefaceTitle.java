package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 序的头衔
 */
@Entity(
    nameInDb = "Preface_titles",
    createInDb = false
)
public class SectionPrefaceTitle implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "Preface_id"
    )
    private String Preface_id;
    @Property(
        nameInDb = "titles"
    )
    private String titles;
    @Generated(hash = 1264180765)
    public SectionPrefaceTitle(String Preface_id, String titles) {
        this.Preface_id = Preface_id;
        this.titles = titles;
    }
    @Generated(hash = 678541338)
    public SectionPrefaceTitle() {
    }
    public String getPreface_id() {
        return this.Preface_id;
    }
    public void setPreface_id(String Preface_id) {
        this.Preface_id = Preface_id;
    }
    public String getTitles() {
        return this.titles;
    }
    public void setTitles(String titles) {
        this.titles = titles;
    }


}
