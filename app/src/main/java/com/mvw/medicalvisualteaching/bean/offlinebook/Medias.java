package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "Medias",
    createInDb = false
)
public class Medias implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    /*
    "id":100,
    "original":null,
    "mediaType":null,
    "abbreviation":"
     */
    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(
        nameInDb = "original"
    )
    private String original;
    @Property(
        nameInDb = "mediaType"
    )
    private String mediaType;
    @Property(
        nameInDb = "abbreviation"
    )
    private String abbreviation;
    @Property(
        nameInDb = "title"
    )
    private String title;
    @Property(
        nameInDb = "mediaBlock_id"
    )
    private String mediaBlock_id;
    @Property(
        nameInDb = "DTYPE"
    )
    private String dtype;

    @Transient
    private List<CombinationItem> combinationItems;
    @Transient
    private List<MediaVideo> mediaVideoItems;
    @Transient
    private List<MediaPPTItem> mediaPPTItems;


    @Generated(hash = 1990764548)
    public Medias(String id, String original, String mediaType, String abbreviation,
            String title, String mediaBlock_id, String dtype) {
        this.id = id;
        this.original = original;
        this.mediaType = mediaType;
        this.abbreviation = abbreviation;
        this.title = title;
        this.mediaBlock_id = mediaBlock_id;
        this.dtype = dtype;
    }

    @Generated(hash = 682330445)
    public Medias() {
    }


    public List<CombinationItem> getCombinationItems() {
        return combinationItems;
    }

    public void setCombinationItems(
        List<CombinationItem> combinationItems) {
        this.combinationItems = combinationItems;
    }

    public List<MediaVideo> getMediaVideoItems() {
        return mediaVideoItems;
    }

    public void setMediaVideoItems(
        List<MediaVideo> mediaVideoItems) {
        this.mediaVideoItems = mediaVideoItems;
    }

    public List<MediaPPTItem> getMediaPPTItems() {
        return mediaPPTItems;
    }

    public void setMediaPPTItems(
        List<MediaPPTItem> mediaPPTItems) {
        this.mediaPPTItems = mediaPPTItems;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal() {
        return this.original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaBlock_id() {
        return this.mediaBlock_id;
    }

    public void setMediaBlock_id(String mediaBlock_id) {
        this.mediaBlock_id = mediaBlock_id;
    }

    public String getDtype() {
        return this.dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }
}
