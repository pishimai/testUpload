package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 书籍节
 * Created by  on 2016/12/5.
 */
@Entity(
    nameInDb = "Section",
    createInDb = false
)
public class Section implements Serializable{
    /*

        "id":1,
        "sectionId":null,
        "name":"要点",
        "order":null,
        "template":null,
        "topImage":null,
        "children":null,
        "section":null

       [DTYPE]: varchar NOT NULL
        [id]: integer
        [extend]: varchar
        [name]: varchar
        [f_order]: integer
        [sectionId]: varchar
        [template]: varchar
        [topImage]: clob
        [chapter_id]: bigint
        [parent_id]: bigint


        */
    @Transient
    public static final long serialVersionUID = 1l;

    @Index(unique = true)
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "sectionId"
    )
    private String sectionId;
    @Property(
        nameInDb = "name"
    )
    private String name;
    @Property(
        nameInDb = "f_order"
    )
    private String order;
    @Property(
        nameInDb = "template"
    )
    private String template;
    @Property(
        nameInDb = "topImage"
    )
    private String topImage;
//    @ToMany
//    @JoinEntity(
//        entity = SectionDetailParagraph.class,
//        sourceProperty = "sectionDetail_id",
//        targetProperty = "paragraphs_id"
//    )
    @Transient
    private List<Paragraph> section;
    //============  数据库
    @Property(
        nameInDb = "chapter_id"
    )
    private String chapterId;
    @Property
    private String extend;

    @Property(
        nameInDb = "DTYPE"
    )
    private String dtype;
    @Transient
    private Map<String,List<SectionTranslate>> contents;
    @Generated(hash = 1592316548)
    public Section(String id, String sectionId, String name, String order,
            String template, String topImage, String chapterId, String extend,
            String dtype) {
        this.id = id;
        this.sectionId = sectionId;
        this.name = name;
        this.order = order;
        this.template = template;
        this.topImage = topImage;
        this.chapterId = chapterId;
        this.extend = extend;
        this.dtype = dtype;
    }
    @Generated(hash = 111791983)
    public Section() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSectionId() {
        return this.sectionId;
    }
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
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
    public String getTemplate() {
        return this.template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public String getTopImage() {
        return this.topImage;
    }
    public void setTopImage(String topImage) {
        this.topImage = topImage;
    }
    public String getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
    public String getExtend() {
        return this.extend;
    }
    public void setExtend(String extend) {
        this.extend = extend;
    }
    public String getDtype() {
        return this.dtype;
    }
    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public Map<String, List<SectionTranslate>> getContents() {
        return contents;
    }

    public void setContents(
        Map<String, List<SectionTranslate>> contents) {
        this.contents = contents;
    }

    public List<Paragraph> getSection() {
        return section;
    }

    public void setSection(List<Paragraph> section) {
        this.section = section;
    }
}
