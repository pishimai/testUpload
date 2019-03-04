package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 作者
 */
@Entity(
    nameInDb = "Paragraph_Author",
    createInDb = false
)
public class ParagraphAuthor implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "Paragraph_id"
    )
    private String Paragraph_id;
    @Property(
        nameInDb = "authors_id"
    )
    private String authors_id;
    @Generated(hash = 680209835)
    public ParagraphAuthor(String Paragraph_id, String authors_id) {
        this.Paragraph_id = Paragraph_id;
        this.authors_id = authors_id;
    }
    @Generated(hash = 527594268)
    public ParagraphAuthor() {
    }
    public String getParagraph_id() {
        return this.Paragraph_id;
    }
    public void setParagraph_id(String Paragraph_id) {
        this.Paragraph_id = Paragraph_id;
    }
    public String getAuthors_id() {
        return this.authors_id;
    }
    public void setAuthors_id(String authors_id) {
        this.authors_id = authors_id;
    }

}
