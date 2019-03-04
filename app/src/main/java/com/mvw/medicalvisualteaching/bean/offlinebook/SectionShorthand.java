package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 速记
 */
@Entity(
    nameInDb = "Section_Shorthand",
    createInDb = false
)
public class SectionShorthand implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    private String id;
    @Property(
        nameInDb = "learningCenter_id"
    )
    private String learningCenter_id;
    @Generated(hash = 1929397504)
    public SectionShorthand(String id, String learningCenter_id) {
        this.id = id;
        this.learningCenter_id = learningCenter_id;
    }
    @Generated(hash = 3493077)
    public SectionShorthand() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLearningCenter_id() {
        return this.learningCenter_id;
    }
    public void setLearningCenter_id(String learningCenter_id) {
        this.learningCenter_id = learningCenter_id;
    }

}
