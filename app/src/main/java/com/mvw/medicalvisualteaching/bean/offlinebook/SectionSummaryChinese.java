package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 小结中文
 */
@Entity(
    nameInDb = "Summary_chinese",
    createInDb = false
)
public class SectionSummaryChinese implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Property(
        nameInDb = "Summary_id"
    )
    private String Summary_id;
    @Property(
        nameInDb = "chinese"
    )
    private String chinese;
    @Generated(hash = 1159743736)
    public SectionSummaryChinese(String Summary_id, String chinese) {
        this.Summary_id = Summary_id;
        this.chinese = chinese;
    }
    @Generated(hash = 1795314116)
    public SectionSummaryChinese() {
    }
    public String getSummary_id() {
        return this.Summary_id;
    }
    public void setSummary_id(String Summary_id) {
        this.Summary_id = Summary_id;
    }
    public String getChinese() {
        return this.chinese;
    }
    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

}
