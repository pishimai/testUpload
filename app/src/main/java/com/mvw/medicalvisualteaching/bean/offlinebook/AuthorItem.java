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
    nameInDb = "AuthorItem",
    createInDb = false
)
public class AuthorItem implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    @Property(
        nameInDb = "id"
    )
    private String id;
    @Property(
        nameInDb = "f_order"
    )
    private String order;
    @Property(
        nameInDb = "name"
    )
    private String name ;
    @Property(
        nameInDb = "author_id"
    )
    private String author_id ;
    @Property(nameInDb = "parent_id")
    private String parent_id;
    @Transient
    private List<String> contents;
    @Transient
    private List<AuthorItem> children;

    @Generated(hash = 8454124)
    public AuthorItem(String id, String order, String name, String author_id,
            String parent_id) {
        this.id = id;
        this.order = order;
        this.name = name;
        this.author_id = author_id;
        this.parent_id = parent_id;
    }

    @Generated(hash = 502036404)
    public AuthorItem() {
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<AuthorItem> getChildren() {
        return children;
    }

    public void setChildren(
        List<AuthorItem> children) {
        this.children = children;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor_id() {
        return this.author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
