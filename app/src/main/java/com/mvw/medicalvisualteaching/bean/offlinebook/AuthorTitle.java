package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "Author_title",
    createInDb = false
)
public class AuthorTitle implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "Author_id"
    )
    private String Author_id;
    @Property(
        nameInDb = "title"
    )
    private String title;
    @Generated(hash = 1014647910)
    public AuthorTitle(String Author_id, String title) {
        this.Author_id = Author_id;
        this.title = title;
    }
    @Generated(hash = 729346653)
    public AuthorTitle() {
    }
    public String getAuthor_id() {
        return this.Author_id;
    }
    public void setAuthor_id(String Author_id) {
        this.Author_id = Author_id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
