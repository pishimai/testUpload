package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "Section_MainPoints",
    createInDb = false
)
public class SectionMainPoints implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    private String id;
    @Property(
        nameInDb = "chapterId"
    )
    private String chapterId;

    @Property(
        nameInDb = "chapterName"
    )
    private String chapterName;

    @Property(
        nameInDb = "piece"
    )
    private String piece;

    @Property(
        nameInDb = "pieceId"
    )
    private String pieceId;

    @Generated(hash = 55303672)
    public SectionMainPoints(String id, String chapterId, String chapterName,
            String piece, String pieceId) {
        this.id = id;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.piece = piece;
        this.pieceId = pieceId;
    }

    @Generated(hash = 694326172)
    public SectionMainPoints() {
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

    public String getChapterName() {
        return this.chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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

}
