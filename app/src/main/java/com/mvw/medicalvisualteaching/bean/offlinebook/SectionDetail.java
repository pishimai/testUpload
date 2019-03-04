package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "section_detail",
    createInDb = false
)
public class SectionDetail implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "extend"
    )
    private String extend;
    @Generated(hash = 95080549)
    public SectionDetail(String id, String extend) {
        this.id = id;
        this.extend = extend;
    }
    @Generated(hash = 426695625)
    public SectionDetail() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExtend() {
        return this.extend;
    }
    public void setExtend(String extend) {
        this.extend = extend;
    }
}
