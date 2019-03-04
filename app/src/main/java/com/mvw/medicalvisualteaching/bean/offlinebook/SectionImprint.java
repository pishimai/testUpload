package com.mvw.medicalvisualteaching.bean.offlinebook;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 */
@Entity(
    nameInDb = "Section_Imprint",
    createInDb = false
)
public class SectionImprint implements Serializable{

    @Transient
    public static final long serialVersionUID = 1l;
    @Unique
    private String id;
    @Property(
        nameInDb = "count"
    )
    private String count;
    @Property(
        nameInDb = "distributing"
    )
    private String distributing;
    @Property(
        nameInDb = "distributingAddress"
    )
    private String distributingAddress;
    @Property(
        nameInDb = "distributingDate"
    )
    private String distributingDate;
    @Property(
        nameInDb = "distributingPostcode"
    )
    private String distributingPostcode;
    @Property(
        nameInDb = "email"
    )
    private String email;
    @Property(
        nameInDb = "hotline"
    )
    private String hotline;
    @Property(
        nameInDb = "isbn"
    )
    private String isbn;
    @Property(
        nameInDb = "operator"
    )
    private String operator;
    @Property(
        nameInDb = "phone"
    )
    private String phone;
    @Property(
        nameInDb = "price"
    )
    private String price;
    @Property(
        nameInDb = "producer"
    )
    private String producer;
    @Property(
        nameInDb = "producerAddress"
    )
    private String producerAddress;
    @Property(
        nameInDb = "publish"
    )
    private String publish;
    @Property(
        nameInDb = "publishAddress"
    )
    private String publishAddress;
    @Property(
        nameInDb = "publishPostcode"
    )
    private String publishPostcode;
    @Property(
        nameInDb = "subject"
    )
    private String subjectName;
    @Property(
        nameInDb = "version"
    )
    private String version;
    @Transient
    private List<String> editors;



    @Generated(hash = 1039512442)
    public SectionImprint(String id, String count, String distributing,
            String distributingAddress, String distributingDate,
            String distributingPostcode, String email, String hotline, String isbn,
            String operator, String phone, String price, String producer,
            String producerAddress, String publish, String publishAddress,
            String publishPostcode, String subjectName, String version) {
        this.id = id;
        this.count = count;
        this.distributing = distributing;
        this.distributingAddress = distributingAddress;
        this.distributingDate = distributingDate;
        this.distributingPostcode = distributingPostcode;
        this.email = email;
        this.hotline = hotline;
        this.isbn = isbn;
        this.operator = operator;
        this.phone = phone;
        this.price = price;
        this.producer = producer;
        this.producerAddress = producerAddress;
        this.publish = publish;
        this.publishAddress = publishAddress;
        this.publishPostcode = publishPostcode;
        this.subjectName = subjectName;
        this.version = version;
    }

    @Generated(hash = 13304429)
    public SectionImprint() {
    }



    public List<String> getEditors() {
        return editors;
    }

    public void setEditors(List<String> editors) {
        this.editors = editors;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDistributing() {
        return this.distributing;
    }

    public void setDistributing(String distributing) {
        this.distributing = distributing;
    }

    public String getDistributingAddress() {
        return this.distributingAddress;
    }

    public void setDistributingAddress(String distributingAddress) {
        this.distributingAddress = distributingAddress;
    }

    public String getDistributingDate() {
        return this.distributingDate;
    }

    public void setDistributingDate(String distributingDate) {
        this.distributingDate = distributingDate;
    }

    public String getDistributingPostcode() {
        return this.distributingPostcode;
    }

    public void setDistributingPostcode(String distributingPostcode) {
        this.distributingPostcode = distributingPostcode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHotline() {
        return this.hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProducer() {
        return this.producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducerAddress() {
        return this.producerAddress;
    }

    public void setProducerAddress(String producerAddress) {
        this.producerAddress = producerAddress;
    }

    public String getPublish() {
        return this.publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getPublishAddress() {
        return this.publishAddress;
    }

    public void setPublishAddress(String publishAddress) {
        this.publishAddress = publishAddress;
    }

    public String getPublishPostcode() {
        return this.publishPostcode;
    }

    public void setPublishPostcode(String publishPostcode) {
        this.publishPostcode = publishPostcode;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
