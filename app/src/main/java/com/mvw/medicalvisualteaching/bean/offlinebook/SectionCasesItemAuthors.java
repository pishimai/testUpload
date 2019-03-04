package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 病情案例作者
 */
@Entity(
    nameInDb = "CasesItem_Author",
    createInDb = false
)
public class SectionCasesItemAuthors implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    @Property(
        nameInDb = "CasesItem_id"
    )
    private String cases_id;
    @Property(
        nameInDb = "authors_id"
    )
    private String authors_id;
    @Generated(hash = 553075538)
    public SectionCasesItemAuthors(String cases_id, String authors_id) {
        this.cases_id = cases_id;
        this.authors_id = authors_id;
    }
    @Generated(hash = 299212883)
    public SectionCasesItemAuthors() {
    }
    public String getCases_id() {
        return this.cases_id;
    }
    public void setCases_id(String cases_id) {
        this.cases_id = cases_id;
    }
    public String getAuthors_id() {
        return this.authors_id;
    }
    public void setAuthors_id(String authors_id) {
        this.authors_id = authors_id;
    }

}
