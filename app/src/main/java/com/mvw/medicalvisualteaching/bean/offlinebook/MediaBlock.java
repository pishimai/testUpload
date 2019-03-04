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
    nameInDb = "MediaBlock",
    createInDb = false
)
public class MediaBlock implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    /*
    "id":83,
    "name":"头颈肌侧面观（A）",
    "order":"图3-2-1",
    "pictures":
     */
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "f_order"
    )
    private String order;
    @Transient
    private List<Medias> pictures;
    @Property(
        nameInDb = "paragraph_id"
    )
    private String paragraph_id;


    @Generated(hash = 1035475726)
    public MediaBlock(String id, String name, String order, String paragraph_id) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.paragraph_id = paragraph_id;
    }

    @Generated(hash = 1045742392)
    public MediaBlock() {
    }


    public List<Medias> getPictures() {
        return pictures;
    }

    public void setPictures(List<Medias> pictures) {
        this.pictures = pictures;
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

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getParagraph_id() {
        return this.paragraph_id;
    }

    public void setParagraph_id(String paragraph_id) {
        this.paragraph_id = paragraph_id;
    }
}
