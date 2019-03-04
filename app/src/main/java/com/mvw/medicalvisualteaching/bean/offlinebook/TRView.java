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
    nameInDb = "TRView",
    createInDb = false
)
public class TRView implements Serializable{
    @Transient
    public static final long serialVersionUID = 1L;
    /*
    "id":513,
    "tdViewList"
     */
    @Property(
        nameInDb = "id"
    )
    @Index(unique = true)
    private String id;
    @Property(nameInDb = "tableClass")
    private String tableClass;
    @Property(
        nameInDb = "tableView_id"
    )
    private String tableView_id;
    @Transient
    private List<TDView> tdViewList;
    @Generated(hash = 1577277055)
    public TRView(String id, String tableClass, String tableView_id) {
        this.id = id;
        this.tableClass = tableClass;
        this.tableView_id = tableView_id;
    }
    @Generated(hash = 1082582360)
    public TRView() {
    }
    public List<TDView> getTdViewList() {
        return tdViewList;
    }
    public void setTdViewList(
        List<TDView> tdViewList) {
        this.tdViewList = tdViewList;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTableClass() {
        return this.tableClass;
    }
    public void setTableClass(String tableClass) {
        this.tableClass = tableClass;
    }
    public String getTableView_id() {
        return this.tableView_id;
    }
    public void setTableView_id(String tableView_id) {
        this.tableView_id = tableView_id;
    }
}
