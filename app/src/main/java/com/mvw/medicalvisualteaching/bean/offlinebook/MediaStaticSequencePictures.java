package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "MediaStaticSequence_pictures",
    createInDb = false
)
public class MediaStaticSequencePictures implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;


    @Property(
        nameInDb = "MediaStaticSequence_id"
    )
    private String MediaStaticSequence_id;
    @Property(
        nameInDb = "pictures"
    )
    private String pictures;
    @Generated(hash = 1613381968)
    public MediaStaticSequencePictures(String MediaStaticSequence_id,
            String pictures) {
        this.MediaStaticSequence_id = MediaStaticSequence_id;
        this.pictures = pictures;
    }
    @Generated(hash = 1310702012)
    public MediaStaticSequencePictures() {
    }
    public String getMediaStaticSequence_id() {
        return this.MediaStaticSequence_id;
    }
    public void setMediaStaticSequence_id(String MediaStaticSequence_id) {
        this.MediaStaticSequence_id = MediaStaticSequence_id;
    }
    public String getPictures() {
        return this.pictures;
    }
    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

}
