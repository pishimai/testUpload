package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 名单
 */
@Entity(
    nameInDb = "NameList",
    createInDb = false
)
public class SectionNameList implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    private String id;
    @Property(
        nameInDb = "college"
    )
    private String college;
    @Property(
        nameInDb = "kind"
    )
    private String kind;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "f_order"
    )
    private String order;
    @Property(
        nameInDb = "acknowledgements_id"
    )
    private String acknowledgements_id;
    @Generated(hash = 938355066)
    public SectionNameList(String id, String college, String kind, String name,
            String order, String acknowledgements_id) {
        this.id = id;
        this.college = college;
        this.kind = kind;
        this.name = name;
        this.order = order;
        this.acknowledgements_id = acknowledgements_id;
    }
    @Generated(hash = 1872713124)
    public SectionNameList() {
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
    public String getKind() {
        return this.kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOrder() {
        return this.order;
    }
    public void setOrder(String order) {
        this.order = order;
    }
    public String getAcknowledgements_id() {
        return this.acknowledgements_id;
    }
    public void setAcknowledgements_id(String acknowledgements_id) {
        this.acknowledgements_id = acknowledgements_id;
    }

}
