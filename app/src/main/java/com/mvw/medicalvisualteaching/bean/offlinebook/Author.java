package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "Author",
    createInDb = false
)
public class Author implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "photo"
    )
    private String photo;
    @Property(
        nameInDb = "name"
    )
    private String name ;
    @Property(
        nameInDb = "gender"
    )
    private String gender;
    @Property(nameInDb = "isExtend")
    private String isExtend;
    @Transient
    private List<String> title;
    @Transient
    private boolean extend;
    @Transient
    private List<AuthorItem> authorItems;

    @Generated(hash = 936013815)
    public Author(String id, String photo, String name, String gender,
            String isExtend) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.gender = gender;
        this.isExtend = isExtend;
    }

    @Generated(hash = 64241762)
    public Author() {
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<AuthorItem> getAuthorItems() {
        return authorItems;
    }

    public void setAuthorItems(
        List<AuthorItem> authorItems) {
        this.authorItems = authorItems;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIsExtend() {
        return this.isExtend;
    }

    public void setIsExtend(String isExtend) {
        this.isExtend = isExtend;
    }

    public boolean isExtend() {
        return extend;
    }

    public void setExtend(boolean extend) {
        this.extend = extend;
    }
}
