package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "TDView",
    createInDb = false
)
public class TDView implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    /*
    "id":11602,
    "colSpan":null,
    "rowSpan":null,
    "text":"肌群",
    "type":"text"
     */
    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(
        nameInDb = "colSpan"
    )
    private String colSpan;
    @Property(
        nameInDb = "rowSpan"
    )
    private String rowSpan;
    @Property(
        nameInDb = "tdCss"
    )
    private String tdCss;
    @Property(
        nameInDb = "text"
    )
    private String text;
    @Property(
        nameInDb = "type"
    )
    private String type;
    @Property(
        nameInDb = "trView_id"
    )
    private String trView_id;
    @Generated(hash = 1786198857)
    public TDView(String id, String colSpan, String rowSpan, String tdCss,
            String text, String type, String trView_id) {
        this.id = id;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
        this.tdCss = tdCss;
        this.text = text;
        this.type = type;
        this.trView_id = trView_id;
    }
    @Generated(hash = 1312743213)
    public TDView() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getColSpan() {
        return this.colSpan;
    }
    public void setColSpan(String colSpan) {
        this.colSpan = colSpan;
    }
    public String getRowSpan() {
        return this.rowSpan;
    }
    public void setRowSpan(String rowSpan) {
        this.rowSpan = rowSpan;
    }
    public String getTdCss() {
        return this.tdCss;
    }
    public void setTdCss(String tdCss) {
        this.tdCss = tdCss;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTrView_id() {
        return this.trView_id;
    }
    public void setTrView_id(String trView_id) {
        this.trView_id = trView_id;
    }

}
