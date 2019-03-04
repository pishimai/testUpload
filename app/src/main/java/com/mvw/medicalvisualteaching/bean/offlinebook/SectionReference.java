package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 参考
 */
@Entity(
    nameInDb = "Section_Reference",
    createInDb = false
)
public class SectionReference implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    private String id;
    @Property(
        nameInDb = "learningCenter_id"
    )
    private String learningCenter_id;
    @Generated(hash = 2068647607)
    public SectionReference(String id, String learningCenter_id) {
        this.id = id;
        this.learningCenter_id = learningCenter_id;
    }
    @Generated(hash = 986282744)
    public SectionReference() {
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
