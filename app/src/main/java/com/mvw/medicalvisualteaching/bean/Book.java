package com.mvw.medicalvisualteaching.bean;

import android.text.TextUtils;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.utils.StringUtils;
import java.io.Serializable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 书籍 Created by  on 2016/12/5.
 */
@Entity(
    nameInDb = "Book"
)
public class Book implements Serializable,Comparable<Book>{

  @Transient
  public static final long serialVersionUID = 1L;
  @Property(
      nameInDb = "path"
  )
  private String path;//下载地址
  @Property(
      nameInDb = "downloadState"
  )
  private String downloadState;//下载状态
  @Property(
      nameInDb = "total"
  )
  private int total;//文件大小
  @Property(
      nameInDb = "downloaded"
  )
  private int downloaded;//已下载大小
  @Property(
      nameInDb = "downloadPath"
  )
  private String downloadPath;//文件路径
  @Property(
      nameInDb = "downloadFile"
  )
  private String downloadFile;//已下载文件名称
  @Property(nameInDb = "downloadPatchPath")
  private String downloadPatchPath;//更新包下载路径

  @Property(nameInDb = "textbookType")
  private String textbookType;//教材类型

  /*
  "packageName":"测试部",
    "packageId":"5624490d0cf212f38db2bd85",
    "coverBase":"",
    "id":"55ecf2d50cf22a8a8f26f2ed",
    "author":"",
    "categoryName":"基础课程",
    "cover":"https://mvw-imed3-mall.oss-cn-beijing.aliyuncs.com/upload/coverImages/9b55ade9-eca9-49b6-a8ec-55cb8c7495f3.jpg",
    "buyStatus":"2",
    "packageIsFree":"0",
    "isbn":"978-7-900574-14-5",
    "name":"医学细胞生物学",
    "path":"http://ebook.imed.org.cn/imed/bookDir/imed_V1.0.4/imed01.jc39_v1.0.4/imed01.jc39_v1.0.4.1_ios/13fba85edd43486d8f8686aced61c989.med.enc",
    "bookDeadline":"1506700800000",
    "createDate":"",
    "textbook":"0",
    "categoryId":"53d9bac8e4b0a8d0c8bb95c8",
    "s9id":"13fba85edd43486d8f8686aced61c989.med.enc",
    "bookSet":"1",
    "size":"281",
    "editor":"詹启敏 许正平",
    "isExpired":"0",
    "isFree":"2",
    "day":"129"
   */
  @Property(
      nameInDb = "isbn"
  )
  @Id
  @Index(unique = true)
  private String isbn;//书籍isbn
  @Property(nameInDb = "id")
  private String id;//书籍id
  @Property(nameInDb = "textbook")
  private String textbook;//0:教材  1:pdf
  @Property(nameInDb = "packageName")
  private String packageName;
  @Property(nameInDb = "categoryId")
  private String categoryId;
  @Property(nameInDb = "packageId")
  private String packageId;
  @Property(nameInDb = "type")
  private String type;//0:正常购买,1:免费书籍的下载(相当于购买),2:借阅
  @Property(nameInDb = "bookSet")
  private String bookSet;
  @Property(nameInDb = "categoryName")
  private String categoryName;
  @Property(nameInDb = "isExpired")
  private String isExpired;//是否已过期	0:未过期,1:已过期,2:已购买不过期
  @Property(nameInDb = "cover")
  private String cover;
  @Property(
      nameInDb = "coverBase"
  )
  private String coverBase;
  @Property(nameInDb = "buyStatus")
  private String buyStatus;//购买状态 0:未购买,1:已购买(免费书籍为已购买)
  @Property(nameInDb = "isFree")
  private String isFree;//是否免费 1:免费,2:收费,
  @Property(nameInDb = "packageIsFree")
  private String packageIsFree;
  @Property(nameInDb = "name")
  private String name;
  @Property(nameInDb = "day")
  private String day;//距离过期天数
  @Property(nameInDb = "bookDeadline")
  private String bookDeadline;//借阅到期时间
  @Property(nameInDb = "createDate")
  private String createDate;
  @Property(nameInDb = "size")
  private String size;
  @Property(nameInDb = "s9id")
  private String s9id;
  @Property(nameInDb = "author")
  private String author;
  @Property(nameInDb = "editor")
  private String editor;
  @Property(nameInDb = "patchVersion")
  private String patchVersion;
  @Property(nameInDb = "patchPath")
  private String patchPath;
  @Property(nameInDb = "sequence")
  private String sequence;
  @Property(nameInDb = "nonWifi")
  private String nonWifi = "0";
  @Property(nameInDb = "shelfStatus")
  private String shelfStatus;

  @Transient
  private int order;
  @Transient
  private String sectionId;
  @Transient
  private String isUpdate;//1:有更新  0或空：没有更新
@Generated(hash = 64089474)
public Book(String path, String downloadState, int total, int downloaded, String downloadPath, String downloadFile, String downloadPatchPath,
        String textbookType, String isbn, String id, String textbook, String packageName, String categoryId, String packageId, String type,
        String bookSet, String categoryName, String isExpired, String cover, String coverBase, String buyStatus, String isFree, String packageIsFree,
        String name, String day, String bookDeadline, String createDate, String size, String s9id, String author, String editor, String patchVersion,
        String patchPath, String sequence, String nonWifi, String shelfStatus) {
    this.path = path;
    this.downloadState = downloadState;
    this.total = total;
    this.downloaded = downloaded;
    this.downloadPath = downloadPath;
    this.downloadFile = downloadFile;
    this.downloadPatchPath = downloadPatchPath;
    this.textbookType = textbookType;
    this.isbn = isbn;
    this.id = id;
    this.textbook = textbook;
    this.packageName = packageName;
    this.categoryId = categoryId;
    this.packageId = packageId;
    this.type = type;
    this.bookSet = bookSet;
    this.categoryName = categoryName;
    this.isExpired = isExpired;
    this.cover = cover;
    this.coverBase = coverBase;
    this.buyStatus = buyStatus;
    this.isFree = isFree;
    this.packageIsFree = packageIsFree;
    this.name = name;
    this.day = day;
    this.bookDeadline = bookDeadline;
    this.createDate = createDate;
    this.size = size;
    this.s9id = s9id;
    this.author = author;
    this.editor = editor;
    this.patchVersion = patchVersion;
    this.patchPath = patchPath;
    this.sequence = sequence;
    this.nonWifi = nonWifi;
    this.shelfStatus = shelfStatus;
}
@Generated(hash = 1839243756)
public Book() {
}
public String getPath() {
    return this.path;
}
public void setPath(String path) {
    this.path = path;
}
public String getDownloadState() {
    return this.downloadState;
}
public void setDownloadState(String downloadState) {
    this.downloadState = downloadState;
}
public int getTotal() {
    return this.total;
}
public void setTotal(int total) {
    this.total = total;
}
public int getDownloaded() {
    return this.downloaded;
}
public void setDownloaded(int downloaded) {
    this.downloaded = downloaded;
}
public String getDownloadPath() {
    return this.downloadPath;
}
public void setDownloadPath(String downloadPath) {
    this.downloadPath = downloadPath;
}
public String getDownloadFile() {
    return this.downloadFile;
}
public void setDownloadFile(String downloadFile) {
    this.downloadFile = downloadFile;
}
public String getDownloadPatchPath() {
    return this.downloadPatchPath;
}
public void setDownloadPatchPath(String downloadPatchPath) {
    this.downloadPatchPath = downloadPatchPath;
}
public String getIsbn() {
    return this.isbn;
}
public void setIsbn(String isbn) {
    this.isbn = isbn;
}
public String getId() {
    return this.id;
}
public void setId(String id) {
    this.id = id;
}
public String getTextbook() {
    return this.textbook;
}
public void setTextbook(String textbook) {
    this.textbook = textbook;
}
public String getPackageName() {
    return this.packageName;
}
public void setPackageName(String packageName) {
    this.packageName = packageName;
}
public String getCategoryId() {
    return this.categoryId;
}
public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
}
public String getPackageId() {
    return this.packageId;
}
public void setPackageId(String packageId) {
    this.packageId = packageId;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getBookSet() {
    return this.bookSet;
}
public void setBookSet(String bookSet) {
    this.bookSet = bookSet;
}
public String getCategoryName() {
    return this.categoryName;
}
public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
}
public String getIsExpired() {
    return this.isExpired;
}
public void setIsExpired(String isExpired) {
    this.isExpired = isExpired;
}
public String getCover() {
    return this.cover;
}
public void setCover(String cover) {
    this.cover = cover;
}
public String getCoverBase() {
    return this.coverBase;
}
public void setCoverBase(String coverBase) {
    this.coverBase = coverBase;
}
public String getBuyStatus() {
    return this.buyStatus;
}
public void setBuyStatus(String buyStatus) {
    this.buyStatus = buyStatus;
}
public String getIsFree() {
    return this.isFree;
}
public void setIsFree(String isFree) {
    this.isFree = isFree;
}
public String getPackageIsFree() {
    return this.packageIsFree;
}
public void setPackageIsFree(String packageIsFree) {
    this.packageIsFree = packageIsFree;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getDay() {
    return this.day;
}
public void setDay(String day) {
    this.day = day;
}
public String getBookDeadline() {
    return this.bookDeadline;
}
public void setBookDeadline(String bookDeadline) {
    this.bookDeadline = bookDeadline;
}
public String getCreateDate() {
    return this.createDate;
}
public void setCreateDate(String createDate) {
    this.createDate = createDate;
}
public String getSize() {
    return this.size;
}
public void setSize(String size) {
    this.size = size;
}
public String getS9id() {
    return this.s9id;
}
public void setS9id(String s9id) {
    this.s9id = s9id;
}
public String getAuthor() {
    return this.author;
}
public void setAuthor(String author) {
    this.author = author;
}
public String getEditor() {
    return this.editor;
}
public void setEditor(String editor) {
    this.editor = editor;
}
public String getPatchVersion() {
    return this.patchVersion;
}
public void setPatchVersion(String patchVersion) {
    this.patchVersion = patchVersion;
}
public String getPatchPath() {
    return this.patchPath;
}
public void setPatchPath(String patchPath) {
    this.patchPath = patchPath;
}
public String getSequence() {
    return this.sequence;
}
public void setSequence(String sequence) {
    this.sequence = sequence;
}



  @Override
  public int compareTo(Book another) {
    String bs = another.getBuyStatus();
    int or = another.getOrder();
    int com = 0;
    if(!TextUtils.equals(Constant.STATUS_NOT_BUY,bs) && !TextUtils.equals(Constant.STATUS_NOT_BUY,buyStatus)){
      //购买状态相等，比较order大小
      if(order > or){
        com = -1;
      }else if(order < or){
        com = 1;
      }else if(order == 0 && or == 0){
        //order都为0的时候，比较sequence大小
        String anotherSequence = another.getSequence();
        if(!TextUtils.isEmpty(anotherSequence) &&  !TextUtils.isEmpty(sequence)){
          if(StringUtils.isNumeric(anotherSequence) && StringUtils.isNumeric(sequence)){
            int anotherSequenceNum = Integer.parseInt(anotherSequence);
            int sequenceNum = Integer.parseInt(sequence);
            if(sequenceNum > anotherSequenceNum){
              com = 1;
            }else if( sequenceNum < anotherSequenceNum){
              com = -1;
            }else {
              com = 0;
            }
          }
        }
      }else {
        com = 0;
      }
    }
    if(TextUtils.equals(Constant.STATUS_NOT_BUY,bs) && TextUtils.equals(Constant.STATUS_NOT_BUY,buyStatus)){
      //都没购买的时候，比较sequence大小
      String anotherSequence = another.getSequence();
      if(!TextUtils.isEmpty(anotherSequence) &&  !TextUtils.isEmpty(sequence)){
        if(StringUtils.isNumeric(anotherSequence) && StringUtils.isNumeric(sequence)){
          int anotherSequenceNum = Integer.parseInt(anotherSequence);
          int sequenceNum = Integer.parseInt(sequence);
          if(sequenceNum > anotherSequenceNum){
            com = 1;
          }else if( sequenceNum < anotherSequenceNum){
            com = -1;
          }else {
            com = 0;
          }
        }
      }
    }
    if(TextUtils.equals(Constant.STATUS_NOT_BUY,bs) && !TextUtils.equals(Constant.STATUS_NOT_BUY,buyStatus)){
      com =  -1;
    }
    if(!TextUtils.equals(Constant.STATUS_NOT_BUY,bs) && TextUtils.equals(Constant.STATUS_NOT_BUY,buyStatus)){
      com =  1;
    }
    return com;
  }


  public void setOrder(int order) {
    this.order = order;
  }

  public int getOrder(){
    return order;
  }

  public String getSectionId() {
    return sectionId;
  }

  public void setSectionId(String sectionId) {
    this.sectionId = sectionId;
  }

  public String getIsUpdate() {
    return isUpdate;
  }

  public void setIsUpdate(String isUpdate) {
    this.isUpdate = isUpdate;
  }
  public String getNonWifi() {
      return this.nonWifi;
  }
  public void setNonWifi(String nonWifi) {
      this.nonWifi = nonWifi;
  }
  public String getShelfStatus() {
      return this.shelfStatus;
  }
  public void setShelfStatus(String shelfStatus) {
      this.shelfStatus = shelfStatus;
  }
  public String getTextbookType() {
      return this.textbookType;
  }
  public void setTextbookType(String textbookType) {
      this.textbookType = textbookType;
  }

}
