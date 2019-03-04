package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 封底
 */
@Entity(
    nameInDb = "Section_BackCover",
    createInDb = false
)
public class SectionBackCover implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    private String id;
    @Property(
        nameInDb = "introduce"
    )
    private String introduce;
    @Property(
        nameInDb = "isbn"
    )
    private String isbn;
    @Generated(hash = 2137275634)
    public SectionBackCover(String id, String introduce, String isbn) {
        this.id = id;
        this.introduce = introduce;
        this.isbn = isbn;
    }
    @Generated(hash = 1083212588)
    public SectionBackCover() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIntroduce() {
        return this.introduce;
    }
    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public String getIsbn() {
        return this.isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}
