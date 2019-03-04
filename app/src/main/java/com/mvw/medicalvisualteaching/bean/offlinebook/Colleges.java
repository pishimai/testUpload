package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "Colleges",
    createInDb = false
)
public class Colleges implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "name"
    )
    private String name ;
    @Generated(hash = 1162300217)
    public Colleges(String id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 1858797758)
    public Colleges() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
