package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 书籍段落
 * Created by  on 2016/12/5.
 */
@Entity(
    nameInDb = "Paragraph",
    createInDb = false
)
public class Paragraph implements Serializable{
    @Transient
    public static final long serialVersionUID = 1l;
    /*
            "id":593,
            "title":null,
            "paragraphId":null,
            "text":null,
            "order":null,
            "typeEnum":null,
            "extend":"BASIC",
            "layout":null,
            "mediaBlocks":[

            ],
            "keyWord":[

            ],
            "tableViews":[

            ],
            "rootId":null,
            "sectionExtend":{

            }
     */

    @Property(
        nameInDb = "id"
    )
    @Unique
    private String id;
    @Property(
        nameInDb = "title"
    )
    private String title;
    @Property(
        nameInDb = "paragraphId"
    )
    private String paragraphId;
    @Property(
        nameInDb = "text"
    )
    private String text;
    @Property(
        nameInDb = "f_order"
    )
    private String order;
    @Property(
        nameInDb = "typeEnum"
    )
    private String typeEnum;

    @Property(
        nameInDb = "layout"
    )
    private String layout;
    @Property(
        nameInDb = "parent_id"
    )
    @Index
    private String parent_id;
    @Transient
    private List<MediaBlock> mediaBlocks;
    @Transient
    private List<KeyWord> keyWord;
    @Transient
    private List<TableView> tableViews;
    @Transient
    private String rootId;
    @Transient
    private Map<String,String> sectionExtend;
    @Transient
    private List<Paragraph> section;
    @Transient
    private List<String> titles;
    @Transient
    private List<Map<String,Object>> authors;

    @Generated(hash = 1496625585)
    public Paragraph(String id, String title, String paragraphId, String text,
            String order, String typeEnum, String layout, String parent_id) {
        this.id = id;
        this.title = title;
        this.paragraphId = paragraphId;
        this.text = text;
        this.order = order;
        this.typeEnum = typeEnum;
        this.layout = layout;
        this.parent_id = parent_id;
    }

    @Generated(hash = 1886769140)
    public Paragraph() {
    }

    public List<Paragraph> getSection() {
        return section;
    }

    public void setSection(List<Paragraph> section) {
        this.section = section;
    }



    public List<KeyWord> getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(List<KeyWord> keyWord) {
        this.keyWord = keyWord;
    }

    public List<MediaBlock> getMediaBlocks() {
        return mediaBlocks;
    }

    public void setMediaBlocks(List<MediaBlock> mediaBlocks) {
        this.mediaBlocks = mediaBlocks;
    }


    public Map<String, String> getSectionExtend() {
        return sectionExtend;
    }

    public void setSectionExtend(Map<String, String> sectionExtend) {
        this.sectionExtend = sectionExtend;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParagraphId() {
        return this.paragraphId;
    }

    public void setParagraphId(String paragraphId) {
        this.paragraphId = paragraphId;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTypeEnum() {
        return this.typeEnum;
    }

    public void setTypeEnum(String typeEnum) {
        this.typeEnum = typeEnum;
    }

    public String getLayout() {
        return this.layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public List<TableView> getTableViews() {
        return tableViews;
    }

    public void setTableViews(
        List<TableView> tableViews) {
        this.tableViews = tableViews;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<Map<String, Object>> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Map<String, Object>> authors) {
        this.authors = authors;
    }
}
