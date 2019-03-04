package com.mvw.medicalvisualteaching.bean.offlinebook;


import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 书籍
 * Created by  on 2016/12/5.
 */
@Entity(
    nameInDb = "Book",
    createInDb = false
)
public class LocalBook implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    /*
        "id":1,
        "name":"系统解剖学",
        "catalog":null,
        "isbn":null,
        "cover":null,
        "smallCover":null,
        "edition":null,
        "publishingOrgan":null,
        "publishingTime":null,
        "author":null,
        "editor":null,
        "subEditor":null,
        "umpire":null,
        "upTime":null,
        "enter":null,
        "readTime":null,
        "outTime":null,
        "totalCount":null,
        "abstracts":null,
        "version":null,
        "receiver":null,
        "platforms":null,
        "devices":null,
        "categories":null,
        "textbook":null,
        "shelvesStatusEnum":null,
        "subjects":null,
        "chapters":Array[26],
        "template":null,
        "bookDetail":null,
        "borrow":null

    [DTYPE]: varchar NOT NULL
        [id]: integer
        [abstracts]: clob
        [author]: varchar
        [catalog]: clob
        [cover]: clob
        [edition]: varchar
        [editor]: varchar
        [enter]: varchar
        [isBorrow]: integer
        [isbn]: varchar
        [name]: varchar
        [outTime]: timestamp
        [publishingOrgan]: varchar
        [publishingTime]: timestamp
        [readTime]: integer
        [smallCover]: clob
        [subEditor]: varchar
        [totalCount]: varchar
        [umpire]: varchar
        [upTime]: timestamp
        [version]: varchar


     */

    @Unique
    @Property(nameInDb = "id")
    private String id;//书籍id
    @Property(
        nameInDb = "name"
    )
    private String name;//书籍名称
    @Property(
        nameInDb = "isbn"
    )
    private String isbn;//图书编号
    @Property(
        nameInDb = "cover"
    )
    private String cover;//书籍大图
    @Property(
        nameInDb = "smallCover"
    )
    private String smallCover;//书籍小图
    @Property(
        nameInDb = "edition"
    )
    private String edition;//图书版本
    @Property(
        nameInDb = "publishingOrgan"
    )
    private String publishingOrgan;//出版机构
    @Property(
        nameInDb = "publishingTime"
    )
    private String publishingTime;//出版时间

    @Property(
        nameInDb = "editor"
    )
    private String editor;//主编
    @Property(
        nameInDb = "subEditor"
    )
    private String subEditor;//副主编
    @Property(
        nameInDb = "umpire"
    )
    private String umpire;//主审
    @Property(
        nameInDb = "totalCount"
    )
    private String totalCount;//字数
    @Property(
        nameInDb = "abstracts"
    )
    private String abstracts;//图书简介
    @Property(
        nameInDb = "DTYPE"
    )
    private String dtype;

//    @ToMany(referencedJoinProperty = "fk_id")
    @Transient
    private List<Chapter> chapters;


    @Generated(hash = 567179740)
    public LocalBook(String id, String name, String isbn, String cover,
            String smallCover, String edition, String publishingOrgan,
            String publishingTime, String editor, String subEditor, String umpire,
            String totalCount, String abstracts, String dtype) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.cover = cover;
        this.smallCover = smallCover;
        this.edition = edition;
        this.publishingOrgan = publishingOrgan;
        this.publishingTime = publishingTime;
        this.editor = editor;
        this.subEditor = subEditor;
        this.umpire = umpire;
        this.totalCount = totalCount;
        this.abstracts = abstracts;
        this.dtype = dtype;
    }

    @Generated(hash = 399939402)
    public LocalBook() {
    }


    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSmallCover() {
        return this.smallCover;
    }

    public void setSmallCover(String smallCover) {
        this.smallCover = smallCover;
    }

    public String getEdition() {
        return this.edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getPublishingOrgan() {
        return this.publishingOrgan;
    }

    public void setPublishingOrgan(String publishingOrgan) {
        this.publishingOrgan = publishingOrgan;
    }

    public String getPublishingTime() {
        return this.publishingTime;
    }

    public void setPublishingTime(String publishingTime) {
        this.publishingTime = publishingTime;
    }

    public String getEditor() {
        return this.editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSubEditor() {
        return this.subEditor;
    }

    public void setSubEditor(String subEditor) {
        this.subEditor = subEditor;
    }

    public String getUmpire() {
        return this.umpire;
    }

    public void setUmpire(String umpire) {
        this.umpire = umpire;
    }

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getAbstracts() {
        return this.abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getDtype() {
        return this.dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
}
