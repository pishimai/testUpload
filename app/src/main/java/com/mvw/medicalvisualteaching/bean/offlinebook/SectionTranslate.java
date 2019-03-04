package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 名词对照索引
 */
@Entity(
    nameInDb = "Translate",
    createInDb = false
)
public class SectionTranslate implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Id
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "chinese"
    )
    private String chinese;
    @Property(
        nameInDb = "english"
    )
    private String english;
    @Property(
        nameInDb = "initial"
    )
    private String initial;
    @Property(
        nameInDb = "DTYPE"
    )
    private String dtype;
    @Generated(hash = 1798188118)
    public SectionTranslate(String id, String chinese, String english,
            String initial, String dtype) {
        this.id = id;
        this.chinese = chinese;
        this.english = english;
        this.initial = initial;
        this.dtype = dtype;
    }
    @Generated(hash = 11385959)
    public SectionTranslate() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getChinese() {
        return this.chinese;
    }
    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
    public String getEnglish() {
        return this.english;
    }
    public void setEnglish(String english) {
        this.english = english;
    }
    public String getInitial() {
        return this.initial;
    }
    public void setInitial(String initial) {
        this.initial = initial;
    }
    public String getDtype() {
        return this.dtype;
    }
    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

}
