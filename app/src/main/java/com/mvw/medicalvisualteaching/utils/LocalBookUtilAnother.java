package com.mvw.medicalvisualteaching.utils;

import static com.mvw.medicalvisualteaching.utils.DataUtil.sendServiceResult;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mvw.medicalvisualteaching.application.MyApplication;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.Result;
import com.mvw.medicalvisualteaching.bean.UserBook;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.UserBookDao;
import com.mvw.medicalvisualteaching.utils.LocalBookUtil.BookName;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.json.JSONException;

/**
 *
 * Created by zhaomengen on 2017/4/18.
 */

public class LocalBookUtilAnother {
  private static String isBn = ""; //获取图书的isbn 唯一标识

  /**
   * 读取本地书籍数据库里的章
   * @param localBook 书籍
   * @param context context
   * @param handler handler
   * @param sn sn
   * @throws JSONException
   */
  public static void getLocalBookCatelog(Book localBook, Context context,Handler handler, String sn)
      throws Exception {
    isBn = localBook.getIsbn();
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+ Utils
        .getBookVideoPath(isBn)+File.separator+Constant.LOCAL_BOOK_DB_NAME;
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    File file = new File(path);
    if(!file.exists()){
      //库文件不存在
      sendServiceResult(true,false, Constant.GET_BOOK_CATELOG,"这本书已经丢失或损坏，请重新安装",null,handler,sn);
      return;
    }
    /*
    select * from Book  left join Chapter on Book.id= Chapter.bookHTML_id left join Section on Chapter.id=Section.chapter_id
     */
    GreenDaoHelper.initLocalBookDatabasePassword(null,path,"");
    //获取书籍内容 Book表
    Database database = GreenDaoHelper.getDaoSessionBook().getDatabase();
    Cursor bookCursor = database.rawQuery("select * from Book;",null);
    Map<String,Object>  bookResult = getCursorMap(bookCursor);
    //获取书籍下的章列表  Chapter表
    String bookId = (String) bookResult.get("id");
    String chapterSql = "select * from Chapter where bookHTML_id="+bookId+" order by f_order asc;";
    Cursor chapterCursor = database.rawQuery(chapterSql,null);
    List<Map<String,Object>> chapters = getCursorMapList(chapterCursor);
    chapterCursor.close();
    //获取章下面的节列表  Section表
    for(Map<String,Object> chapter : chapters){
      //每个章的图标，需要添加上本地的存储路径 basePath
      String icon = (String) chapter.get("icon");
      chapter.put("icon",basePath + icon);
      //根据章的id获取对应的节Section
      String chapterId = (String) chapter.get("id");
      String sectionSql = "select * from Section where chapter_id='"+chapterId+"' order by f_order asc;";
      Cursor sectionCursor = database.rawQuery(sectionSql,null);
      List<Map<String,Object>> sections = getCursorMapList(sectionCursor);
      chapter.put("sections",sections);
    }
    bookResult.put("chapters",chapters);
    //查询用户的SectionId 为了url不拼接sid，缓存需要
    UserBookDao userBookDao = GreenDaoHelper.getDaoSession().getUserBookDao();
    UserBook userBook = userBookDao.queryBuilder().where(UserBookDao.Properties.Id.eq(MyApplication
        .getUser().getId()+"+"+isBn)).unique();
    String sid = "";
    if (userBook!=null && !TextUtils.isEmpty(userBook.getSectionId())){
      sid = userBook.getSectionId();
    }
    bookResult.put("sectionId",sid);
    sendLocalBookServiceResult(Constant.GET_BOOK_CATELOG_LOCAL,bookResult,handler,sn);
  }

  /**
   * 读取本地书籍数据库里的节
   * @param localBook 书籍
   * @param context context
   * @param sectionId 节的id
   * @param handler handler
   * @param sn sn
   * @throws JSONException
   */
  public static void getLocalBookChapter(final Book localBook , final Context context, final String sectionId,
      final Handler handler, final String sn)
      throws JSONException, UnsupportedEncodingException {
    isBn = localBook.getIsbn();
    new Thread(){
      @Override
      public void run() {
        try {
          long start = System.currentTimeMillis();
          getParagraphResult(localBook,context,sectionId,handler,sn);
          Logger.i("节   "+(System.currentTimeMillis() - start));
        } catch (JSONException | UnsupportedEncodingException | JsonProcessingException e) {
          e.printStackTrace();
        }
      }
    }.start();
  }

  /**
   * 获取节下面的内容
   * @param localBook 本地书籍
   * @param context context
   * @param sectionId 节的id
   * @param handler handler
   * @param sn sn
   * @throws JSONException
   * @throws UnsupportedEncodingException
   * @throws JsonProcessingException
   */
  public static void getParagraphResult(Book localBook ,Context context,String sectionId,Handler handler,String sn)
      throws JSONException, UnsupportedEncodingException, JsonProcessingException {
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+Utils.getBookVideoPath(localBook.getIsbn())+File.separator+Constant.LOCAL_BOOK_DB_NAME;
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    File file = new File(path);
    if(!file.exists()){
      //库文件不存在
      sendServiceResult(true,false, Constant.GET_BOOK_CATELOG,"这本书已经丢失或损坏，请重新安装",null,handler,sn);
      return;
    }
    GreenDaoHelper.initLocalBookDatabasePassword(null,path,"");
    Database database = GreenDaoHelper.getDaoSessionBook().getDatabase();
    //获取节的数据 Section
    String sqlSection = "select * from Section where id="+sectionId+";";
    Cursor sectionCursor = database.rawQuery(sqlSection,null);
    Map<String,Object> section = getCursorMap(sectionCursor);
    //获取节下面的段落内容 Section_Paragraph , Paragraph
    List<Map<String,Object>> paragraphs = getParagraphContentList(section,database,basePath);
    section.put("section",paragraphs);
    //每节顶部的图片，需要添加本地的路径 basePath
    String topImage = (String) section.get("topImage");
    if (!TextUtils.isEmpty(topImage)){
      topImage = basePath+topImage;
    }
    section.put("topImage",topImage);
    //Section表的子类表，继承关系,  节有特殊类型的节：要点，封面等等
    section = getSubSection(section,database,basePath);
    sendLocalBookServiceResult(Constant.GET_BOOK_CHAPTER_LOCAL,section,handler,sn);
  }

  /**
   * 获取作者简介
   */
  public static void getAuthorInfo(Book localBook,Context context,String authorId,Handler handler,String sn){
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+Utils.getBookVideoPath(localBook.getIsbn())+File.separator+Constant.LOCAL_BOOK_DB_NAME;
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    Database database = GreenDaoHelper.getDaoSessionBook().getDatabase();
    //作者
    String authorSql = "select * from Author where id="+authorId;
    Cursor authorCursor = database.rawQuery(authorSql,null);
    Map<String,Object> author = getCursorMap(authorCursor);
    String photo = (String) author.get("photo");
    if (!TextUtils.isEmpty(photo)){
      photo = basePath+photo;
    }
    author.put("photo",photo);

    //作者头衔
    String aId = (String) author.get("id");
    String authorTitleSql = "select * from Author_title where Author_id="+aId;
    Cursor authorTitleCursor = database.rawQuery(authorTitleSql,null);
    List<Map<String,Object>> authorTitles = getCursorMapList(authorTitleCursor);

    List<String> titles = new ArrayList<>();
    for(Map<String,Object> authorTitle : authorTitles){
      String title = (String) authorTitle.get("title");
      titles.add(title);
    }
    author.put("title",titles);
    //作者介绍标题
    String authorItemSql = "select * from AuthorItem where author_id="+aId;
    Cursor authorItemCursor = database.rawQuery(authorItemSql,null);
    List<Map<String,Object>> authorItems = getCursorMapList(authorItemCursor);
    for (Map<String,Object> authorItem : authorItems){
      String authorItemId = (String) authorItem.get("id");
      String authorContentSql = "select * from AuthorItem_contents where AuthorItem_id="+authorItemId;
      Cursor authorContentCursor = database.rawQuery(authorContentSql,null);
      List<Map<String,Object>> contentsList = getCursorMapList(authorContentCursor);
      List<String> contents = new ArrayList<>();
      for(Map<String,Object> authorItemContents : contentsList){
        String content = (String) authorItemContents.get("contents");
        contents.add(content);
      }
      authorItem.put("contents",contents);
      //子简介内容
      List<Map<String,Object>> children = getAuthorChildren(authorItemId,database);
      authorItem.put("children",children);
    }
    author.put("authorItems",authorItems);
    sendLocalBookServiceResult(Constant.GET_AUTHOR_PROFILE_LOCAL,author,handler,sn);
  }

  private static List<Map<String,Object>> getAuthorChildren(String parentId,Database database){
    String authorItemSql = "select * from AuthorItem where parent_id="+parentId;
    Cursor authorItemCursor = database.rawQuery(authorItemSql,null);
    List<Map<String,Object>> authorItems = getCursorMapList(authorItemCursor);
    for (Map<String,Object> authorItem : authorItems){
      String authorItemId = (String) authorItem.get("id");
      String authorContentSql = "select * from AuthorItem_contents where AuthorItem_id="+authorItemId;
      Cursor authorContentCursor = database.rawQuery(authorContentSql,null);
      List<Map<String,Object>> contentsList = getCursorMapList(authorContentCursor);
      List<String> contents = new ArrayList<>();
      for(Map<String,Object> authorItemContents : contentsList){
        String content = (String) authorItemContents.get("contents");
        contents.add(content);
      }
      authorItem.put("contents",contents);
      //子简介内容
      String pId = (String) authorItem.get("parent_id");
      if(pId != null && !TextUtils.equals("",pId)){
        List<Map<String,Object>> children = getAuthorChildren(authorItemId,database);
        authorItem.put("children",children);
      }
    }
    return authorItems;
  }

  /**
   *  获取特效数据
   */
  public static void getBookMedia(Book localBook,Context context,String mediaId,Handler handler,String sn)
      throws UnsupportedEncodingException {
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+Utils.getBookVideoPath(localBook.getIsbn())+File.separator+Constant.LOCAL_BOOK_DB_NAME;
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    Database database = GreenDaoHelper.getDaoSessionBook().getDatabase();
    String sql = "select * from Medias where mediaBlock_id="+mediaId;
    Cursor cursor = database.rawQuery(sql,null);
    List<Map<String,Object>> mediasList = getCursorMapList(cursor);

    String sql2 = "select * from MediaBlock where id="+mediaId;
    Cursor cursor2 = database.rawQuery(sql2,null);
    Map<String,Object> baseMap = getCursorMap(cursor2);
    String order = (String)baseMap.get("f_order");
    baseMap.remove("f_order");
    baseMap.put("order",order);
    Map<String,Object> mediaMap = new ArrayMap<>();
    mediaMap.putAll(baseMap);
    for (Map<String,Object> medias:mediasList) {
      String original = (String) medias.get("original");
      if (!TextUtils.isEmpty(original)){
        original = basePath+original;
      }
      medias.put("original",original);
      String abb = (String) medias.get("abbreviation");
      if (!TextUtils.isEmpty(abb)){
        abb = basePath+abb;
      }
      medias.put("abbreviation",abb);
      String mediaType = (String) medias.get("mediaType");
      String id = (String) medias.get("id");
      switch (mediaType){
        case "COMBINATION"://叠加图片
          String combinationItemSql = "select * from CombinationItem where mediaCombination_id="+id;
          Cursor combinationCursor = database.rawQuery(combinationItemSql,null);
          List<Map<String,Object>> combinationItems = getCursorMapList(combinationCursor);
          for(Map<String,Object> item : combinationItems){
            String picture = (String) item.get("picture");
            if (!TextUtils.isEmpty(picture)){
              picture = basePath+picture;
            }
            item.put("picture",picture);
          }
          medias.put("combinationItems",combinationItems);
          break;
        case "VIDEO"://视频
          String mediaVideoSql = "select * from MediaVideoItem where videoCombination_id="+id;
          Cursor mediaVideoCursor = database.rawQuery(mediaVideoSql,null);
          List<Map<String,Object>> mediaVideo = getCursorMapList(mediaVideoCursor);
          for(Map<String,Object> video : mediaVideo){
            String videoPath = (String) video.get("path");
            String name = (String) video.get("name");
            video.put("path",AppConfig.BOOK_ONLINE_READ_URL+videoPath);
            String abbreviation = (String)video.get("abbreviation");
            if (!TextUtils.isEmpty(abbreviation)){
              abbreviation = basePath+abbreviation;
              video.put("abbreviation",abbreviation);
            }else{
              video.put("abbreviation","");
            }

            if(name == null){
              video.put("name","");
            }
          }
          medias.put("mediaVideoItems",mediaVideo);
          break;
        case "SEQUENCE"://图像序列
          String mediaSequenceSql = "select * from MediaSequence where id="+id;
          Cursor mediaSequenceCursor = database.rawQuery(mediaSequenceSql,null);
          Map<String,Object> mediaSequence = getCursorMap(mediaSequenceCursor);
          String mediaSequenceId = (String) mediaSequence.get("id");
          String sequencePicSql = "select * from MediaSequence_pictures where MediaSequence_id="+mediaSequenceId;
          Cursor sequencePicCursor = database.rawQuery(sequencePicSql,null);
          List<Map<String,Object>> pictures = getCursorMapList(sequencePicCursor);
          List<String> pics = new ArrayList<>();
          for(Map<String,Object> pic : pictures){
            String image = (String) pic.get("pictures");
            if (!TextUtils.isEmpty(image)){
              image = basePath+image;
            }
            pics.add(image);
          }
          mediaSequence.put("pictures",pics);
          String pattern = (String) mediaSequence.get("pattern");
          if (!TextUtils.isEmpty(pattern)){
            pattern = basePath+pattern;
          }
          mediaSequence.put("pattern",pattern);
          medias.putAll(mediaSequence);
          break;
        case "PPT"://幻灯片
          String mediaPPTSql = "select * from MediaPPTItem where pptCombination_id="+id;
          Cursor mediaPPTCursor = database.rawQuery(mediaPPTSql,null);
          List<Map<String,Object>> pptItems = getCursorMapList(mediaPPTCursor);
          for(Map<String,Object> pptItem : pptItems){
            String pptPath = (String) pptItem.get("path");
            if (!TextUtils.isEmpty(pptPath)){
              pptPath = basePath + pptPath;
            }
            pptItem.put("path",pptPath);
          }
          medias.put("mediaPPTItems",pptItems);
          break;
        case "STATIC_SEQUENCE"://静态对象动画
          String sequencePicturesSql = "select * from MediaStaticSequence_pictures where MediaStaticSequence_id="+id;
          Cursor sequencePicturesCursor = database.rawQuery(sequencePicturesSql,null);
          List<Map<String,Object>> mediaSequencePictures = getCursorMapList(sequencePicturesCursor);
          List<String> staticPictures = new ArrayList<>();
          for(Map<String,Object>  p : mediaSequencePictures){
            String sequencePic = (String) p.get("pictures");
            if (!TextUtils.isEmpty(sequencePic)){
              sequencePic = basePath+sequencePic;
            }
            staticPictures.add(sequencePic);
          }
          medias.put("pictures",staticPictures);
          break;
        case "PICTURE":
          //小图点大图

          break;
      }
    }
    mediaMap.put("pictures",mediasList);
    database.close();
    GreenDaoHelper.getDaoSessionBook().clear();
    GreenDaoHelper.closeLocalBookDatabase();
    sendLocalBookServiceResult(Constant.GET_BOOK_MEDIA_LOCAL,mediaMap,handler,sn);
  }

  /**
   * 表格详情
   */
  public static void getTableText(Book localBook,Context context,String mediaId,Handler handler,String sn){
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+Utils.getBookVideoPath(localBook.getIsbn())+File.separator+Constant.LOCAL_BOOK_DB_NAME;
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    String tableTextSql = "select text from Paragraph where id="+mediaId;
    Database database = GreenDaoHelper.getDaoSessionBook().getDatabase();
    Cursor cursor = database.rawQuery(tableTextSql,null);
    Map<String,Object> paragraph = getCursorMap(cursor);
    String text = (String) paragraph.get("text");
    sendLocalBookServiceResult(Constant.GET_BOOK_MEDIA_LOCAL,text,handler,sn);
  }

  /**
   * 段落的作者
   */
  private static Map<String,Object> getParagraphAuthor(String paragraphId,Database database,String basePath){
    Cursor sectionAuthorCursor = database.rawQuery("select * from Paragraph_Author where Paragraph_id="+paragraphId,null);
    List<Map<String,Object>> sectionAuthors = getCursorMapList(sectionAuthorCursor);
    List<Map<String,Object>> authors = new ArrayList<>();
    Map<String,Object> result = new ArrayMap<>();
    for(Map<String,Object> sectionAuthor : sectionAuthors){
      String authorId = (String) sectionAuthor.get("authors_id");
      String authorSql = "select * from Author where id ="+authorId;
      Cursor authorCursor = database.rawQuery(authorSql,null);
      Map<String,Object> authorMap = getCursorMap(authorCursor);
      String authorExtend = (String) authorMap.get("isExtend");
      if(TextUtils.equals("1",authorExtend)){
        authorMap.put("extend",true);
      }else {
        authorMap.put("extend",false);
      }
      String mark = (String) authorMap.get("mark");//序里的签名图片
      if (!TextUtils.isEmpty(mark)){
        mark = basePath + mark;
      }
      authorMap.put("mark",mark);
      //序段落作者头衔，只有序的段落作者有头衔
      authorMap.put("titles",authorMap.get("preTitles"));
      authors.add(authorMap);
    }
    result.put("authors",authors);
    return result;
  }

  /**
   * 获取特殊类型节的内容
   * @param section
   * @param database
   * @param basePath
   * @return
   * @throws JSONException
   */
  //TODO
  private static Map<String,Object> getSubSection(Map<String,Object> section,Database database,String basePath) throws JSONException {
    String type = (String) section.get("DTYPE");
    String sectionId = (String) section.get("id");
    if (TextUtils.isEmpty(type)){
      return null;
    }
    switch (type){
      case "main_points":
        //要点
        String mainPointSql = "select * from Section_MainPoints where id="+sectionId;
        Cursor mainPointCursor = database.rawQuery(mainPointSql,null);
        Map<String, Object> mainPoint = getCursorMap(mainPointCursor);
        section.putAll(mainPoint);
        break;
      case "cover":
        //封面
        String coverSql = "select * from Section_Cover where id="+sectionId;
        Cursor coverCursor = database.rawQuery(coverSql,null);
        Map<String,Object> cover = getCursorMap(coverCursor);
        //封面图片需要添加本地路径  basePath
        String coverImage = basePath + cover.get("picture");
        cover.put("picture",coverImage);
        section.putAll(cover);
        break;
      case "flyleaf":
        //扉页
        //在BookHTML表中查询到当前书籍的 subject 类型
        Cursor bookHtmlCursor = database.rawQuery("select subjects from BookHTML",null);
        String flyleafSubject = "";
        while (bookHtmlCursor.moveToNext()){
          flyleafSubject = bookHtmlCursor.getString(bookHtmlCursor.getColumnIndex("subjects"));
        }
        bookHtmlCursor.close();
        //根据当前用户的类型，获取扉页的数据
        String flyleafSql = "SELECT *\n"
            + "        FROM Editors e\n"
            + "        WHERE subject= '"+flyleafSubject+
            "' and e.dutyType IN ('CHAIRMAN', 'CHIEF_EDITOR', 'ASSOCIATE_EDITOR','COMMON_EDITORS','SECRETARY')\n"
            + "        ORDER BY e.subject, e.dutyType";
        Cursor flyleafCursor = database.rawQuery(flyleafSql,null);
        //副主编
        List<Map<String,Object>> flyleafSqlAssociateEditor = new ArrayList<>();
        //主审
        List<Map<String,Object>> flyleafSqlChairman = new ArrayList<>();
        //主编
        List<Map<String,Object>> flyleafChiefEditor = new ArrayList<>();
        //编委
        List<Map<String,Object>> flyleafCommonEditor = new ArrayList<>();
        //学术秘书
        List<Map<String,Object>> flyleafSecretary = new ArrayList<>();
        Map<String,Object> flyleafContentMap = new ArrayMap<>();
        while (flyleafCursor.moveToNext()){
          String dutyType = flyleafCursor.getString(flyleafCursor.getColumnIndex("dutyType"));
          String name = flyleafCursor.getString(flyleafCursor.getColumnIndex("name"));
          String college = flyleafCursor.getString(flyleafCursor.getColumnIndex("college"));
          Map<String,Object> map = new ArrayMap<>();
          map.put("name",name);//姓名
          map.put("college",college);//学校，机构
          if("ASSOCIATE_EDITOR".equals(dutyType)){
            flyleafSqlAssociateEditor.add(map);
          }else if("CHAIRMAN".equals(dutyType)){
            flyleafSqlChairman.add(map);
          }else if("CHIEF_EDITOR".equals(dutyType)){
            flyleafChiefEditor.add(map);
          }else if("COMMON_EDITORS".equals(dutyType)){
            flyleafCommonEditor.add(map);
          }else if("SECRETARY".equals(dutyType)){
            flyleafSecretary.add(map);
          }
        }
        flyleafCursor.close();
        flyleafContentMap.put("ASSOCIATE_EDITOR",flyleafSqlAssociateEditor);
        flyleafContentMap.put("CHAIRMAN",flyleafSqlChairman);
        flyleafContentMap.put("CHIEF_EDITOR",flyleafChiefEditor);
        flyleafContentMap.put("COMMON_EDITORS",flyleafCommonEditor);
        flyleafContentMap.put("SECRETARY",flyleafSecretary);
        flyleafCursor.close();
        section.put("contents",flyleafContentMap);
        break;
      case "imprint":
        //版权页
        //版权数据，  Seciont_Imprint表
        String imprintSql = "select * from Section_Imprint where id="+sectionId;
        Cursor imprintCursor = database.rawQuery(imprintSql,null);
        Map<String,Object> imprint = getCursorMap(imprintCursor);
        //主编信息，  Imprint_editors表
        String imprintEditorSql = "select * from Imprint_editors where Imprint_id="+sectionId;
        Cursor imprintEditorCursor = database.rawQuery(imprintEditorSql,null);
        List<Map<String,Object>> imprintEditors = getCursorMapList(imprintEditorCursor);
        List<String> list = new ArrayList<>();
        for(Map<String,Object> imprintEditor : imprintEditors){
          String editors = (String) imprintEditor.get("editors");
          list.add(editors);
        }
        imprint.put("editors",list);
        String englishSubject = (String) imprint.get("subject");
        //教材英文名称转中文
        imprint.put("subjectName",bookNameEC(englishSubject));
        section.putAll(imprint);
        break;
      case "preface":
        //总序
        String prefaceSql = "select * from Section_Preface where id="+sectionId;
        Cursor prefaceCursor = database.rawQuery(prefaceSql,null);
        Map<String,Object> preface = getCursorMap(prefaceCursor);
        section.putAll(preface);
        break;
      case "cases":
        //病例分析，病例分析需要查询病例的表 Section_CaseSection， CaseItem
        Map<String,Object> caseSection = getCasesItemList(section,database,basePath);
        section.putAll(caseSection);
        break;
      case "summary":
        //小结
        Map<String,Object> summary = getSummary(section,database,basePath);
        section.putAll(summary);
        break;
      case "document":
        //主要文献参考
        Map<String,Object> document = getDocument(section,database);
        section.putAll(document);
        break;
      case "acknowledgements":
        //致谢名单
        Map<String,Object> acknowledgements = getAcknowledgements(section,database);
        section.putAll(acknowledgements);
        break;
      case "backCover":
        //封底
        Map<String,Object> backCover = getBackCover(section,database);
        String backCoverImage = basePath + backCover.get("bottomPicture");
        String isbn = basePath + backCover.get("isbn");
        backCover.put("bottomPicture",backCoverImage);
        backCover.put("isbn",isbn);
        String id = (String)backCover.get("id");
        backCover.put("tableView",getTableViewByCover(id,database,basePath));
        section.putAll(backCover);
        break;
      case "learningCenter":
        //学习园地
        Map<String,Object> learningCenter = getLearningCenter(section,database,basePath);
        section.putAll(learningCenter);
        break;
      case "chineseToEnglish":
        //中英文名词对照索引
        Map<String,List<Map<String,Object>>> translateCE = translate("CETranslate",database);
        section.put("contents",translateCE);
        break;
      case "englishToChinese":
        //英中文名词对照索引
        Map<String,List<Map<String,Object>>> translateEC = translate("ECTranslate",database);
        section.put("contents",translateEC);
        break;
      case "committeeCollege":
        //委员院校单位
        String collegeSql = "select name from Colleges";
        Cursor collegeCursor = database.rawQuery(collegeSql,null);
        List<Map<String,Object>> collegeList = getCursorMapList(collegeCursor);
        Map<String,Object> collegesMap = new ArrayMap<>();
        List<String> colleges = new ArrayList<>();
        for (Map<String,Object> college : collegeList){
          String name = (String) college.get("name");
          colleges.add(name);
        }
        collegesMap.put("colleges",colleges);
        section.put("contents",collegesMap);
        break;
      case "catalog":
        //教材目录
        Cursor subjectCursor = database.rawQuery("select distinct(subject) from Editors",null);
        List<Map<String,Object>> catalogContents = new ArrayList<>();
        while (subjectCursor.moveToNext()){
          String catalogSubject = subjectCursor.getString(subjectCursor.getColumnIndex("subject"));
          String catalogSql = " SELECT *\n"
              + "        FROM Editors e\n"
              + "        WHERE subject='"+catalogSubject+"' and e.dutyType IN ('CHAIRMAN', 'CHIEF_EDITOR', 'ASSOCIATE_EDITOR')\n"
              + "        ORDER BY e.subject, e.dutyType";
          Cursor catalogCursor = database.rawQuery(catalogSql,null);
          List<String> associateEditor = new ArrayList<>();
          List<String> chairman = new ArrayList<>();
          List<String> chiefEditor = new ArrayList<>();
          Map<String,Object> contentMap = new ArrayMap<>();
          while (catalogCursor.moveToNext()){
            String dutyType = catalogCursor.getString(catalogCursor.getColumnIndex("dutyType"));
            String name = catalogCursor.getString(catalogCursor.getColumnIndex("name"));
            if("ASSOCIATE_EDITOR".equals(dutyType)){
              associateEditor.add(name);
            }else if("CHAIRMAN".equals(dutyType)){
              chairman.add(name);
            }else if("CHIEF_EDITOR".equals(dutyType)){
              chiefEditor.add(name);
            }
          }
          catalogCursor.close();
          contentMap.put("ASSOCIATE_EDITOR",associateEditor);
          contentMap.put("CHAIRMAN",chairman);
          contentMap.put("CHIEF_EDITOR",chiefEditor);
          String subject = bookNameEC(catalogSubject);
          if(subject == null){
            subject = catalogSubject;
          }
          contentMap.put("subject",subject);
          catalogContents.add(contentMap);
        }
        subjectCursor.close();
        section.put("contents",catalogContents);

        break;
      case "committeeConsultant":
        //顾问委员会
        String committeeSql = "SELECT DISTINCT\n"
            + "            committee.name,\n"
            + "            committee.title,\n"
            + "            committee.dutyTitle,\n"
            + "            committee.unit\n"
            + "        FROM Committee committee\n"
            + "        WHERE title IN ('CHIEF_CONSULTANT', 'CONSULTANT')\n"
            + "        ORDER BY title";
        Cursor committeeCursor = database.rawQuery(committeeSql,null);
        List<Map<String,Object>> chiefConsultants = new ArrayList<>();
        List<Map<String,Object>> consultants = new ArrayList<>();
        Map<String,Object> committeeContents = new ArrayMap<>();
        while (committeeCursor.moveToNext()){
          Map<String,Object> map = new ArrayMap<>();
          String title = committeeCursor.getString(committeeCursor.getColumnIndex("title"));
          String name = committeeCursor.getString(committeeCursor.getColumnIndex("name"));
          String dutyTitle = committeeCursor.getString(committeeCursor.getColumnIndex("dutyTitle"));
          String unit = committeeCursor.getString(committeeCursor.getColumnIndex("unit"));
          map.put("name",name);
          map.put("dutyTitle",dutyTitle);
          map.put("unit",unit);
          if("CHIEF_CONSULTANT".equals(title)){
            chiefConsultants.add(map);
          }else if("CONSULTANT".equals(title)){
            consultants.add(map);
          }
        }
        committeeCursor.close();
        committeeContents.put("CHIEF_CONSULTANT",chiefConsultants);
        committeeContents.put("CONSULTANT",consultants);
        section.put("contents",committeeContents);
        break;
      case "committeeWriting":
        //编写委员会
        String writingSql = "SELECT DISTINCT\n"
            + "            committee.name,\n"
            + "            committee.title,\n"
            + "            committee.dutyTitle,\n"
            + "            committee.unit\n"
            + "        FROM Committee committee\n"
            + "        WHERE title IN ('DIRECTOR', 'EXECUTIVE_DIRECTOR', 'SECRETARY_GENERAL', 'DEPUTY_DIRECTOR', 'COMMISSIONER')\n"
            + "        ORDER BY title";
        Cursor writingCursor = database.rawQuery(writingSql,null);
        List<Map<String,Object>> DIRECTOR = new ArrayList<>();
        List<Map<String,Object>> EXECUTIVE_DIRECTOR = new ArrayList<>();
        List<Map<String,Object>> SECRETARY_GENERAL = new ArrayList<>();
        List<Map<String,Object>> DEPUTY_DIRECTOR = new ArrayList<>();
        List<Map<String,Object>> COMMISSIONER = new ArrayList<>();
        Map<String,Object> writingMap = new ArrayMap<>();
        while (writingCursor.moveToNext()){
          Map<String,Object> map = new ArrayMap<>();
          String title = writingCursor.getString(writingCursor.getColumnIndex("title"));
          String name = writingCursor.getString(writingCursor.getColumnIndex("name"));
          String dutyTitle = writingCursor.getString(writingCursor.getColumnIndex("dutyTitle"));
          String unit = writingCursor.getString(writingCursor.getColumnIndex("unit"));
          map.put("name",name);
          map.put("dutyTitle",dutyTitle);
          map.put("unit",unit);
          if("DIRECTOR".equals(title)){
            DIRECTOR.add(map);
          }else if("EXECUTIVE_DIRECTOR".equals(title)){
            EXECUTIVE_DIRECTOR.add(map);
          }else if("SECRETARY_GENERAL".equals(title)){
            SECRETARY_GENERAL.add(map);
          }else if("DEPUTY_DIRECTOR".equals(title)){
            DEPUTY_DIRECTOR.add(map);
          }else if("COMMISSIONER".equals(title)){
            COMMISSIONER.add(map);
          }
        }
        writingCursor.close();
        writingMap.put("DIRECTOR",DIRECTOR);
        writingMap.put("EXECUTIVE_DIRECTOR",EXECUTIVE_DIRECTOR);
        writingMap.put("SECRETARY_GENERAL",SECRETARY_GENERAL);
        writingMap.put("DEPUTY_DIRECTOR",DEPUTY_DIRECTOR);
        writingMap.put("COMMISSIONER",COMMISSIONER);
        section.put("contents",writingMap);
        break;
    }
    return section;
  }

  /**
   * 学习园地
   * @param section 节
   * @param database database
   * @param basePath basePath
   * @return
   * @throws JSONException
   */
  private static  Map<String,Object> getLearningCenter(Map<String,Object> section, Database database,String basePath) throws JSONException {
    String id = (String) section.get("id");
//    String learningCenterSql = "select * from Section_LearningCenter where id="+id;
//    Cursor learningCenterCursor = database.rawQuery(learningCenterSql,null);
//    Map<String,Object> learningCenter = getCursorMap(learningCenterCursor);
    Map<String,Object> learningCenter = new HashMap<>();
    //速记手册
    String sectionReferenceSql = "select * from Section_Reference left join Section on Section_Reference.id=Section.id where Section_Reference.learningCenter_id="+id;
    Cursor sectionReferenceCursor = database.rawQuery(sectionReferenceSql,null);
    Map<String,Object> sectionReference = getCursorMap(sectionReferenceCursor);
    //手册里的内容段落是从  Paragraph里获取的
    List<Map<String,Object>> references = getParagraphContentList(sectionReference,database,basePath);
    sectionReference.put("section",references);
    //推荐阅读
    String sectionShorthandSql = "select * from Section_Shorthand left join Section on Section_Shorthand.id=Section.id where Section_Shorthand.learningCenter_id="+id;
    Cursor sectionShorthandCursor = database.rawQuery(sectionShorthandSql,null);
    Map<String,Object> sectionShorthand = getCursorMap(sectionShorthandCursor);
    //Paragraph里获取的段落内容
    List<Map<String,Object>> shorthand = getParagraphContentList(sectionShorthand,database,basePath);
    sectionShorthand.put("section",shorthand);

    learningCenter.put("reference",sectionReference);
    learningCenter.put("shorthand",sectionShorthand);
    return learningCenter;
  }

  /**
   * 封底
   */
  private static Map<String,Object> getBackCover(Map<String,Object> section,Database database){
    String sectionId = (String) section.get("id");
    String backCoverSql = "select * from Section_BackCover where id="+sectionId;
    Cursor cursor = database.rawQuery(backCoverSql,null);
    return getCursorMap(cursor);
  }

  /**
   * 致谢名单 NameList
   * 有参编人员和致谢人员两种
   */
  private static Map<String,Object> getAcknowledgements(Map<String,Object> section,Database database){
    String sectionId = (String) section.get("id");
    Map<String,Object> acknowledgements = new ArrayMap<>();
    String editorSql = "select * from NameList where kind = 'editors' and acknowledgements_id="+sectionId;
    Cursor editorCursor = database.rawQuery(editorSql,null);
    List<Map<String,Object>> editors = getCursorMapList(editorCursor);
    acknowledgements.put("editors",editors);

    String thanksSql = "select * from NameList where kind = 'thanks' and acknowledgements_id="+sectionId;
    Cursor thanksCursor = database.rawQuery(thanksSql,null);
    List<Map<String,Object>> thanks = getCursorMapList(thanksCursor);
    acknowledgements.put("thanks",thanks);
    return acknowledgements;
  }

  /**
   * 对照索引 Translate
   */
  private static Map<String,List<Map<String,Object>>> translate(String translateDType,Database database) throws JSONException {
    Cursor cursor = database.rawQuery("select distinct(initial) from Translate order by initial asc",null);
    List<String> initials = new ArrayList<>();
    while (cursor.moveToNext()){
      String initial = cursor.getString(cursor.getColumnIndex("initial"));
      if(initial != null){
        initials.add(initial);
      }
    }
    cursor.close();
    Map<String,List<Map<String,Object>>> map = new ArrayMap<>();
    long start = System.currentTimeMillis();
    for(String initial : initials){
      String translateSql = "select * from Translate where initial='"+initial+"' and DTYPE='"+translateDType+"';";
      Cursor translateCursor = database.rawQuery(translateSql,null);
      List<Map<String,Object>> translates = getCursorMapList(translateCursor);
      if(translates != null && translates.size()>0){
        map.put(initial,translates);
      }
    }
    Logger.i("对照索引 "+(System.currentTimeMillis() - start));
    return map;
  }

  /**
   * 文献参考 DocumentItem
   */
  private static Map<String,Object> getDocument(Map<String,Object> section,Database database){
    String sectionId = (String) section.get("id");
    Map<String,Object> document = new ArrayMap<>();
    String documentItemSql = "select * from DocumentItem where document_id="+sectionId;
    Cursor documentItemCursor = database.rawQuery(documentItemSql,null);
    List<Map<String,Object>> documents = getCursorMapList(documentItemCursor);
    document.put("documents",documents);
    return document;
  }

  /**
   * 小结
   * 小结有中英文
   */
  private static Map<String,Object> getSummary(Map<String,Object> section,Database database,String basePath)throws JSONException{

    String sectionId = (String) section.get("id");
    String summarySql = "select * from Section_Summary where id="+sectionId;
    Cursor summaryCursor = database.rawQuery(summarySql,null);
    Map<String,Object> summary = getCursorMap(summaryCursor);
    //查询小结的中文

    String chineseStr = (String) summary.get("chinese_id");
    Map<String,Object> map = new HashMap<>();
    map.put("id",chineseStr);
    List<Map<String,Object>> sections = getParagraphContentById(map,database,basePath);
    if (sections.size()>0){
      summary.put("chinese",sections.get(0));//// TODO: 2017/7/12
    }else{
      summary.put("chinese",null);
    }



    //查询小结的英文

    String english = (String) summary.get("english_id");
    Map<String,Object> map2 = new HashMap<>();
    map2.put("id",english);
    List<Map<String,Object>> sections2 = getParagraphContentById(map2,database,basePath);
    if (sections2.size()>0){
      summary.put("english",sections2.get(0));
    }else{
      summary.put("english",null);
    }
    return summary;
  }
  /**
   * 病例分析
   */
  private static Map<String,Object> getCasesItemList(Map<String,Object> section,Database database,String basePath) throws JSONException {
    String sectionId = (String) section.get("id");
    //Section_CaseSection
    String caseSectionSql = "select * from Section_CaseSection where id="+sectionId;
    Cursor caseSectionCursor = database.rawQuery(caseSectionSql,null);
    Map<String,Object> caseSection = getCursorMap(caseSectionCursor);
    //CasesItem
    String casesItemsSql = "select * from CasesItem where cases_id="+sectionId;
    Cursor casesItemCursor = database.rawQuery(casesItemsSql,null);
    List<Map<String,Object>> casesItemList = getCursorMapList(casesItemCursor);
    for(Map<String,Object> casesItem : casesItemList){
      String casesItemId = (String) casesItem.get("id");
      //根据CasesItem_Author的关联查询病例段落下的作者
      String casesItemAuthor = "select * from CasesItem_Author left join Author on CasesItem_Author.authors_id=Author.id"
          + " where CasesItem_id="+casesItemId;
      Cursor casesItemAuthorCursor = database.rawQuery(casesItemAuthor,null);
      List<Map<String,Object>> authors = getCursorMapList(casesItemAuthorCursor);
      for(Map<String,Object> author : authors){
        String isExtend = (String) author.get("isExtend");
        if(isExtend != null && "1".equals(isExtend)){
          author.put("extend",true);//显示作者简介按钮
        }else {
          author.put("extend",false);//不显示
        }
        String photo = (String) author.get("photo");
        if (!TextUtils.isEmpty(photo)){
          photo = basePath+photo;
          author.put("photo",photo);//不显示
        }else{
          author.put("photo","");
        }
      }
      casesItem.put("authors",authors);

      //病例段落下有子段落，具体层级不知道，需要递归查询（实现方法可优化）
      List<Map<String,Object>> sectionCasesItems = getCasesItem(casesItemId,database,basePath);
      casesItem.put("section",sectionCasesItems);
    }
    caseSection.put("casesItemList",casesItemList);
    return caseSection;
  }
  /**
   * 病例分析
   */
  private static List<Map<String,Object>> getCasesItem(String id,Database database,String basePath)
      throws JSONException {
    String casesItemsSql = "select * from CasesItem where parent_id="+id;
    Cursor casesItemCursor = database.rawQuery(casesItemsSql,null);
    List<Map<String,Object>> casesItems = getCursorMapList(casesItemCursor);
    for(Map<String,Object> casesItem : casesItems){
      String casesItemId = (String) casesItem.get("id");
      String casesItemAuthor = "select * from CasesItem_Author left join Author on CasesItem_Author.authors_id=Author.id"
          + " where CasesItem_id="+casesItemId;
      Cursor casesItemAuthorCursor = database.rawQuery(casesItemAuthor,null);
      List<Map<String,Object>> authors = getCursorMapList(casesItemAuthorCursor);
      for(Map<String,Object> author : authors){
        String isExtend = (String) author.get("isExtend");
        if(isExtend != null && "1".equals(isExtend)){
          author.put("extend",true);
        }else {
          author.put("extend",false);
        }
        String photo = (String) author.get("photo");
        if (!TextUtils.isEmpty(photo)){
          photo = basePath+photo;
          author.put("photo",photo);//不显示
        }else{
          author.put("photo","");
        }
      }
      casesItem.put("authors",authors);
      // TODO: 2017/8/8
      //查keyword
      String casesKeyWord = "select ke.* from KeyWord ke left join CasesItem_KeyWord ck on ke.id = ck.keyWord_id"
          + " where CasesItem_id="+casesItemId;
      Cursor casesKeyWordCursor = database.rawQuery(casesKeyWord,null);
      List<Map<String,Object>> keyWords = getCursorMapList(casesKeyWordCursor);
      for(Map<String,Object> keyword : keyWords){
        String link = (String)keyword.get("link");
        if (!TextUtils.isEmpty(link)){
          link = basePath+link;
        }
        String readAudio = (String) keyword.get("readAudio");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(readAudio)){
          readAudio = basePath+readAudio;
        }
        String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(extendPath)){
          extendPath = basePath+extendPath;
        }
        keyword.put("extendPath",extendPath);
        keyword.put("link",link);
        keyword.put("readAudio",readAudio);
      }
      casesItem.put("keyWord",keyWords);
      //查 MediaBlock
      String mediaBlockSql = "select ke.* from MediaBlock ke left join CasesItem_MediaBlock ck on ke.id = ck.mediaBlocks_id"
          + " where CasesItem_id="+casesItemId;
      Cursor mediaBlockCursor = database.rawQuery(mediaBlockSql,null);
      List<Map<String,Object>> mediaBlocks = getCursorMapList(mediaBlockCursor);
      casesItem.put("mediaBlock",getMediaBlock(mediaBlocks,database,basePath));

      String parentId = (String) casesItem.get("parent_id");
      if(parentId != null && !TextUtils.equals("",parentId)){
        List<Map<String,Object>> sectionCasesItems = getCasesItem(casesItemId,database,basePath);
        casesItem.put("section",sectionCasesItems);
      }
    }
    return casesItems;
  }

  /**
   * 获取章节段落
   */
  private static List<Map<String,Object>> getParagraphContentList(Map<String,Object> section,Database database,String basePath) throws JSONException {
    //获取该节对应的所有 基础段落
    String sectionId = (String) section.get("id");
    String sql = "select * from Section_Paragraph  left join Paragraph on Section_Paragraph.paragraphs_id= Paragraph.id where Section_Paragraph.sections_id="+sectionId+" order by paragraphs_id"+";";
    Cursor paragraphsCursor = database.rawQuery(sql,null);
    List<Map<String,Object>> paragraphs = getCursorMapList(paragraphsCursor);
    if(paragraphs == null){
      //没有对应结果
      return null;
    }
    String sectionExtend = (String) section.get("extend");
    if(!"BASIC".equals(sectionExtend)){
      if(paragraphs.size() > 1){
        //内容扩展章节获取
        paragraphs.remove(0);
      }
    }

    List<Map<String,Object>> myParagraphs = new ArrayList<>();
    for(int i= 0; i<paragraphs.size(); i++){
      Map<String,Object> paragraph = paragraphs.get(i);
      String paragraphId = (String) paragraph.get("id");
      //KeyWord 段落里的音频点读
      String keywordSql = "select * from KeyWord ke left join Paragraph_KeyWord pa on ke.id = pa.keyWord_id where pa.paragraph_id="+paragraphId+";";
      Cursor keywordCursor = database.rawQuery(keywordSql,null);
      List<Map<String,Object>> keywords = getCursorMapList(keywordCursor);
      for(Map<String,Object> keyword : keywords){
        //音频点读的地址添加 basePath
        String link =(String) keyword.get("link");
        if (!TextUtils.isEmpty(link)){
          link = basePath+link;
        }
        String readAudio = (String) keyword.get("readAudio");
        if (!TextUtils.isEmpty(readAudio)){
          readAudio = basePath+readAudio;
        }
        String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(extendPath)){
          extendPath = basePath+extendPath;
        }
        keyword.put("extendPath",extendPath);

        keyword.put("link",link);
        keyword.put("readAudio",readAudio);
      }
      //段落下的图片，音频等特效
      String mediaBlockSql = "select * from MediaBlock where paragraph_id="+paragraphId+";";
      Cursor mediaBlockCursor = database.rawQuery(mediaBlockSql,null);
      List<Map<String,Object>> mediaBlocks = getCursorMapList(mediaBlockCursor);
      mediaBlocks = getMediaBlock(mediaBlocks,database,basePath);
      paragraph.put("mediaBlocks",mediaBlocks);
      paragraph.put("keyWord",keywords);
      //段落作者
      Map<String,Object> authorMap = getParagraphAuthor(paragraphId,database,basePath);
      paragraph.putAll(authorMap);
      //获取段落表格
      paragraph.put("tableViews",getTableView(paragraphId,database,basePath));

      List<Map<String,Object>> sections = getParagraphContent(paragraph,database,basePath);
      paragraph.put("section",sections);
      myParagraphs.add(paragraph);
    }

    return myParagraphs;
  }
  /**
   * 获取章节段落
   */
  private static List<Map<String,Object>> getParagraphContent(Map<String,Object> paragraph,Database database,String basePath)throws JSONException {
    //查找段落下面的子段落集合
    String paragraphId = (String) paragraph.get("id");
    String paragraphSql = "select * from Paragraph where parent_id="+paragraphId+";";
    Cursor paragraphCursor = database.rawQuery(paragraphSql,null);
    List<Map<String,Object>> childParagraphs = getCursorMapList(paragraphCursor);//子段落
    List<Map<String,Object>> paragraphs = new ArrayList<>();
    for(int i=0; i<childParagraphs.size(); i++){
      Map<String,Object> childParagraph = childParagraphs.get(i);
      String childParagraphId = (String) childParagraph.get("id");
      String link_id = (String) childParagraph.get("link_id");
      childParagraph.put("link",link_id);
      //段落下的音频点读
//      String keywordSql = "select * from KeyWord ke left join  where paragraph_id="+childParagraphId+";";//TODO :// FIX
      String keywordSql = "select * from KeyWord ke left join Paragraph_KeyWord pa on ke.id = pa.keyWord_id where pa.paragraph_id="+childParagraphId+";";

      Cursor keywordCursor = database.rawQuery(keywordSql,null);
      List<Map<String,Object>> keywords = getCursorMapList(keywordCursor);
      for(Map<String,Object> keyword : keywords){
        String type = (String) keyword.get("type");
        if(!TextUtils.isEmpty(type)){
          if(StringUtils.isNumeric(type)){
            keyword.put("type", Integer.parseInt(type));
          }
        }
      }
      for(Map<String,Object> keyword : keywords){
        String link = (String)keyword.get("link");
        if (!TextUtils.isEmpty(link)){
          link = basePath+link;
        }
        String readAudio = (String) keyword.get("readAudio");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(readAudio)){
          readAudio = basePath+readAudio;
        }
        String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(extendPath)){
          extendPath = basePath+extendPath;
        }
        keyword.put("extendPath",extendPath);
        keyword.put("link",link);
        keyword.put("readAudio",readAudio);
      }
      //段落下的图片，视频等特效
      String mediaBlockSql = "select * from MediaBlock where paragraph_id="+childParagraphId+";";
      Cursor mediaBlockCursor = database.rawQuery(mediaBlockSql,null);
      List<Map<String,Object>> mediaBlocks = getCursorMapList(mediaBlockCursor);
      mediaBlocks = getMediaBlock(mediaBlocks,database,basePath);
      childParagraph.put("mediaBlocks",mediaBlocks);
      childParagraph.put("keyWord",keywords);
      String parentId = (String) childParagraph.get("parent_id");
      Map<String,Object> authorMap = getParagraphAuthor(childParagraphId,database,basePath);
      childParagraph.putAll(authorMap);
      //段落下的表格
      childParagraph.put("tableViews",getTableView(childParagraphId,database,basePath));
      //获取拓展阅读
      childParagraph.put("sectionExtend",getSectionExtend(childParagraph,database));

      if(!TextUtils.equals(parentId,"0") || parentId != null){
        List<Map<String,Object>> sections = getParagraphContent(childParagraph,database,basePath);
        childParagraph.put("section",sections);
      }
      paragraphs.add(childParagraph);
    }

    return  paragraphs;
  }


  /**
   * 获取章节段落
   */
  private static List<Map<String,Object>> getParagraphContentById(Map<String,Object> paragraph,Database database,String basePath)throws JSONException {
    //查找段落下面的子段落集合
    String paragraphId = (String) paragraph.get("id");
    String paragraphSql = "select * from Paragraph where id="+paragraphId+";";
    Cursor paragraphCursor = database.rawQuery(paragraphSql,null);
    List<Map<String,Object>> childParagraphs = getCursorMapList(paragraphCursor);//子段落
    List<Map<String,Object>> paragraphs = new ArrayList<>();
    for(int i=0; i<childParagraphs.size(); i++){
      Map<String,Object> childParagraph = childParagraphs.get(i);
      String childParagraphId = (String) childParagraph.get("id");
      //段落下的音频点读
//      String keywordSql = "select * from KeyWord ke left join  where paragraph_id="+childParagraphId+";";//TODO :// FIX
      String keywordSql = "select * from KeyWord ke left join Paragraph_KeyWord pa on ke.id = pa.keyWord_id where pa.paragraph_id="+childParagraphId+";";

      Cursor keywordCursor = database.rawQuery(keywordSql,null);
      List<Map<String,Object>> keywords = getCursorMapList(keywordCursor);
      for(Map<String,Object> keyword : keywords){
        String type = (String) keyword.get("type");
        if(!TextUtils.isEmpty(type)){
          if(StringUtils.isNumeric(type)){
            keyword.put("type", Integer.parseInt(type));
          }
        }
      }
      for(Map<String,Object> keyword : keywords){
        String link = (String)keyword.get("link");
        if (!TextUtils.isEmpty(link)){
          link = basePath+link;
        }
        String readAudio = (String) keyword.get("readAudio");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(readAudio)){
          readAudio = basePath+readAudio;
        }
        String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(extendPath)){
          extendPath = basePath+extendPath;
        }
        keyword.put("extendPath",extendPath);
        keyword.put("link",link);
        keyword.put("readAudio",readAudio);
      }
      //段落下的图片，视频等特效
      String mediaBlockSql = "select * from MediaBlock where paragraph_id="+childParagraphId+";";
      Cursor mediaBlockCursor = database.rawQuery(mediaBlockSql,null);
      List<Map<String,Object>> mediaBlocks = getCursorMapList(mediaBlockCursor);
      mediaBlocks = getMediaBlock(mediaBlocks,database,basePath);
      childParagraph.put("mediaBlocks",mediaBlocks);
      childParagraph.put("keyWord",keywords);
      String parentId = (String) childParagraph.get("parent_id");
      Map<String,Object> authorMap = getParagraphAuthor(childParagraphId,database,basePath);
      childParagraph.putAll(authorMap);
      //段落下的表格
      childParagraph.put("tableViews",getTableView(childParagraphId,database,basePath));
      //获取拓展阅读
      childParagraph.put("sectionExtend",getSectionExtend(childParagraph,database));

      if(!TextUtils.equals(parentId,"0") || parentId != null){
        List<Map<String,Object>> sections = getParagraphContent(childParagraph,database,basePath);
        childParagraph.put("section",sections);
      }
      paragraphs.add(childParagraph);
    }

    return  paragraphs;
  }


  /**
   * 根据paragraphId获取段落数据
   */
  public static void getParagraphById(Book localBook, String id,Handler handler,String sn)throws JSONException {

    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+Utils.getBookVideoPath(localBook.getIsbn())+File.separator+Constant.LOCAL_BOOK_DB_NAME;
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    File file = new File(path);
    if(!file.exists()){
      //库文件不存在
      sendServiceResult(true,false, Constant.GET_BOOK_CATELOG,"这本书已经丢失或损坏，请重新安装",null,handler,sn);
      return;
    }
    GreenDaoHelper.initLocalBookDatabasePassword(null,path,"");
    Database database = GreenDaoHelper.getDaoSessionBook().getDatabase();
    //查找段落下面的子段落集合
    String paragraphId = id;
    String paragraphSql = "select * from Paragraph where id="+paragraphId+";";
    Cursor paragraphCursor = database.rawQuery(paragraphSql,null);
    List<Map<String,Object>> childParagraphs = getCursorMapList(paragraphCursor);//子段落
    Map<String,Object> paragraphs = new HashMap<>();
    for(int i=0; i<childParagraphs.size(); i++){
      Map<String,Object> childParagraph = childParagraphs.get(i);
      String childParagraphId = (String) childParagraph.get("id");
      //段落下的音频点读
//      String keywordSql = "select * from KeyWord ke left join  where paragraph_id="+childParagraphId+";";//TODO :// FIX
      String keywordSql = "select * from KeyWord ke left join Paragraph_KeyWord pa on ke.id = pa.keyWord_id where pa.paragraph_id="+childParagraphId+";";

      Cursor keywordCursor = database.rawQuery(keywordSql,null);
      List<Map<String,Object>> keywords = getCursorMapList(keywordCursor);
      for(Map<String,Object> keyword : keywords){
        String type = (String) keyword.get("type");
        if(!TextUtils.isEmpty(type)){
          if(StringUtils.isNumeric(type)){
            keyword.put("type", Integer.parseInt(type));
          }
        }
      }
      for(Map<String,Object> keyword : keywords){
        String link = (String)keyword.get("link");
        if (!TextUtils.isEmpty(link)){
          link = basePath+link;
        }
        String readAudio = (String) keyword.get("readAudio");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(readAudio)){
          readAudio = basePath+readAudio;
        }
        String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
        if (!TextUtils.isEmpty(extendPath)){
          extendPath = basePath+extendPath;
        }
        keyword.put("extendPath",extendPath);
        keyword.put("link",link);
        keyword.put("readAudio",readAudio);

      }
      //段落下的图片，视频等特效
      String mediaBlockSql = "select * from MediaBlock where paragraph_id="+childParagraphId+";";
      Cursor mediaBlockCursor = database.rawQuery(mediaBlockSql,null);
      List<Map<String,Object>> mediaBlocks = getCursorMapList(mediaBlockCursor);
      mediaBlocks = getMediaBlock(mediaBlocks,database,basePath);
      childParagraph.put("mediaBlocks",mediaBlocks);
      childParagraph.put("keyWord",keywords);
      String parentId = (String) childParagraph.get("parent_id");
      Map<String,Object> authorMap = getParagraphAuthor(childParagraphId,database,basePath);
      childParagraph.putAll(authorMap);
      //段落下的表格
      childParagraph.put("tableViews",getTableView(childParagraphId,database,basePath));
      //获取拓展阅读
      childParagraph.put("sectionExtend",getSectionExtend(childParagraph,database));

      if(!TextUtils.equals(parentId,"0") || parentId != null){
        List<Map<String,Object>> sections = getParagraphContent(childParagraph,database,basePath);
        childParagraph.put("section",sections);
      }
      paragraphs.putAll(childParagraph);
    }
    sendLocalBookServiceResult(Constant.GET_BOOK_PARAGRAPH_LOCAL,paragraphs,handler,sn);
  }



  /**
   * 获取表格数据
   */
  private static List<Map<String,Object>> getTableView(String paragraphId,Database database,String basePath){
    //查询段落下的表格（可能有多个表格）
    String tableViewSql = "select * from TableView where paragraph_id="+paragraphId;
    Cursor tableViewCursor = database.rawQuery(tableViewSql,null);
    List<Map<String,Object>> tableViews = getCursorMapList(tableViewCursor);
    //遍历表格
    for(Map<String,Object> tableView : tableViews){
      String tableViewId = (String) tableView.get("id");
      //获取表格下的行内容  TRView表
      String trViewSql = "select * from TRView where tableView_id="+tableViewId;
      Cursor trViewCursor = database.rawQuery(trViewSql,null);
      List<Map<String,Object>> trViews = getCursorMapList(trViewCursor);
      for(Map<String,Object> trView : trViews){
        String trViewId = (String) trView.get("id");
        //获取行下的列内容
        String tdViewSql = "select * from TDView where trView_id="+trViewId;
        Cursor tdViewCursor = database.rawQuery(tdViewSql,null);
        List<Map<String,Object>> tdViews = getCursorMapList(tdViewCursor);
        for(Map<String,Object> tdView : tdViews){
          //表格中可能有图片，需要拼接本地路径，basePath
          String type = (String) tdView.get("type");
          if("picture".equals(type)){
            String pic = basePath + tdView.get("text");
            tdView.put("text",pic);
          }
          //tdView下有特效，keyWords    TDView_KeyWord表
          String tdViewId = (String) tdView.get("id");
          //修改表格查询语句 服务器修改表结构
          String keywordSql = "select * from KeyWord ke left join TDView_KeyWord td on ke.id = td.keyWord_id where td.TDView_id ="+tdViewId;
          Cursor keywordCursor = database.rawQuery(keywordSql,null);
          List<Map<String,Object>> keyWords = getCursorMapList(keywordCursor);
          for(Map<String,Object> keyword : keyWords){
            //音频点读的地址添加 basePath
            String link = (String)keyword.get("link");
            if (!TextUtils.isEmpty(link)){
              link = basePath+link;
            }
            String readAudio = (String) keyword.get("readAudio");//拼接本地路径 basePath
            if (!TextUtils.isEmpty(readAudio)){
              readAudio = basePath+readAudio;
            }
            String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
            if (!TextUtils.isEmpty(extendPath)){
              extendPath = basePath+extendPath;
            }
            keyword.put("extendPath",extendPath);
            keyword.put("link",link);
            keyword.put("readAudio",readAudio);
          }
          tdView.put("keyWords",keyWords);

        }
        trView.put("tdViewList",tdViews);
      }
      //tabview 本地路径拼接
      String abbreviation=(String)tableView.get("abbreviation");
      if (!TextUtils.isEmpty(abbreviation)){
        tableView.put("abbreviation",basePath+abbreviation);
      }else{
        tableView.put("abbreviation","");
      }
      tableView.put("trViews",trViews);
    }
    return tableViews;
  }

  /**
   * 获取表格数据
   */
  private static Map<String,Object> getTableViewByCover(String paragraphId,Database database,String basePath){
    //查询段落下的表格（可能有多个表格）
    String tableViewSql = "select * from TableView where backCover_id="+paragraphId;
    Cursor tableViewCursor = database.rawQuery(tableViewSql,null);
    Map<String,Object>tableViews = getCursorMap(tableViewCursor);
    //遍历表格
    String tableViewId = (String) tableViews.get("id");
    //获取表格下的行内容  TRView表
    String trViewSql = "select * from TRView where tableView_id="+tableViewId;
    Cursor trViewCursor = database.rawQuery(trViewSql,null);
    List<Map<String,Object>> trViews = getCursorMapList(trViewCursor);
    for(Map<String,Object> trView : trViews){
      String trViewId = (String) trView.get("id");
      //获取行下的列内容
      String tdViewSql = "select * from TDView where trView_id="+trViewId;
      Cursor tdViewCursor = database.rawQuery(tdViewSql,null);
      List<Map<String,Object>> tdViews = getCursorMapList(tdViewCursor);
      for(Map<String,Object> tdView : tdViews){
        //表格中可能有图片，需要拼接本地路径，basePath
        String type = (String) tdView.get("type");
        if("picture".equals(type)){
          String pic = basePath + tdView.get("text");
          tdView.put("text",pic);
        }
        //tdView下有特效，keyWords    TDView_KeyWord表
        String tdViewId = (String) tdView.get("id");
        //修改表格查询语句 服务器修改表结构
        String keywordSql = "select * from KeyWord ke left join TDView_KeyWord td on ke.id = td.keyWord_id where td.TDView_id ="+tdViewId;
        Cursor keywordCursor = database.rawQuery(keywordSql,null);
        List<Map<String,Object>> keyWords = getCursorMapList(keywordCursor);
        for(Map<String,Object> keyword : keyWords){
          //音频点读的地址添加 basePath
          String link = (String)keyword.get("link");
          if (!TextUtils.isEmpty(link)){
            link = basePath+link;
          }
          String readAudio = (String) keyword.get("readAudio");//拼接本地路径 basePath
          if (!TextUtils.isEmpty(readAudio)){
            readAudio = basePath+readAudio;
          }
          String extendPath = (String) keyword.get("extendPath");//拼接本地路径 basePath
          if (!TextUtils.isEmpty(extendPath)){
            extendPath = basePath+extendPath;
          }
          keyword.put("extendPath",extendPath);
          keyword.put("link",link);
          keyword.put("readAudio",readAudio);
        }
        tdView.put("keyWords",keyWords);

      }
      trView.put("tdViewList",tdViews);

      tableViews.put("trViews",trViews);
    }
    return tableViews;
  }

  /**
   * 获取段落下的拓展内容
   * @param paragraph
   * @param database
   * @return
   */
  private static Map<String, String> getSectionExtend(Map<String,Object> paragraph,Database database){
    //      "select * from Section_Paragraph left join Paragraph on Section_Paragraph."
    Map<String,String> extendMap = new HashMap<>();
    String paragraphId = (String) paragraph.get("id");
    //1。根据该段落，查询出该段落的节 2.查询段落节的template字段值，如果是extend，则该节是拓展节
    String sectionParagraphSql = "select * from Section_Paragraph where paragraphs_id="+paragraphId;
    Cursor sectionParagraphCursor = database.rawQuery(sectionParagraphSql,null);
    List<Map<String,Object>> spList = getCursorMapList(sectionParagraphCursor);
    for(Map<String,Object> sectionParagraph : spList){
      String sectionId = (String) sectionParagraph.get("sections_id");
      String sectionSql = "select extend,template,id from Section where id=" + sectionId;
      Cursor sectionCursor = database.rawQuery(sectionSql, null);
      Map<String, Object> section = getCursorMap(sectionCursor);
      String extend = (String) section.get("extend");
      String template = (String) section.get("template");
      //如果节的template字段为extend值，说明该节是拓展节
      if (TextUtils.equals("extend", template)) {
        extendMap.put(extend, sectionId);
      }
    }
    return extendMap;
  }

  /**
   * 段落下的图片，音频特效内容
   * @param mediaBlockList mediaBlockList
   * @param database database
   * @param basePath basePath
   * @return
   */
  private static List<Map<String,Object>> getMediaBlock(List<Map<String,Object>> mediaBlockList,Database database,String basePath){
    for(Map<String,Object> mediaBlock : mediaBlockList){
      String f_order = (String) mediaBlock.get("f_order");
      mediaBlock.remove("f_order");
      mediaBlock.put("order",f_order);
      String mediaId = (String) mediaBlock.get("id");
      String mediasSql = "select * from Medias where mediaBlock_id="+mediaId+";";
      Cursor cursor = database.rawQuery(mediasSql,null);
      List<Map<String,Object>> mediasList = getCursorMapList(cursor);
      for(Map<String,Object> media : mediasList){
        //图片需要添加本地路径
        String abb = basePath + media.get("abbreviation");
        media.put("abbreviation",abb);
        String original = (String) media.get("original");
        if (!TextUtils.isEmpty(original)){
          original = basePath+original;
        }
        media.put("original",original);
        //查询本地视频路径
        String mediaType =(String) media.get("mediaType");
        String id =(String) media.get("id");
        switch (mediaType){ // TODO: 2017/7/20
          case "COMBINATION"://叠加图片
            String combinationItemSql = "select * from CombinationItem where mediaCombination_id="+id;
            Cursor combinationCursor = database.rawQuery(combinationItemSql,null);
            List<Map<String,Object>> combinationItems = getCursorMapList(combinationCursor);
            for(Map<String,Object> item : combinationItems){
              String picture = (String) item.get("picture");
              if (!TextUtils.isEmpty(picture)){
                picture = basePath+picture;
              }
              item.put("picture",picture);
            }
            media.put("combinationItems",combinationItems);
            break;
          case "VIDEO"://视频
            String mediaVideoSql = "select * from MediaVideoItem where videoCombination_id="+id;
            Cursor mediaVideoCursor = database.rawQuery(mediaVideoSql,null);
            List<Map<String,Object>> mediaVideo = getCursorMapList(mediaVideoCursor);
            for(Map<String,Object> video : mediaVideo){
              String videoPath = (String) video.get("path");
              String name = (String) video.get("name");
              video.put("path",AppConfig.BOOK_ONLINE_READ_URL+videoPath);
              if(name == null){
                video.put("name","");
              }
              String abbreviation = (String)video.get("abbreviation");
              if (!TextUtils.isEmpty(abbreviation)){
                abbreviation = basePath+abbreviation;
                video.put("abbreviation",abbreviation);
              }else{
                video.put("abbreviation","");
              }
            }
            media.put("mediaVideoItems",mediaVideo);
            break;
          case "SEQUENCE"://图像序列
            String mediaSequenceSql = "select * from MediaSequence where id="+id;
            Cursor mediaSequenceCursor = database.rawQuery(mediaSequenceSql,null);
            Map<String,Object> mediaSequence = getCursorMap(mediaSequenceCursor);
            String mediaSequenceId = (String) mediaSequence.get("id");
            String sequencePicSql = "select * from MediaSequence_pictures where MediaSequence_id="+mediaSequenceId;
            Cursor sequencePicCursor = database.rawQuery(sequencePicSql,null);
            List<Map<String,Object>> pictures = getCursorMapList(sequencePicCursor);
            List<String> pics = new ArrayList<>();
            for(Map<String,Object> pic : pictures){
              String image = (String) pic.get("pictures");
              if (!TextUtils.isEmpty(image)){
                image = basePath+image;
              }
              pics.add(image);
            }
            mediaSequence.put("pictures",pics);
            String pattern = (String) mediaSequence.get("pattern");
            if (!TextUtils.isEmpty(pattern)){
              pattern = basePath+pattern;
            }
            mediaSequence.put("pattern",pattern);
            media.putAll(mediaSequence);
            break;
          case "PPT"://幻灯片
            String mediaPPTSql = "select * from MediaPPTItem where pptCombination_id="+id;
            Cursor mediaPPTCursor = database.rawQuery(mediaPPTSql,null);
            List<Map<String,Object>> pptItems = getCursorMapList(mediaPPTCursor);
            for(Map<String,Object> pptItem : pptItems){
              String pptPath = (String) pptItem.get("path");
              if (!TextUtils.isEmpty(pptPath)){
                pptPath = basePath + pptPath;
              }
              pptItem.put("path",pptPath);
            }
            media.put("mediaPPTItems",pptItems);
            break;
          case "STATIC_SEQUENCE"://静态对象动画
            String sequencePicturesSql = "select * from MediaStaticSequence_pictures where MediaStaticSequence_id="+id;
            Cursor sequencePicturesCursor = database.rawQuery(sequencePicturesSql,null);
            List<Map<String,Object>> mediaSequencePictures = getCursorMapList(sequencePicturesCursor);
            List<String> staticPictures = new ArrayList<>();
            for(Map<String,Object>  p : mediaSequencePictures){
              String sequencePic = (String) p.get("pictures");
              if (!TextUtils.isEmpty(sequencePic)){
                sequencePic = basePath + sequencePic;
              }
              staticPictures.add(sequencePic);
            }
            media.put("pictures",staticPictures);
            break;
          case "PICTURE":
            //小图点大图

            break;
        }
      }
      mediaBlock.put("pictures",mediasList);
    }
    return mediaBlockList;
  }

  /**
   * 书籍名称英文转中文
   */
  private static String bookNameEC(String english){
    BookName bookName = BookName.valueOf(english);
    return bookName.chinese;
  }

  private static <T> void sendLocalBookServiceResult(String command,T object,Handler handler,String sn){
    Map<String,Object> result = new ArrayMap<>();
    Map<String,Object> serviceResult = new ArrayMap<>();
    serviceResult.put("result",object);
    serviceResult.put("flag",true);
    result.put("serviceResult",serviceResult);
    result.put("errorMessage","");
    result.put("opFlag",true);
    result.put("timestamp","");
    Message msg = Message.obtain();
    Result resultObj = new Result();
    resultObj.setCommand(command);
    resultObj.setSuccess(true);
    resultObj.setResponse("");
    resultObj.setSn(sn);
    resultObj.setData(result);
    msg.obj = resultObj;
    handler.sendMessage(msg);
    GreenDaoHelper.closeLocalBookDatabase();
  }

  private static  List<Map<String,Object>> getCursorMapList(Cursor cursor){
    List<Map<String,Object>> list = new ArrayList<>();
    while (cursor.moveToNext()){
      Map<String,Object> map = new ArrayMap<>();
      String[] columnNames = cursor.getColumnNames();
      for(int i=0; i<columnNames.length; i++){
        String name = columnNames[i];
        String value = cursor.getString(i);
        map.put(name,value);
      }
//      for(String name : columnNames){
//        String value = cursor.getString(cursor.getColumnIndex(name));
//        map.put(name,value);
//      }
      list.add(map);
    }
    cursor.close();
    return list;
  }

  public static Map<String,Object> getCursorMap(Cursor cursor){
    Map<String,Object> map = new ArrayMap<>();
    if(cursor.moveToNext()){
      String[] columnNames = cursor.getColumnNames();
      for(int i=0; i<columnNames.length; i++){
        String name = columnNames[i];
        String value = cursor.getString(i);
        map.put(name,value);
      }
//      for(String name : columnNames){
//        String value = cursor.getString(cursor.getColumnIndex(name));
//        map.put(name,value);
//      }
    }
    cursor.close();
    return map;
  }


}
