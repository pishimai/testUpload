package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 */
@Entity(
    nameInDb = "Imprint_editors",
    createInDb = false
)
public class SectionImprintEditor implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    @Property(
        nameInDb = "Imprint_id"
    )
    private String Imprint_id;
    @Property(
        nameInDb = "editors"
    )
    private String editors;
    @Generated(hash = 1244737168)
    public SectionImprintEditor(String Imprint_id, String editors) {
        this.Imprint_id = Imprint_id;
        this.editors = editors;
    }
    @Generated(hash = 708025906)
    public SectionImprintEditor() {
    }
    public String getImprint_id() {
        return this.Imprint_id;
    }
    public void setImprint_id(String Imprint_id) {
        this.Imprint_id = Imprint_id;
    }
    public String getEditors() {
        return this.editors;
    }
    public void setEditors(String editors) {
        this.editors = editors;
    }

}
