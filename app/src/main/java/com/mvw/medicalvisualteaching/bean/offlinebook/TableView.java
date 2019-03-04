package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 */
@Entity(
    nameInDb = "TableView",
    createInDb = false
)
public class TableView implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    /*
    "id":513,
    "rowNum":15,
    "colNum":6,
    "tableIndex":"表3-2-1",
    "tableName":"头肌的起止点、作用和神经支配",
    "trViews"
     */
    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(
        nameInDb = "rowNum"
    )
    private String rowNum;
    @Property(
        nameInDb = "colNum"
    )
    private String colNum;
    @Property(
        nameInDb = "tableIndex"
    )
    private String tableIndex;
    @Property(
        nameInDb = "tableName"
    )
    private String tableName;
    @Property(
        nameInDb = "paragraph_id"
    )
    private String paragraph_id;
    @Transient
    private List<TRView> trViews;
    @Generated(hash = 1572338653)
    public TableView(String id, String rowNum, String colNum, String tableIndex,
            String tableName, String paragraph_id) {
        this.id = id;
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.tableIndex = tableIndex;
        this.tableName = tableName;
        this.paragraph_id = paragraph_id;
    }
    @Generated(hash = 1165582941)
    public TableView() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRowNum() {
        return this.rowNum;
    }
    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }
    public String getColNum() {
        return this.colNum;
    }
    public void setColNum(String colNum) {
        this.colNum = colNum;
    }
    public String getTableIndex() {
        return this.tableIndex;
    }
    public void setTableIndex(String tableIndex) {
        this.tableIndex = tableIndex;
    }
    public String getTableName() {
        return this.tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getParagraph_id() {
        return this.paragraph_id;
    }
    public void setParagraph_id(String paragraph_id) {
        this.paragraph_id = paragraph_id;
    }

    public List<TRView> getTrViews() {
        return trViews;
    }

    public void setTrViews(List<TRView> trViews) {
        this.trViews = trViews;
    }
}
