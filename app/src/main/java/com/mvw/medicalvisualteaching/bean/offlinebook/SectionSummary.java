package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 小结
 */
@Entity(
    nameInDb = "Section_Summary",
    createInDb = false
)
public class SectionSummary implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "audio"
    )
    private String audio;

    @Transient
    private List<String> chinese;
    @Transient
    private List<String> english;
    @Generated(hash = 1137597616)
    public SectionSummary(String id, String audio) {
        this.id = id;
        this.audio = audio;
    }
    @Generated(hash = 168612115)
    public SectionSummary() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAudio() {
        return this.audio;
    }
    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<String> getChinese() {
        return chinese;
    }

    public void setChinese(List<String> chinese) {
        this.chinese = chinese;
    }

    public List<String> getEnglish() {
        return english;
    }

    public void setEnglish(List<String> english) {
        this.english = english;
    }
}
