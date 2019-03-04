package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "MediaPPTItem",
    createInDb = false
)
public class MediaPPTItem implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;

    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(
        nameInDb = "pptCombination_id"
    )
    private String pptCombination_id;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "path"
    )
    private String path;
    @Generated(hash = 2010932831)
    public MediaPPTItem(String id, String pptCombination_id, String name,
            String path) {
        this.id = id;
        this.pptCombination_id = pptCombination_id;
        this.name = name;
        this.path = path;
    }
    @Generated(hash = 997685201)
    public MediaPPTItem() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPptCombination_id() {
        return this.pptCombination_id;
    }
    public void setPptCombination_id(String pptCombination_id) {
        this.pptCombination_id = pptCombination_id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }

}
