package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "AuthorItem_contents",
    createInDb = false
)
public class AuthorItemContents implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "AuthorItem_id"
    )
    private String AuthorItem_id;
    @Property(
        nameInDb = "contents"
    )
    private String contents;
    @Generated(hash = 1497727627)
    public AuthorItemContents(String AuthorItem_id, String contents) {
        this.AuthorItem_id = AuthorItem_id;
        this.contents = contents;
    }
    @Generated(hash = 471286560)
    public AuthorItemContents() {
    }
    public String getAuthorItem_id() {
        return this.AuthorItem_id;
    }
    public void setAuthorItem_id(String AuthorItem_id) {
        this.AuthorItem_id = AuthorItem_id;
    }
    public String getContents() {
        return this.contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }


}
