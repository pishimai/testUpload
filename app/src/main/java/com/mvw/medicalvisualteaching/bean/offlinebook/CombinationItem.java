package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "CombinationItem",
    createInDb = false
)
public class CombinationItem implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    /*
    "id":2359,
    "name":"椎体",
    "picture":"http://oss-cn-beijing.aliyuncs.com/mvw-imed3/mvw_imed_book/270102p01ac01.png"
     */
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "picture"
    )
    private String picture ;
    @Property(
        nameInDb = "mediaCombination_id"
    )
    private String mediaCombination_id;
    @Generated(hash = 1766833849)
    public CombinationItem(String id, String name, String picture,
            String mediaCombination_id) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.mediaCombination_id = mediaCombination_id;
    }
    @Generated(hash = 1655427391)
    public CombinationItem() {
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
    public String getPicture() {
        return this.picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getMediaCombination_id() {
        return this.mediaCombination_id;
    }
    public void setMediaCombination_id(String mediaCombination_id) {
        this.mediaCombination_id = mediaCombination_id;
    }


}
