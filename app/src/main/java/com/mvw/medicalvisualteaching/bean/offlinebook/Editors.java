package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "Editors",
    createInDb = false
)
public class Editors implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "college"
    )
    private String college;
    @Property(
        nameInDb = "name "
    )
    private String name ;
    @Property(
        nameInDb = "dutyType"
    )
    private String dutyType;
    @Property(
        nameInDb = "subject "
    )
    private String subject;
    @Generated(hash = 1587456979)
    public Editors(String id, String college, String name, String dutyType,
            String subject) {
        this.id = id;
        this.college = college;
        this.name = name;
        this.dutyType = dutyType;
        this.subject = subject;
    }
    @Generated(hash = 1676226055)
    public Editors() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCollege() {
        return this.college;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDutyType() {
        return this.dutyType;
    }
    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }
    public String getSubject() {
        return this.subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

}
