package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 致谢名单
 */
@Entity(
    nameInDb = "Section_Acknowledgements",
    createInDb = false
)
public class SectionAcknowledgements implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Id
    private String id;
    @Transient
    private List<SectionNameList> editors;
    @Transient
    private List<SectionNameList> thanks;
    @Generated(hash = 438140218)
    public SectionAcknowledgements(String id) {
        this.id = id;
    }
    @Generated(hash = 1403260261)
    public SectionAcknowledgements() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<SectionNameList> getEditors() {
        return editors;
    }

    public void setEditors(
        List<SectionNameList> editors) {
        this.editors = editors;
    }

    public List<SectionNameList> getThanks() {
        return thanks;
    }

    public void setThanks(
        List<SectionNameList> thanks) {
        this.thanks = thanks;
    }
}
