package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 病例分析内容
 */
@Entity(
    nameInDb = "CasesItem",
    createInDb = false
)
public class SectionCasesItem implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    @Id
    private String id;
    @Property(
        nameInDb = "f_order"
    )
    private String f_order;
    @Property(
        nameInDb = "paragraphId"
    )
    private String paragraphId;
    @Property(
        nameInDb = "text"
    )
    private String text;
    @Property(
        nameInDb = "title"
    )
    private String title;
    @Property(
        nameInDb = "cases_id"
    )
    private String cases_id;
    @Property(
        nameInDb = "parent_id"
    )
    private String parent_id;
    @Transient
    private List<Author> authors;
    @Transient
    private List<SectionCasesItem> section;

    @Generated(hash = 187778398)
    public SectionCasesItem(String id, String f_order, String paragraphId,
            String text, String title, String cases_id, String parent_id) {
        this.id = id;
        this.f_order = f_order;
        this.paragraphId = paragraphId;
        this.text = text;
        this.title = title;
        this.cases_id = cases_id;
        this.parent_id = parent_id;
    }

    @Generated(hash = 591387063)
    public SectionCasesItem() {
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<SectionCasesItem> getSection() {
        return section;
    }

    public void setSection(
        List<SectionCasesItem> section) {
        this.section = section;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_order() {
        return this.f_order;
    }

    public void setF_order(String f_order) {
        this.f_order = f_order;
    }

    public String getParagraphId() {
        return this.paragraphId;
    }

    public void setParagraphId(String paragraphId) {
        this.paragraphId = paragraphId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCases_id() {
        return this.cases_id;
    }

    public void setCases_id(String cases_id) {
        this.cases_id = cases_id;
    }

    public String getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
