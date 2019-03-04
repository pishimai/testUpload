package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 病情案例
 */
@Entity(
    nameInDb = "Section_CaseSection",
    createInDb = false
)
public class SectionCaseSection implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Transient
    private List<SectionCasesItem> casesItemList;

    @Generated(hash = 1340432177)
    public SectionCaseSection(String id) {
        this.id = id;
    }

    @Generated(hash = 440658430)
    public SectionCaseSection() {
    }

    public List<SectionCasesItem> getCasesItemList() {
        return casesItemList;
    }

    public void setCasesItemList(
        List<SectionCasesItem> casesItemList) {
        this.casesItemList = casesItemList;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
