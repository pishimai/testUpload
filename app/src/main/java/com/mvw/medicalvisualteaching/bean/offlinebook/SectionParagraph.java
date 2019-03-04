package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "Section_Paragraph",
    createInDb = false
)
public class SectionParagraph implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;

    @Property(
        nameInDb = "sections_id"
    )
    @Index(unique = true)
    private String sectionId;
    @Property(
        nameInDb = "paragraphs_id"
    )
    private String paragraphsId;
    @Generated(hash = 277776310)
    public SectionParagraph(String sectionId, String paragraphsId) {
        this.sectionId = sectionId;
        this.paragraphsId = paragraphsId;
    }
    @Generated(hash = 1198564075)
    public SectionParagraph() {
    }
    public String getSectionId() {
        return this.sectionId;
    }
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }
    public String getParagraphsId() {
        return this.paragraphsId;
    }
    public void setParagraphsId(String paragraphsId) {
        this.paragraphsId = paragraphsId;
    }

}
