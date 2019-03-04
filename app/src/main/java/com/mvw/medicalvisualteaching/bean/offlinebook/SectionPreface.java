package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 总序
 */
@Entity(
    nameInDb = "Section_Preface",
    createInDb = false
)
public class SectionPreface implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    private String id;
    @Property(
        nameInDb = "mark"
    )
    private String mark;
    @Property(
        nameInDb = "mark1"
    )
    private String mark1;
    @Property(
        nameInDb = "timestamp"
    )
    private String timestamp;
    @Generated(hash = 377231848)
    public SectionPreface(String id, String mark, String mark1, String timestamp) {
        this.id = id;
        this.mark = mark;
        this.mark1 = mark1;
        this.timestamp = timestamp;
    }
    @Generated(hash = 589247534)
    public SectionPreface() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMark() {
        return this.mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public String getMark1() {
        return this.mark1;
    }
    public void setMark1(String mark1) {
        this.mark1 = mark1;
    }
    public String getTimestamp() {
        return this.timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
