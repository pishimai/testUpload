package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 封面
 */
@Entity(
    nameInDb = "Section_Cover",
    createInDb = false
)
public class SectionCover implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Property(
        nameInDb = "id"
    )
    @Id
    private String id;
    @Property(
        nameInDb = "backgroundColor"
    )
    private String backgroundColor;
    @Property(
        nameInDb = "picture"
    )
    private String  picture;
    @Generated(hash = 2098220388)
    public SectionCover(String id, String backgroundColor, String picture) {
        this.id = id;
        this.backgroundColor = backgroundColor;
        this.picture = picture;
    }
    @Generated(hash = 1387031076)
    public SectionCover() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBackgroundColor() {
        return this.backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public String getPicture() {
        return this.picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
