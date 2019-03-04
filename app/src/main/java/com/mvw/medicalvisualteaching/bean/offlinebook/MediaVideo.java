package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "MediaVideoItem",
    createInDb = false
)
public class MediaVideo implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    /*
   "id":151,
    "name":null,
    "abbreviation":"http://oss-cn-beijing.aliyuncs.com/mvw-imed3/mvw_imed_book/imed_27/picture/270103c01z.jpg",
    "path":"http://oss-cn-beijing.aliyuncs.com/mvw-imed3/mvw_imed_book/imed_27/video/270103c01z.mp4"     */
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "name"
    )
    private String name="";
    @Property(
        nameInDb = "abbreviation"
    )
    private String abbreviation ;
    @Property(
        nameInDb = "path"
    )
    private String path ;
    @Property(
        nameInDb = "videoCombination_id"
    )
    private String videoCombination_id;
    @Generated(hash = 816250416)
    public MediaVideo(String id, String name, String abbreviation, String path, String videoCombination_id) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.path = path;
        this.videoCombination_id = videoCombination_id;
    }
    @Generated(hash = 1550173169)
    public MediaVideo() {
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
    public String getAbbreviation() {
        return this.abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getVideoCombination_id() {
        return this.videoCombination_id;
    }
    public void setVideoCombination_id(String videoCombination_id) {
        this.videoCombination_id = videoCombination_id;
    }
}
