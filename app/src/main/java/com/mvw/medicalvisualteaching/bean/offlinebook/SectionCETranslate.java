package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 中英文名词对照索引
 */
@Entity(
    nameInDb = "CETranslate",
    createInDb = false
)
public class SectionCETranslate implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Id
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "chineseToEnglish_id"
    )
    private String chineseToEnglish_id;
    @Generated(hash = 1575786120)
    public SectionCETranslate(String id, String chineseToEnglish_id) {
        this.id = id;
        this.chineseToEnglish_id = chineseToEnglish_id;
    }
    @Generated(hash = 1128874845)
    public SectionCETranslate() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getChineseToEnglish_id() {
        return this.chineseToEnglish_id;
    }
    public void setChineseToEnglish_id(String chineseToEnglish_id) {
        this.chineseToEnglish_id = chineseToEnglish_id;
    }

}
