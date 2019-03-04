package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "MediaSequence",
    createInDb = false
)
public class MediaSequence implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    /*
        "pattern":"http://oss-cn-beijing.aliyuncs.com/mvw-imed3/mvw_imed_book/imed_27/picture/270600p02xl_",
        "count":73,
        "type":"jpg",
        "step":5,
        "reverse":true
     */
    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(
        nameInDb = "pattern"
    )
    private String pattern;
    @Property(
        nameInDb = "count"
    )
    private int count;
    @Property(
        nameInDb = "type"
    )
    private String type;
    @Property(
        nameInDb = "step"
    )
    private int step;
    @Property(
        nameInDb = "reverse"
    )
    private int reverse;
    @Transient
    private List<String> pictures;
    @Generated(hash = 1531866144)
    public MediaSequence(String id, String pattern, int count, String type, int step, int reverse) {
        this.id = id;
        this.pattern = pattern;
        this.count = count;
        this.type = type;
        this.step = step;
        this.reverse = reverse;
    }

    @Generated(hash = 342868584)
    public MediaSequence() {
    }
    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStep() {
        return this.step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getReverse() {
        return this.reverse;
    }

    public void setReverse(int reverse) {
        this.reverse = reverse;
    }
}
