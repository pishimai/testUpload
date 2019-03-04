package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "MediaSequence_pictures",
    createInDb = false
)
public class MediaSequencePictures implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;

    @Property(
        nameInDb = "MediaSequence_id"
    )
    private String MediaSequence_id;
    @Property(
        nameInDb = "pictures"
    )
    private String pictures;
    @Generated(hash = 869328403)
    public MediaSequencePictures(String MediaSequence_id, String pictures) {
        this.MediaSequence_id = MediaSequence_id;
        this.pictures = pictures;
    }
    @Generated(hash = 2031593116)
    public MediaSequencePictures() {
    }
    public String getMediaSequence_id() {
        return this.MediaSequence_id;
    }
    public void setMediaSequence_id(String MediaSequence_id) {
        this.MediaSequence_id = MediaSequence_id;
    }
    public String getPictures() {
        return this.pictures;
    }
    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

}
