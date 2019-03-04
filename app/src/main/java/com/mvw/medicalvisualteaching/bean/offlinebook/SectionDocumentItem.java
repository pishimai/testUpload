package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 参考文献
 */
@Entity(
    nameInDb = "DocumentItem",
    createInDb = false
)
public class SectionDocumentItem implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "indicator"
    )
    private String indicator;
    @Property(
        nameInDb = "item"
    )
    private String item;
    @Property(
        nameInDb = "document_id"
    )
    private String document_id;
    @Generated(hash = 1784883213)
    public SectionDocumentItem(String id, String indicator, String item,
            String document_id) {
        this.id = id;
        this.indicator = indicator;
        this.item = item;
        this.document_id = document_id;
    }
    @Generated(hash = 944803739)
    public SectionDocumentItem() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIndicator() {
        return this.indicator;
    }
    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }
    public String getItem() {
        return this.item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public String getDocument_id() {
        return this.document_id;
    }
    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }
}
