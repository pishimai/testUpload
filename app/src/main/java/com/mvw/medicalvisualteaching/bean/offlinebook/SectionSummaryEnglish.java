package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 小结英文
 */
@Entity(
    nameInDb = "Summary_english",
    createInDb = false
)
public class SectionSummaryEnglish implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Property(
        nameInDb = "Summary_id"
    )
    private String Summary_id;
    @Property(
        nameInDb = "english"
    )
    private String english;
    @Generated(hash = 1208781150)
    public SectionSummaryEnglish(String Summary_id, String english) {
        this.Summary_id = Summary_id;
        this.english = english;
    }
    @Generated(hash = 875721368)
    public SectionSummaryEnglish() {
    }
    public String getSummary_id() {
        return this.Summary_id;
    }
    public void setSummary_id(String Summary_id) {
        this.Summary_id = Summary_id;
    }
    public String getEnglish() {
        return this.english;
    }
    public void setEnglish(String english) {
        this.english = english;
    }

}
