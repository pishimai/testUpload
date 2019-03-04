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
 * 参考文献
 */
@Entity(
    nameInDb = "Section_Document",
    createInDb = false
)
public class SectionDocument implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Unique
    @Id
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Transient
    private List<SectionDocumentItem> documents;

    @Generated(hash = 1848023786)
    public SectionDocument(String id) {
        this.id = id;
    }

    @Generated(hash = 362059957)
    public SectionDocument() {
    }

    public List<SectionDocumentItem> getDocuments() {
        return documents;
    }

    public void setDocuments(
        List<SectionDocumentItem> documents) {
        this.documents = documents;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
