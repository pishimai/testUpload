package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "Committee",
    createInDb = false
)
public class Committee implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(
        nameInDb = "dutyTitle"
    )
    private String dutyTitle;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "title"
    )
    private String title;
    @Property(
        nameInDb = "unit"
    )
    private String unit;
    @Generated(hash = 1875459129)
    public Committee(String id, String dutyTitle, String name, String title,
            String unit) {
        this.id = id;
        this.dutyTitle = dutyTitle;
        this.name = name;
        this.title = title;
        this.unit = unit;
    }
    @Generated(hash = 2122919279)
    public Committee() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDutyTitle() {
        return this.dutyTitle;
    }
    public void setDutyTitle(String dutyTitle) {
        this.dutyTitle = dutyTitle;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

}
