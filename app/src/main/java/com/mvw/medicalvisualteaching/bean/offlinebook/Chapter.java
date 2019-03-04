package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;


/**
 * 书籍章
 * Created by  on 2016/12/5.
 */
@Entity(
    nameInDb = "Chapter",
    createInDb = false
)
public class Chapter implements Serializable{
    /*

             "id":10,
                "sections":Array[11],
                "chapterId":null,
                "name":"封面",
                "order":null,
                "logo":null,
                "pieceId":null,
                "piece":null,
                "icon":null

       [id]: integer
        [chapterId]: varchar
        [logo]: clob
        [name]: varchar
        [f_order]: integer
        [piece]: varchar
        [pieceId]: varchar
        [bookHTML_id]: bigint


        */
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    @Property( nameInDb = "id")
    @Id
    private String id;
    @Property(
        nameInDb = "chapterId"
    )
    private String chapterId;
    @Property(
        nameInDb = "icon"
    )
    private String icon;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "f_order"
    )
    private int order;
    @Property(
        nameInDb = "piece"
    )
    private String piece;
    @Property(
        nameInDb = "pieceId"
    )
    private String pieceId;
    @Property(
        nameInDb = "bookHTML_id"
    )
    private int bookHTML_id;
    @Transient
    private List<Map<String,Object>> sections;

    @Generated(hash = 474964910)
    public Chapter(String id, String chapterId, String icon, String name, int order,
            String piece, String pieceId, int bookHTML_id) {
        this.id = id;
        this.chapterId = chapterId;
        this.icon = icon;
        this.name = name;
        this.order = order;
        this.piece = piece;
        this.pieceId = pieceId;
        this.bookHTML_id = bookHTML_id;
    }

    @Generated(hash = 393170288)
    public Chapter() {
    }

    public List<Map<String, Object>> getSections() {
        return sections;
    }

    public void setSections(List<Map<String, Object>> sections) {
        this.sections = sections;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapterId() {
        return this.chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPiece() {
        return this.piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getPieceId() {
        return this.pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

    public int getBookHTML_id() {
        return this.bookHTML_id;
    }

    public void setBookHTML_id(int bookHTML_id) {
        this.bookHTML_id = bookHTML_id;
    }
}
