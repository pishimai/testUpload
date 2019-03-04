package com.mvw.medicalvisualteaching.utils;

import static com.mvw.medicalvisualteaching.utils.DataUtil.sendLocalBookServiceResult;
import static com.mvw.medicalvisualteaching.utils.DataUtil.sendServiceResult;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.mvw.medicalvisualteaching.bean.Book;
import com.mvw.medicalvisualteaching.bean.offlinebook.Author;
import com.mvw.medicalvisualteaching.bean.offlinebook.AuthorItem;
import com.mvw.medicalvisualteaching.bean.offlinebook.AuthorItemContents;
import com.mvw.medicalvisualteaching.bean.offlinebook.AuthorTitle;
import com.mvw.medicalvisualteaching.bean.offlinebook.Chapter;
import com.mvw.medicalvisualteaching.bean.offlinebook.Colleges;
import com.mvw.medicalvisualteaching.bean.offlinebook.CombinationItem;
import com.mvw.medicalvisualteaching.bean.offlinebook.KeyWord;
import com.mvw.medicalvisualteaching.bean.offlinebook.LocalBook;
import com.mvw.medicalvisualteaching.bean.offlinebook.MediaBlock;
import com.mvw.medicalvisualteaching.bean.offlinebook.MediaPPTItem;
import com.mvw.medicalvisualteaching.bean.offlinebook.MediaSequence;
import com.mvw.medicalvisualteaching.bean.offlinebook.MediaSequencePictures;
import com.mvw.medicalvisualteaching.bean.offlinebook.MediaStaticSequencePictures;
import com.mvw.medicalvisualteaching.bean.offlinebook.MediaVideo;
import com.mvw.medicalvisualteaching.bean.offlinebook.Medias;
import com.mvw.medicalvisualteaching.bean.offlinebook.Paragraph;
import com.mvw.medicalvisualteaching.bean.offlinebook.ParagraphAuthor;
import com.mvw.medicalvisualteaching.bean.offlinebook.Section;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionAcknowledgements;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionBackCover;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionCaseSection;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionCasesItem;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionCasesItemAuthors;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionCover;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionDocument;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionDocumentItem;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionImprint;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionImprintEditor;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionLearningCenter;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionMainPoints;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionNameList;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionParagraph;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionPreface;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionReference;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionShorthand;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionSummary;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionSummaryChinese;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionSummaryEnglish;
import com.mvw.medicalvisualteaching.bean.offlinebook.SectionTranslate;
import com.mvw.medicalvisualteaching.bean.offlinebook.TDView;
import com.mvw.medicalvisualteaching.bean.offlinebook.TRView;
import com.mvw.medicalvisualteaching.bean.offlinebook.TableView;
import com.mvw.medicalvisualteaching.config.AppConfig;
import com.mvw.medicalvisualteaching.config.Constant;
import com.mvw.medicalvisualteaching.db.GreenDaoHelper;
import com.mvw.medicalvisualteaching.db.dao.AuthorDao;
import com.mvw.medicalvisualteaching.db.dao.AuthorItemContentsDao;
import com.mvw.medicalvisualteaching.db.dao.AuthorItemDao;
import com.mvw.medicalvisualteaching.db.dao.AuthorTitleDao;
import com.mvw.medicalvisualteaching.db.dao.ChapterDao;
import com.mvw.medicalvisualteaching.db.dao.CollegesDao;
import com.mvw.medicalvisualteaching.db.dao.CombinationItemDao;
import com.mvw.medicalvisualteaching.db.dao.CommitteeDao;
import com.mvw.medicalvisualteaching.db.dao.EditorsDao;
import com.mvw.medicalvisualteaching.db.dao.KeyWordDao;
import com.mvw.medicalvisualteaching.db.dao.LocalBookDao;
import com.mvw.medicalvisualteaching.db.dao.MediaBlockDao;
import com.mvw.medicalvisualteaching.db.dao.MediaPPTItemDao;
import com.mvw.medicalvisualteaching.db.dao.MediaSequenceDao;
import com.mvw.medicalvisualteaching.db.dao.MediaSequencePicturesDao;
import com.mvw.medicalvisualteaching.db.dao.MediaSequencePicturesDao.Properties;
import com.mvw.medicalvisualteaching.db.dao.MediaStaticSequencePicturesDao;
import com.mvw.medicalvisualteaching.db.dao.MediaVideoDao;
import com.mvw.medicalvisualteaching.db.dao.MediasDao;
import com.mvw.medicalvisualteaching.db.dao.ParagraphAuthorDao;
import com.mvw.medicalvisualteaching.db.dao.ParagraphDao;
import com.mvw.medicalvisualteaching.db.dao.SectionAcknowledgementsDao;
import com.mvw.medicalvisualteaching.db.dao.SectionBackCoverDao;
import com.mvw.medicalvisualteaching.db.dao.SectionCaseSectionDao;
import com.mvw.medicalvisualteaching.db.dao.SectionCasesItemAuthorsDao;
import com.mvw.medicalvisualteaching.db.dao.SectionCasesItemDao;
import com.mvw.medicalvisualteaching.db.dao.SectionCoverDao;
import com.mvw.medicalvisualteaching.db.dao.SectionDao;
import com.mvw.medicalvisualteaching.db.dao.SectionDocumentDao;
import com.mvw.medicalvisualteaching.db.dao.SectionDocumentItemDao;
import com.mvw.medicalvisualteaching.db.dao.SectionImprintDao;
import com.mvw.medicalvisualteaching.db.dao.SectionImprintEditorDao;
import com.mvw.medicalvisualteaching.db.dao.SectionLearningCenterDao;
import com.mvw.medicalvisualteaching.db.dao.SectionMainPointsDao;
import com.mvw.medicalvisualteaching.db.dao.SectionNameListDao;
import com.mvw.medicalvisualteaching.db.dao.SectionParagraphDao;
import com.mvw.medicalvisualteaching.db.dao.SectionPrefaceDao;
import com.mvw.medicalvisualteaching.db.dao.SectionReferenceDao;
import com.mvw.medicalvisualteaching.db.dao.SectionShorthandDao;
import com.mvw.medicalvisualteaching.db.dao.SectionSummaryChineseDao;
import com.mvw.medicalvisualteaching.db.dao.SectionSummaryDao;
import com.mvw.medicalvisualteaching.db.dao.SectionSummaryEnglishDao;
import com.mvw.medicalvisualteaching.db.dao.SectionTranslateDao;
import com.mvw.medicalvisualteaching.db.dao.TDViewDao;
import com.mvw.medicalvisualteaching.db.dao.TRViewDao;
import com.mvw.medicalvisualteaching.db.dao.TableViewDao;
import com.orhanobut.logger.Logger;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;

/**
 * 离线书籍内容
 * Created by zhaomengen on 2017/4/18.
 */

public class LocalBookUtil {

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
    long start = System.currentTimeMillis();
//    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/localbook.db";
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"69066658a16532b1";
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn();
    Logger.i(path);
    Logger.i(basePath);
//    path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/Local_Book.db";
//    path = context.getExternalFilesDir(null).getPath()+File.separator+"sqllite-032017042014.db";
//    path = context.getExternalFilesDir(null).getPath()+File.separator+"localbook.db";
    File file = new File(path);
    if(!file.exists()){
      //库文件不存在
      sendServiceResult(true,false, Constant.GET_BOOK_CATELOG,"这本书已经丢失或损坏，请重新安装",null,handler,sn);
      return;
    }
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    LocalBookDao localBookDao = GreenDaoHelper.getDaoSessionBook().getLocalBookDao();
    LocalBook bookResult = localBookDao.queryBuilder().unique();
    ChapterDao chapterDao = GreenDaoHelper.getDaoSessionBook().getChapterDao();
    List<Chapter> chapters = chapterDao.queryBuilder().orderAsc(ChapterDao.Properties.Order).list();
    Database chapterDatabase = chapterDao.getDatabase();

    for(Chapter chapter : chapters){
      String iconPath = basePath+chapter.getIcon();
      chapter.setIcon(iconPath);
      String sectionSql = "select id,name,sectionId from Section where chapter_id='"+chapter.getId()+"' order by f_order asc";
      Cursor sectionCursor = chapterDatabase.rawQuery(sectionSql,null);
      List<Map<String,Object>> sections = new ArrayList<>();
      while (sectionCursor.moveToNext()){
        String id = sectionCursor.getString(sectionCursor.getColumnIndex("id"));
        String name = sectionCursor.getString(sectionCursor.getColumnIndex("name"));
        String sectionId = sectionCursor.getString(sectionCursor.getColumnIndex("sectionId"));
        Map<String,Object> section = new ArrayMap<>();
        section.put("id",id);
        section.put("name",name);
        section.put("sectionId",sectionId);
        sections.add(section);
      }
      sectionCursor.close();
      chapter.setSections(sections);
//      chapter.getSections();
    }

    bookResult.setChapters(chapters);
//    bookResult.setCover("");
//    GreenDaoHelper.getDaoSessionBook().clear();//清空关闭
//    GreenDaoHelper.closeLocalBookDatabase();
//    Gson gson = new GsonBuilder().serializeNulls().create();

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
    new Thread(){
      @Override
      public void run() {
        try {
          long start = System.currentTimeMillis();
          getParagraphContent(localBook,context,sectionId,handler,sn);
          Logger.i("节 ： "+(System.currentTimeMillis() - start));
          System.gc();

        } catch (JSONException | UnsupportedEncodingException | JsonProcessingException e) {
          Map<String,Object> result = new ArrayMap<>();
          result.put("serviceResult","");
          result.put("errorMessage","这本书已经丢失或损坏，请重新安装");
          result.put("opFlag",true);
          result.put("timestamp","");
          Gson gson = new Gson();
          String response = gson.toJson(result);
          sendServiceResult(true,Constant.GET_BOOK_CHAPTER_LOCAL,response,handler,sn);
          e.printStackTrace();
        }
      }
    }.start();
  }

  public static void getParagraphContent(Book localBook,Context context,String sectionId,Handler handler,String sn)
      throws JSONException, UnsupportedEncodingException, JsonProcessingException {
//    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/localbook.db";
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"69066658a16532b1";
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn();
//    path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/Local_Book.db";
//    path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"localbook.db";
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    //获取节的数据
    SectionDao sectionDao = GreenDaoHelper.getDaoSessionBook().getSectionDao();
    Query<Section> sectionQuery = sectionDao.queryBuilder().where(SectionDao.Properties.Id.eq(null)).build();
    Section section = sectionQuery.setParameter(0,sectionId).unique();
    if(section == null){
      throw new JSONException("");
    }
    long start = System.currentTimeMillis();
    //获取内容
    List<Paragraph> paragraphs = getParagraphContentList(section,basePath);
    section.setSection(paragraphs);

    //获取中英文对照索引
    Map<String,Object> sectionMap = new ArrayMap<>();
    sectionMap.put("id",section.getId());
    sectionMap.put("template",section.getTemplate());
//    sectionMap.put("chapterId",section.getChapterId());
    sectionMap.put("contents",section.getContents());
    sectionMap.put("extend",section.getExtend());
    sectionMap.put("name",section.getName());
    sectionMap.put("order",section.getOrder());
    sectionMap.put("section",section.getSection());
    sectionMap.put("sectionId",section.getSectionId());
    String topImage = basePath + section.getTopImage();
    sectionMap.put("topImage",topImage);
    //作者
//    Map<String,Object> authors = getSectionAuthor(section);
//    sectionMap.putAll(authors);
    //设置其他
    Map<String,Object> object = getTypeObject(section,localBook,basePath);
    sectionMap.putAll(object);
//    Logger.i(" 查询耗时 ："+(System.currentTimeMillis() - start));
//    GreenDaoHelper.getDaoSessionBook().clear();//清空关闭
//    GreenDaoHelper.closeLocalBookDatabase();
    DataUtil.sendLocalBookServiceResult(Constant.GET_BOOK_CHAPTER_LOCAL,sectionMap,handler,sn);
    Logger.i(" 最终Json耗时："+(System.currentTimeMillis() - start));
    //通知html
//    sendServiceResult(true,Constant.GET_BOOK_CHAPTER_LOCAL,response,handler,sn);
  }

  public static void getTableText(Book localBook,Context context,String mediaId,Handler handler,String sn){
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/localbook.db";
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    ParagraphDao paragraphDao = GreenDaoHelper.getDaoSessionBook().getParagraphDao();
    Paragraph paragraph =paragraphDao.queryBuilder().where(ParagraphDao.Properties.Id.eq(mediaId)).unique();
    DataUtil.sendLocalBookServiceResult(Constant.GET_BOOK_MEDIA_LOCAL,paragraph.getText(),handler,sn);
  }

  /**
   *  获取特效数据
   */
  public static void getBookMedia(Book localBook,Context context,String mediaId,Handler handler,String sn)
      throws UnsupportedEncodingException {
//    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/localbook.db";
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"69066658a16532b1";
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    MediasDao mediasDao = GreenDaoHelper.getDaoSessionBook().getMediasDao();
    Database database = mediasDao.getDatabase();
    String sql = "select id,original,mediaType,title from Medias where id="+mediaId;
    Cursor cursor = database.rawQuery(sql,null);
    Medias medias = new Medias();
    while (cursor.moveToNext()){
      medias.setId(cursor.getString(cursor.getColumnIndex("id")));
      String original = basePath + cursor.getString(cursor.getColumnIndex("original"));
      medias.setOriginal(original);
      medias.setMediaType(cursor.getString(cursor.getColumnIndex("mediaType")));
      medias.setTitle(cursor.getString(cursor.getColumnIndex("title")));
    }
    cursor.close();
//    Medias medias = mediasDao.queryBuilder().where(MediasDao.Properties.Id.eq(mediaId)).unique();
    Map<String,Object> mediaMap = new ArrayMap<>();
    switch (medias.getMediaType()){
      case "COMBINATION"://叠加图片
        CombinationItemDao combinationItemDao = GreenDaoHelper.getDaoSessionBook().getCombinationItemDao();
        List<CombinationItem> combinationItems = combinationItemDao.queryBuilder().where(CombinationItemDao.Properties.MediaCombination_id.eq(medias.getId())).list();
        for(CombinationItem item : combinationItems){
          String pic = basePath+item.getPicture();
          item.setPicture(pic);
        }
        medias.setCombinationItems(combinationItems);
        break;
      case "VIDEO"://视频
        MediaVideoDao mediaVideoDao = GreenDaoHelper.getDaoSessionBook().getMediaVideoDao();
        List<MediaVideo> mediaVideo = mediaVideoDao.queryBuilder().where(MediaVideoDao.Properties.VideoCombination_id.eq(medias.getId())).list();
        for(MediaVideo video : mediaVideo){
          String videoPath = video.getPath();
          video.setPath(AppConfig.BOOK_ONLINE_READ_URL+videoPath);
          video.setAbbreviation(basePath+video.getAbbreviation());
          if(video.getName() == null){
            video.setName("");
          }
        }
        medias.setMediaVideoItems(mediaVideo);
        break;
      case "SEQUENCE"://图像序列
        MediaSequenceDao mediaSequenceDao = GreenDaoHelper.getDaoSessionBook().getMediaSequenceDao();
        MediaSequence mediaSequence = mediaSequenceDao.queryBuilder().where(MediaSequenceDao.Properties.Id.eq(medias.getId())).unique();
        MediaSequencePicturesDao sequencePicturesDao = GreenDaoHelper.getDaoSessionBook().getMediaSequencePicturesDao();
        List<MediaSequencePictures> sequencePictures = sequencePicturesDao.queryBuilder().where(Properties.MediaSequence_id.eq(mediaSequence.getId())).list();
        List<String> sequencePictureList = new ArrayList<>();
        for(MediaSequencePictures picture : sequencePictures){
          String pic = basePath + picture.getPictures();
          sequencePictureList.add(pic);
        }
        mediaSequence.setPictures(sequencePictureList);
        mediaMap.put("count",mediaSequence.getCount());
        String pattern = basePath+mediaSequence.getPattern();
        mediaMap.put("pattern",pattern);
//        mediaMap.put("pattern",mediaSequence.getPattern());
        if(mediaSequence.getReverse() == 1){
          mediaMap.put("reverse",true);
        }else {
          mediaMap.put("reverse",false);
        }
        mediaMap.put("step",mediaSequence.getStep());
        mediaMap.put("type",mediaSequence.getType());
        mediaMap.put("pictures",mediaSequence.getPictures());
        break;
      case "PPT"://幻灯片
        MediaPPTItemDao pptItemDao = GreenDaoHelper.getDaoSessionBook().getMediaPPTItemDao();
        List<MediaPPTItem> pptItems = pptItemDao.queryBuilder().where(MediaPPTItemDao.Properties.PptCombination_id.eq(medias.getId())).list();
        for(MediaPPTItem pptItem : pptItems){
          String pptPath = basePath + pptItem.getPath();
          pptItem.setPath(pptPath);
        }
        medias.setMediaPPTItems(pptItems);
        break;
      case "STATIC_SEQUENCE"://静态对象动画
        MediaStaticSequencePicturesDao staticSequencePicturesDao = GreenDaoHelper.getDaoSessionBook().getMediaStaticSequencePicturesDao();
        List<MediaStaticSequencePictures> mediaSequencePictures = staticSequencePicturesDao.queryBuilder().where(MediaStaticSequencePicturesDao.Properties.MediaStaticSequence_id.eq(medias.getId())).list();
        List<String> pictures = new ArrayList<>();
        for(MediaStaticSequencePictures  p : mediaSequencePictures){
          String sequencePic = basePath+p.getPictures();
//          String sequencePic = p.getPictures();
          pictures.add(sequencePic);
        }
        mediaMap.put("pictures",pictures);
        break;
      case "PICTURE":
        //小图点大图

        break;
    }
    mediaMap.put("id",medias.getId());
    String abbreviation = basePath + medias.getAbbreviation();
    mediaMap.put("abbreviation",abbreviation);
    mediaMap.put("combinationItems",medias.getCombinationItems());
    mediaMap.put("mediaPPTItems",medias.getMediaPPTItems());
    mediaMap.put("mediaType",medias.getMediaType());
    mediaMap.put("mediaVideoItems",medias.getMediaVideoItems());
    mediaMap.put("original",medias.getOriginal());
    mediaMap.put("title",medias.getTitle());

    database.close();
    GreenDaoHelper.getDaoSessionBook().clear();
    GreenDaoHelper.closeLocalBookDatabase();

    sendLocalBookServiceResult(Constant.GET_BOOK_MEDIA_LOCAL,mediaMap,handler,sn);
  }

  /**
   * 获取作者简介
   */
  public static void getAuthorInfo(Book localBook,Context context,String authorId,Handler handler,String sn){
//    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"db_audio/localbook.db";
    String path = localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator+"69066658a16532b1";
    String basePath = "file://"+localBook.getDownloadPath() + File.separator + localBook.getIsbn()+File.separator;
    GreenDaoHelper.initLocalBookDatabasePassword(context,path,"");
    //作者
    AuthorDao authorDao = GreenDaoHelper.getDaoSessionBook().getAuthorDao();
    Author author = authorDao.queryBuilder().where(AuthorDao.Properties.Id.eq(authorId)).unique();
    author.setPhoto(basePath + author.getPhoto());
    //作者头衔
    AuthorTitleDao authorTitleDao = GreenDaoHelper.getDaoSessionBook().getAuthorTitleDao();
    List<AuthorTitle> authorTitles = authorTitleDao.queryBuilder().where(AuthorTitleDao.Properties.Author_id.eq(author.getId())).list();
    List<String> titles = new ArrayList<>();
    for(AuthorTitle authorTitle : authorTitles){
      titles.add(authorTitle.getTitle());
    }
    author.setTitle(titles);
    //作者介绍标题
    AuthorItemDao authorItemDao = GreenDaoHelper.getDaoSessionBook().getAuthorItemDao();
    List<AuthorItem> authorItems = authorItemDao.queryBuilder().where(AuthorItemDao.Properties.Author_id.eq(author.getId())).list();
    AuthorItemContentsDao authorItemContentsDao = GreenDaoHelper.getDaoSessionBook().getAuthorItemContentsDao();
    QueryBuilder<AuthorItemContents> queryBuilder = authorItemContentsDao.queryBuilder();
    queryBuilder.where(AuthorItemContentsDao.Properties.AuthorItem_id.eq(null));
    Query<AuthorItemContents> queryContents = queryBuilder.build();

    QueryBuilder<AuthorItem> queryBuilderParent = authorItemDao.queryBuilder();
    queryBuilderParent.where(AuthorItemDao.Properties.Parent_id.eq(null));
    Query<AuthorItem> queryParent = queryBuilderParent.build();
    //查询每个标题下的介绍内容
    for (AuthorItem authorItem : authorItems){
      queryContents.setParameter(0,authorItem.getId());
      List<AuthorItemContents> contentsList = queryContents.list();
      List<String> contents = new ArrayList<>();
      for(AuthorItemContents authorItemContents : contentsList){
        contents.add(authorItemContents.getContents());
      }
      authorItem.setContents(contents);
      //子简介内容
      List<AuthorItem> children = getAuthorChildren(authorItem.getId(),queryParent,queryContents);
      authorItem.setChildren(children);
    }
    author.setAuthorItems(authorItems);
    sendLocalBookServiceResult(Constant.GET_AUTHOR_PROFILE_LOCAL,author,handler,sn);
  }

  private static List<AuthorItem> getAuthorChildren(String parentId,Query<AuthorItem> queryParent,Query<AuthorItemContents> queryContents){
    queryParent.setParameter(0,parentId);
    List<AuthorItem> authorItems = queryParent.list();
    for (AuthorItem authorItem : authorItems){
      queryContents.setParameter(0,authorItem.getId());
      List<AuthorItemContents> contentsList = queryContents.list();
      List<String> contents = new ArrayList<>();
      for(AuthorItemContents authorItemContents : contentsList){
        contents.add(authorItemContents.getContents());
      }
      authorItem.setContents(contents);
      //子简介内容
      if(authorItem.getParent_id() != null && !TextUtils.equals("",authorItem.getParent_id())){
        List<AuthorItem> children = getAuthorChildren(authorItem.getId(),queryParent,queryContents);
        authorItem.setChildren(children);
      }
    }
    return authorItems;
  }

  /**
   * 节的作者
   */
  private static Map<String,Object> getParagraphAuthor(Paragraph paragraph){
    ParagraphAuthorDao authorDao = GreenDaoHelper.getDaoSessionBook().getParagraphAuthorDao();
    List<ParagraphAuthor> sectionAuthors = authorDao.queryBuilder().where(ParagraphAuthorDao.Properties.Paragraph_id.eq(paragraph.getId())).list();
    Database authorDatabase = authorDao.getDatabase();
    List<Map<String,Object>> authors = new ArrayList<>();
    List<String> authorTitles = new ArrayList<>();
    Map<String,Object> result = new ArrayMap<>();
    for(ParagraphAuthor sectionAuthor : sectionAuthors){
      String authorSql = "select id,name,isExtend from Author where id ="+sectionAuthor.getAuthors_id();
      Cursor authorCursor = authorDatabase.rawQuery(authorSql,null);
      Map<String,Object> authorMap = new ArrayMap<>();
      while (authorCursor.moveToNext()){
        String id = authorCursor.getString(authorCursor.getColumnIndex("id"));
        String name = authorCursor.getString(authorCursor.getColumnIndex("name"));
        String isExtend = authorCursor.getString(authorCursor.getColumnIndex("isExtend"));
        authorMap.put("id",id);
        authorMap.put("name",name);
        if(isExtend != null && "1".equals(isExtend)){
          authorMap.put("extend",true);
        }else {
          authorMap.put("extend",false);
        }

      }
      authorCursor.close();
      authors.add(authorMap);
      String authorTitleSql = "select title from Author_title where Author_id ="+sectionAuthor.getAuthors_id();
      Cursor authorTitleCursor = authorDatabase.rawQuery(authorTitleSql,null);
      while (authorTitleCursor.moveToNext()){
        String title = authorTitleCursor.getString(authorTitleCursor.getColumnIndex("title"));
        authorTitles.add(title);
      }
      authorTitleCursor.close();
    }
    result.put("titles",authorTitles);
    result.put("authors",authors);
    return result;
  }

  private static Map<String,Object> getTypeObject(Section section,Book localBook,String basePath) throws JSONException {
    Map<String,Object> result = new ArrayMap<>();
    switch (section.getDtype()){
      case "main_points":
        //要点
        SectionMainPointsDao mainPointsDao = GreenDaoHelper.getDaoSessionBook().getSectionMainPointsDao();
        SectionMainPoints mainPoints = mainPointsDao.queryBuilder().where(SectionMainPointsDao.Properties.Id.eq(section.getId())).unique();
        result.put("id",mainPoints.getId());
        result.put("chapterId",mainPoints.getChapterId());
        result.put("chapterName",mainPoints.getChapterName());
        result.put("piece",mainPoints.getPiece());
        result.put("pieceId",mainPoints.getPieceId());
        break;
      case "cover":
        //封面
        SectionCoverDao coverDao = GreenDaoHelper.getDaoSessionBook().getSectionCoverDao();
        SectionCover cover = coverDao.queryBuilder().where(SectionCoverDao.Properties.Id.eq(section.getId())).unique();
        result.put("id",cover.getId());
        result.put("backgroundColor",cover.getBackgroundColor());
        String coverImage = basePath + cover.getPicture();
        result.put("picture",coverImage);
        break;
      case "flyleaf":
        //扉页
        EditorsDao editorsDao = GreenDaoHelper.getDaoSessionBook().getEditorsDao();
        Database database = editorsDao.getDatabase();
        Cursor bookHtmlCursor = database.rawQuery("select subjects from BookHTML",null);
        String flyleafSubject = "";
        while (bookHtmlCursor.moveToNext()){
          flyleafSubject = bookHtmlCursor.getString(bookHtmlCursor.getColumnIndex("subjects"));
        }
        bookHtmlCursor.close();
        String flyleafSql = "SELECT *\n"
            + "        FROM Editors e\n"
            + "        WHERE subject= '"+flyleafSubject+
            "' and e.dutyType IN ('CHAIRMAN', 'CHIEF_EDITOR', 'ASSOCIATE_EDITOR','COMMON_EDITORS','SECRETARY')\n"
            + "        ORDER BY e.subject, e.dutyType";
        Cursor flyleafCursor = database.rawQuery(flyleafSql,null);
        List<Map<String,Object>> flyleafSqlAssociateEditor = new ArrayList<>();
        List<Map<String,Object>> flyleafSqlChairman = new ArrayList<>();
        List<Map<String,Object>> flyleafChiefEditor = new ArrayList<>();
        List<Map<String,Object>> flyleafCommonEditor = new ArrayList<>();
        List<Map<String,Object>> flyleafSecretary = new ArrayList<>();
        Map<String,Object> flyleafContentMap = new ArrayMap<>();
        while (flyleafCursor.moveToNext()){
          String dutyType = flyleafCursor.getString(flyleafCursor.getColumnIndex("dutyType"));
          String name = flyleafCursor.getString(flyleafCursor.getColumnIndex("name"));
          String college = flyleafCursor.getString(flyleafCursor.getColumnIndex("college"));
          Map<String,Object> map = new ArrayMap<>();
          map.put("name",name);
          map.put("college",college);
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
        result.put("contents",flyleafContentMap);
        break;
      case "imprint":
        //版权页
        SectionImprintDao imprintDao = GreenDaoHelper.getDaoSessionBook().getSectionImprintDao();
        SectionImprint imprint = imprintDao.queryBuilder().where(SectionImprintDao.Properties.Id.eq(section.getId())).unique();
        if(imprint != null){
          SectionImprintEditorDao imprintEditorDao = GreenDaoHelper.getDaoSessionBook().getSectionImprintEditorDao();
          List<SectionImprintEditor> imprintEditors = imprintEditorDao.queryBuilder().where(SectionImprintEditorDao.Properties.Imprint_id.eq(imprint.getId())).list();
          List<String> list = new ArrayList<>();
          for(SectionImprintEditor editor : imprintEditors){
            list.add(editor.getEditors());
          }
          imprint.setEditors(list);
          result.put("id",imprint.getId());
          result.put("count",imprint.getCount());
          result.put("distributing",imprint.getDistributing());
          result.put("distributingAddress",imprint.getDistributingAddress());
          result.put("distributingDate",imprint.getDistributingDate());
          result.put("distributingPostcode",imprint.getDistributingPostcode());
          result.put("editors",imprint.getEditors());
          result.put("email",imprint.getEmail());
          result.put("hotline",imprint.getHotline());
          result.put("isbn",imprint.getIsbn());
          result.put("operator",imprint.getOperator());
          result.put("phone",imprint.getPhone());
          result.put("price",imprint.getPrice());
          result.put("producer",imprint.getProducer());
          result.put("producerAddress",imprint.getProducerAddress());
          result.put("publish",imprint.getPublish());
          result.put("publishAddress",imprint.getPublishAddress());
          result.put("publishPostcode",imprint.getPublishPostcode());
          String subject = bookNameEC(imprint.getSubjectName());
          if(subject == null){
            subject = imprint.getSubjectName();
          }
          result.put("subjectName",subject);
          result.put("version",imprint.getVersion());
        }
        break;
      case "preface":
        //序
        SectionPrefaceDao prefaceDao = GreenDaoHelper.getDaoSessionBook().getSectionPrefaceDao();
        SectionPreface preface = prefaceDao.queryBuilder().where(SectionPrefaceDao.Properties.Id.eq(section.getId())).unique();
//        SectionPrefaceTitleDao prefaceTitleDao = GreenDaoHelper.getDaoSessionBook().getSectionPrefaceTitleDao();
//        List<SectionPrefaceTitle> titles = prefaceTitleDao.queryBuilder().where(SectionPrefaceTitleDao.Properties.Preface_id.eq(preface.getId())).list();
//        List<String> titleList = new ArrayList<>();
//        for(SectionPrefaceTitle title : titles){
//          titleList.add(title.getTitles());
//        }
//        result.put("titles",titleList);
//        result.put("id",preface.getId());
//        String markImage = basePath + preface.getMark();
//        result.put("mark",markImage);
//        String mark1Image = basePath + preface.getMark1();
//        result.put("mark1",mark1Image);
        result.put("timestamp",preface.getTimestamp());
        break;
      case "cases":
        //病例分析
        SectionCaseSection caseSection = getCasesItemList(section);
        result.put("id",caseSection.getId());
        result.put("casesItemList",caseSection.getCasesItemList());
        break;
      case "summary":
        //小结
        SectionSummary summary = getSummary(section);
        result.put("id",summary.getId());
        result.put("audio",summary.getAudio());
        result.put("chinese",summary.getChinese());
        result.put("english",summary.getEnglish());
        break;
      case "document":
        //主要文献参考
        SectionDocument document = getDocument(section);
        result.put("id",document.getId());
        result.put("documents",document.getDocuments());
        break;
      case "acknowledgements":
        //致谢名单
        SectionAcknowledgements acknowledgements = getAcknowledgements(section);
        result.put("id",acknowledgements.getId());
        result.put("editors",acknowledgements.getEditors());
        result.put("thanks",acknowledgements.getThanks());
        break;
      case "backCover":
        //封底
        SectionBackCover backCover = getBackCover(section);
        result.put("id",backCover.getId());
        result.put("introduce",backCover.getIntroduce());
        String isbn = basePath + backCover.getIsbn();
        result.put("isbn",isbn);
        break;
      case "learningCenter":
        //学习园地
        SectionLearningCenter learningCenter = getLearningCenter(section,basePath);
//        result.put("id",learningCenter.getId());
        result.put("reference",learningCenter.getReference());
        result.put("shorthand",learningCenter.getShorthand());
        break;
      case "chineseToEnglish":
        //中英文名词对照索引
        Map<String,List<SectionTranslate>> translateCE = translate("CETranslate");
        result.put("contents",translateCE);
        break;
      case "englishToChinese":
        //英中文名词对照索引
        Map<String,List<SectionTranslate>> translateEC = translate("ECTranslate");
        result.put("contents",translateEC);
        break;
      case "committeeCollege":
        //委员院校单位
        CollegesDao collegesDao = GreenDaoHelper.getDaoSessionBook().getCollegesDao();
        List<Colleges> collegeList = collegesDao.queryBuilder().list();
        Map<String,Object> collegesMap = new ArrayMap<>();
        List<String> colleges = new ArrayList<>();
        for (Colleges college : collegeList){
          colleges.add(college.getName());
        }
        collegesMap.put("colleges",colleges);
        result.put("contents",collegesMap);
        break;
      case "catalog":
        //教材目录
        EditorsDao catalogEditorDao = GreenDaoHelper.getDaoSessionBook().getEditorsDao();
        Database catalogDatabase = catalogEditorDao.getDatabase();
        Cursor subjectCursor = catalogDatabase.rawQuery("select distinct(subject) from Editors",null);
        List<Map<String,Object>> catalogContents = new ArrayList<>();
        while (subjectCursor.moveToNext()){
          String catalogSubject = subjectCursor.getString(subjectCursor.getColumnIndex("subject"));
          String catalogSql = " SELECT *\n"
              + "        FROM Editors e\n"
              + "        WHERE subject='"+catalogSubject+"' and e.dutyType IN ('CHAIRMAN', 'CHIEF_EDITOR', 'ASSOCIATE_EDITOR')\n"
              + "        ORDER BY e.subject, e.dutyType";
          Cursor catalogCursor = catalogDatabase.rawQuery(catalogSql,null);
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
        result.put("contents",catalogContents);

        break;
      case "committeeConsultant":
        //顾问委员会
        CommitteeDao committeeDao = GreenDaoHelper.getDaoSessionBook().getCommitteeDao();
        Database committeeDatabase = committeeDao.getDatabase();
        String committeeSql = "SELECT DISTINCT\n"
            + "            committee.name,\n"
            + "            committee.title,\n"
            + "            committee.dutyTitle,\n"
            + "            committee.unit\n"
            + "        FROM Committee committee\n"
            + "        WHERE title IN ('CHIEF_CONSULTANT', 'CONSULTANT')\n"
            + "        ORDER BY title";
        Cursor committeeCursor = committeeDatabase.rawQuery(committeeSql,null);
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
        result.put("contents",committeeContents);
        break;
      case "committeeWriting":
        //编写委员会
        CommitteeDao writingDao = GreenDaoHelper.getDaoSessionBook().getCommitteeDao();
        Database writingDatabase = writingDao.getDatabase();
        String writingSql = "SELECT DISTINCT\n"
            + "            committee.name,\n"
            + "            committee.title,\n"
            + "            committee.dutyTitle,\n"
            + "            committee.unit\n"
            + "        FROM Committee committee\n"
            + "        WHERE title IN ('DIRECTOR', 'EXECUTIVE_DIRECTOR', 'SECRETARY_GENERAL', 'DEPUTY_DIRECTOR', 'COMMISSIONER')\n"
            + "        ORDER BY title";
        Cursor writingCursor = writingDatabase.rawQuery(writingSql,null);
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
        result.put("contents",writingMap);
        break;
    }
    return result;
  }

  public enum BookName {
    INTERNAL("0001", "内科学"), //
    CHIRURGERY("0002", "外科学"), //
    GYNECOTOKOLOGY("0003", "妇产科学"), //
    PEDIATRICS("0004", "儿科学"), //
    NEUROLOGY("0005", "神经病学"), //
    MEDICAL_IMAGE("0006", "医学影像学"), //
    CLINICAL_SKILLS("0007", "临床技能学"), //
    STOMATOLOGY("0008", "口腔科学"), //
    DIAGNOSTICS("0009", "诊断学"), //
    EMERGENCY_DISASTER("0010", "急诊与灾难医学"), //

    INFECTIOUS_DISEASE("0011", "感染病学"), //
    DERMATOLOGY("0012", "皮肤性病学"), //
    ALIENISM("0013", "精神病学"), //
    NUCLEAR("0014", "核医学"), //
    OPHTHALMOLOGY("0015", "眼科学"), //
    OTOLARYNGOLOGY("0016", "耳鼻咽喉头颈科学"), //
    TRADITIONAL_CHINESE("0017", "中医学"), //
    CLINICAL_PHARMACOLOGY("0018", "临床药理学"), //
    ANESTHESIOLOGY("0019", "麻醉学"), //
    REHABILITATION("0020", "康复医学"), //

    GENERAL_PRACTICE("0021", "全科医学概论"), //
    HYGIENE("0022", "卫生学"), //
    PREVENTIVE_MEDICINE("0023", "预防医学"), //
    CLINICAL_EPIDEMIOLOGY("0024", "临床流行病学"), //
    GERATOLOGY("0025", "老年医学"), //
    FORENSIC("0026", "法医学"), //
    DESCRIPTIVE_ANATOMY("0027", "系统解剖学"), //
    PHYSIOLOGY("0028", "生理学"), //
    PHARMACOLOGY("0029", "药理学"), //
    BIOCHEMISTRY_MOLECULAR_BIOLOGY("0030", "生物化学与分子生物学"),//

    PATHEMATOLOGY("0031", "病理学"), //
    HISTOLOGY_EMBRYOLOGY("0032", "组织学与胚胎学"), //
    REGIONAL_ANATOMY("0033", "局部解剖学"), //
    IMMUNOLOGY("0034", "医学免疫学"), //
    HUMAN_PARASITOLOGY("0035", "人体寄生虫学"), //
    MICROBIOLOGY("0036", "医学微生物学"), //
    MORBID_PHYSIOLOGY("0037", "病理生理学"), //
    MATHEMATICS("0038", "医用高等数学"), //
    CELL_BIOLOGY("0039", "医学细胞生物学"),//
    STATISTICS("0040", "医学统计学"), //

    CHEMISTRY("0041", "医学基础化学"), //
    ORGANIC_CHEMISTRY("0042", "医学有机化学"), //
    HODEGETICS("0043", "医学伦理学"), //
    PSYCHOLOGY("0044", "医学心理学"), //
    EPIDEMIOLOGY("0045", "流行病学"), //
    GENETICS("0046", "医学遗传学"), //
    PHYSICS("0047", "医学物理学"), //
    INTRODUCTION_MEDICINE("0048", "医学导论"), //
    LITERATURE_RETRIEVAL("0049", "医学文献检索"), //
    HEALTH_LAW("0050", "卫生法"), //
    COMPUTER("0051", "医学信息与医用计算机基础"), //
    ENGLISH("0052", "医学英语"); //
    private String num;
    public String chinese;

    BookName(String num,String chinese) {
      this.num = num;
      this.chinese = chinese;
    }

  }

  /**
   * 书籍名称英文转中文
   */
  private static String bookNameEC(String english){
    BookName bookName = BookName.valueOf(english);
    return bookName.chinese;
  }

  private static  SectionLearningCenter getLearningCenter(Section section,String basePath) throws JSONException {
    long start  = System.currentTimeMillis();
    SectionDao sectionDao = GreenDaoHelper.getDaoSessionBook().getSectionDao();
    SectionLearningCenterDao learningCenterDao = GreenDaoHelper.getDaoSessionBook().getSectionLearningCenterDao();
    SectionLearningCenter learningCenter = learningCenterDao.queryBuilder().where(SectionLearningCenterDao.Properties.Id.eq(section.getId())).unique();
    SectionReferenceDao referenceDao = GreenDaoHelper.getDaoSessionBook().getSectionReferenceDao();
    SectionShorthandDao shorthandDao = GreenDaoHelper.getDaoSessionBook().getSectionShorthandDao();
    //速记
    SectionReference reference = referenceDao.queryBuilder().where(SectionReferenceDao.Properties.LearningCenter_id.eq(learningCenter.getId())).unique();
    //推荐
    SectionShorthand shorthand = shorthandDao.queryBuilder().where(SectionShorthandDao.Properties.LearningCenter_id.eq(learningCenter.getId())).unique();
    Logger.i(" 学习园地一 "+(System.currentTimeMillis()-start));
    start = System.currentTimeMillis();
    Section referenceSection = sectionDao.queryBuilder().where(SectionDao.Properties.Id.eq(reference.getId())).unique();
    List<Paragraph> references = getParagraphContentList(referenceSection,basePath);
    Section shorthandSection = sectionDao.queryBuilder().where(SectionDao.Properties.Id.eq(shorthand.getId())).unique();
    List<Paragraph> shorthands = getParagraphContentList(shorthandSection,basePath);
    Logger.i(" 学习园地二  "+(System.currentTimeMillis()-start));
    referenceSection.setSection(references);
    Map<String,Object> referenceSectionMap = new ArrayMap<>();
    referenceSectionMap.put("id",referenceSection.getId());
    referenceSectionMap.put("template",referenceSection.getTemplate());
    referenceSectionMap.put("chapterId",referenceSection.getChapterId());
    referenceSectionMap.put("contents",referenceSection.getContents());
    referenceSectionMap.put("extend",referenceSection.getExtend());
    referenceSectionMap.put("name",referenceSection.getName());
    referenceSectionMap.put("order",referenceSection.getOrder());
    referenceSectionMap.put("section",referenceSection.getSection());
    referenceSectionMap.put("sectionId",referenceSection.getSectionId());
    referenceSectionMap.put("topImage",referenceSection.getTopImage());
    //作者
//    Map<String,Object> referenceAuthor = getSectionAuthor(referenceSection);
//    referenceSectionMap.putAll(referenceAuthor);

    shorthandSection.setSection(shorthands);
    Map<String,Object> shorthandSectionMap = new ArrayMap<>();
    shorthandSectionMap.put("id",shorthandSection.getId());
    shorthandSectionMap.put("template",shorthandSection.getTemplate());
    shorthandSectionMap.put("chapterId",shorthandSection.getChapterId());
    shorthandSectionMap.put("contents",shorthandSection.getContents());
    shorthandSectionMap.put("extend",shorthandSection.getExtend());
    shorthandSectionMap.put("name",shorthandSection.getName());
    shorthandSectionMap.put("order",shorthandSection.getOrder());
    shorthandSectionMap.put("section",shorthandSection.getSection());
    shorthandSectionMap.put("sectionId",shorthandSection.getSectionId());
    shorthandSectionMap.put("topImage",shorthandSection.getTopImage());
//    Map<String,Object> shorthandAuthor = getSectionAuthor(shorthandSection);
//    shorthandSectionMap.putAll(shorthandAuthor);

    learningCenter.setReference(referenceSectionMap);
    learningCenter.setShorthand(shorthandSectionMap);
    return learningCenter;
  }

  /**
   * 封底
   */
  private static SectionBackCover getBackCover(Section section){
    SectionBackCoverDao backCoverDao = GreenDaoHelper.getDaoSessionBook().getSectionBackCoverDao();
    return backCoverDao.queryBuilder().where(SectionBackCoverDao.Properties.Id.eq(section.getId())).unique();
  }

  /**
   * 致谢名单
   */
  private static SectionAcknowledgements getAcknowledgements(Section section){
    SectionAcknowledgementsDao acknowledgementsDao = GreenDaoHelper.getDaoSessionBook().getSectionAcknowledgementsDao();
    SectionAcknowledgements acknowledgements = acknowledgementsDao.queryBuilder().where(SectionAcknowledgementsDao.Properties.Id.eq(section.getId())).unique();
    SectionNameListDao nameListDao = GreenDaoHelper.getDaoSessionBook().getSectionNameListDao();
    QueryBuilder<SectionNameList> queryBuilder = nameListDao.queryBuilder();
    queryBuilder.where(queryBuilder.and(SectionNameListDao.Properties.Kind.eq(null),
        SectionNameListDao.Properties.Acknowledgements_id.eq(null)));
    Query<SectionNameList> query = queryBuilder.build();
    query.setParameter(0,"editors");
    query.setParameter(1,acknowledgements.getId());
    List<SectionNameList> editors = query.list();
    query.setParameter(0,"thanks");
    query.setParameter(1,acknowledgements.getId());
    List<SectionNameList> thanks = query.list();
    acknowledgements.setEditors(editors);
    acknowledgements.setThanks(thanks);
    return acknowledgements;
  }

  /**
   * 对照索引
   */
  private static Map<String,List<SectionTranslate>> translate(String translateDType) throws JSONException {
    SectionTranslateDao translateDao = GreenDaoHelper.getDaoSessionBook().getSectionTranslateDao();
    Database database = translateDao.getDatabase();
    Cursor cursor = database.rawQuery("select distinct(initial) from Translate order by initial asc",null);
    List<String> initials = new ArrayList<>();
    while (cursor.moveToNext()){
      String initial = cursor.getString(cursor.getColumnIndex("initial"));
      if(initial != null){
        initials.add(initial);
      }
    }
    cursor.close();
    Map<String,List<SectionTranslate>> map = new ArrayMap<>();
    long start = System.currentTimeMillis();
    for(String initial : initials){
      QueryBuilder<SectionTranslate> queryBuilder = translateDao.queryBuilder();
      queryBuilder.where(queryBuilder.and(SectionTranslateDao.Properties.Initial.eq(initial),SectionTranslateDao.Properties.Dtype.eq(translateDType)));
      List<SectionTranslate> translates = queryBuilder.list();
      if(translates != null && translates.size()>0){
        map.put(initial,translates);
      }
    }
    Logger.i("对照索引 "+(System.currentTimeMillis() - start));
    return map;
  }

  /**
   * 文献参考
   */
  private static SectionDocument getDocument(Section section){
    SectionDocumentDao documentDao = GreenDaoHelper.getDaoSessionBook().getSectionDocumentDao();
    SectionDocument document = documentDao.queryBuilder().where(SectionDocumentDao.Properties.Id.eq(section.getId())).unique();
    SectionDocumentItemDao documentItemDao = GreenDaoHelper.getDaoSessionBook().getSectionDocumentItemDao();
    List<SectionDocumentItem> documents =  documentItemDao.queryBuilder().where(SectionDocumentItemDao.Properties.Document_id.eq(document.getId())).list();
    document.setDocuments(documents);
    return document;
  }

  /**
   * 小结
   */
  private static SectionSummary getSummary(Section section){
    SectionSummaryDao summaryDao = GreenDaoHelper.getDaoSessionBook().getSectionSummaryDao();
    SectionSummaryChineseDao summaryChineseDao = GreenDaoHelper.getDaoSessionBook().getSectionSummaryChineseDao();
    SectionSummaryEnglishDao summaryEnglishDao = GreenDaoHelper.getDaoSessionBook().getSectionSummaryEnglishDao();
    SectionSummary summary = summaryDao.queryBuilder().where(SectionSummaryDao.Properties.Id.eq(section.getId())).unique();
    if(summary != null){
      List<SectionSummaryChinese> summaryChineseList = summaryChineseDao.queryBuilder().where(SectionSummaryChineseDao.Properties.Summary_id.eq(summary.getId())).list();
      List<String> chineseList = new ArrayList<>();
      for(SectionSummaryChinese chinese : summaryChineseList){
        chineseList.add(chinese.getChinese());
      }
      List<SectionSummaryEnglish> summaryEnglishes = summaryEnglishDao.queryBuilder().where(SectionSummaryEnglishDao.Properties.Summary_id.eq(summary.getId())).list();
      List<String> englishList = new ArrayList<>();
      for(SectionSummaryEnglish english : summaryEnglishes){
        englishList.add(english.getEnglish());
      }
      summary.setChinese(chineseList);
      summary.setEnglish(englishList);
    }
    return summary;
  }
  /**
   * 病例分析
   */
  private static SectionCaseSection getCasesItemList(Section section) throws JSONException {
    //Section_CaseSection
    SectionCaseSectionDao caseSectionDao = GreenDaoHelper.getDaoSessionBook().getSectionCaseSectionDao();
    SectionCaseSection caseSection = caseSectionDao.queryBuilder().where(SectionCaseSectionDao.Properties.Id.eq(section.getId())).unique();
    //CasesItem
    SectionCasesItemDao casesItemDao = GreenDaoHelper.getDaoSessionBook().getSectionCasesItemDao();
    QueryBuilder<SectionCasesItem> itemQueryBuilder = casesItemDao.queryBuilder();
    itemQueryBuilder.where(SectionCasesItemDao.Properties.Parent_id.eq(null));
    Query<SectionCasesItem> casesItemQuery = itemQueryBuilder.build();
    List<SectionCasesItem> casesItems = casesItemDao.queryBuilder().where(SectionCasesItemDao.Properties.Cases_id.eq(section.getId())).list();
    //病例作者
    SectionCasesItemAuthorsDao casesItemAuthorsDao = GreenDaoHelper.getDaoSessionBook().getSectionCasesItemAuthorsDao();
    QueryBuilder<SectionCasesItemAuthors> authorsQueryBuilder = casesItemAuthorsDao.queryBuilder().where(SectionCasesItemAuthorsDao.Properties.Cases_id.eq(null));
    Query<SectionCasesItemAuthors> casesItemAuthorsQuery = authorsQueryBuilder.build();
    AuthorDao authorDao = GreenDaoHelper.getDaoSessionBook().getAuthorDao();
    QueryBuilder<Author> authorQueryBuilder = authorDao.queryBuilder().where(AuthorDao.Properties.Id.eq(null));
    Query<Author> authorQuery = authorQueryBuilder.build();
    for(int i=0; i<casesItems.size(); i++){
      SectionCasesItem casesItem = casesItems.get(i);
      casesItemAuthorsQuery.setParameter(0,casesItem.getId());
      List<SectionCasesItemAuthors> authorsList = casesItemAuthorsQuery.list();
      List<Author> authors = new ArrayList<>();
      for (SectionCasesItemAuthors itemAuthor : authorsList){
        Author author = authorQuery.setParameter(0,itemAuthor.getAuthors_id()).unique();

        if(author != null && author.getIsExtend() != null && "1".equals(author.getIsExtend())){
          author.setExtend(true);
        }
        authors.add(author);
      }
      casesItem.setAuthors(authors);//案例作者
      List<SectionCasesItem> sectionCasesItems = getCasesItem(casesItemQuery,authorQuery,casesItemAuthorsQuery,casesItem.getId());
      casesItem.setSection(sectionCasesItems);
    }
    caseSection.setCasesItemList(casesItems);
    return caseSection;
  }
  /**
   * 病例分析
   */
  private static List<SectionCasesItem> getCasesItem(Query<SectionCasesItem> casesItemQuery,
      Query<Author> authorQuery,
      Query<SectionCasesItemAuthors> casesItemAuthorsQuery,
      String parentId)
      throws JSONException {
    casesItemQuery.setParameter(0,parentId);
    List<SectionCasesItem> casesItems = casesItemQuery.list();
    for(int i=0; i<casesItems.size(); i++){
      SectionCasesItem casesItem = casesItems.get(i);
      casesItemAuthorsQuery.setParameter(0,casesItem.getId());
      List<SectionCasesItemAuthors> authorsList = casesItemAuthorsQuery.list();
      List<Author> authors = new ArrayList<>();
      for (SectionCasesItemAuthors itemAuthor : authorsList){
        Author author = authorQuery.setParameter(0,itemAuthor.getAuthors_id()).unique();
        if(author != null && author.getIsExtend() != null && "1".equals(author.getIsExtend())){
          author.setExtend(true);
        }
        authors.add(author);
      }
      casesItem.setAuthors(authors);//案例作者
      if(casesItem.getParent_id() != null && !TextUtils.equals("",casesItem.getParent_id())){
        List<SectionCasesItem> sectionCasesItems = getCasesItem(casesItemQuery,authorQuery,casesItemAuthorsQuery,casesItem.getId());
        casesItem.setSection(sectionCasesItems);
      }
    }
    return casesItems;
  }

  /**
   * 获取章节段落
   */
  private static List<Paragraph> getParagraphContentList(Section section,String basePath) throws JSONException {
    //获取该节对应的所有 基础段落

    SectionParagraphDao sectionDetailParagraphDao = GreenDaoHelper.getDaoSessionBook().getSectionParagraphDao();
    Query<SectionParagraph> sectionParagraphSectionIdQuery = sectionDetailParagraphDao.queryBuilder().where(
        SectionParagraphDao.Properties.SectionId.eq(null)).build();
    List<SectionParagraph> sectionDetailParagraphs = sectionParagraphSectionIdQuery.setParameter(0,section.getId()).list();
    if(sectionDetailParagraphs == null){
      //没有对应结果
      return null;
    }
    if(!"BASIC".equals(section.getExtend())){
      if(sectionDetailParagraphs.size() > 1){
        //内容扩展章节获取
        sectionDetailParagraphs.remove(sectionDetailParagraphs.size()-1);
      }
    }
    ParagraphDao paragraphDao = GreenDaoHelper.getDaoSessionBook().getParagraphDao();
    //字段落查询初始化
    QueryBuilder<Paragraph> paragraphQueryBuilder = paragraphDao.queryBuilder();
    paragraphQueryBuilder.where(ParagraphDao.Properties.Id.eq(null));
    Query<Paragraph> paragraphQuery = paragraphQueryBuilder.build();
    QueryBuilder<Paragraph> paragraphParentQueryBuilder = paragraphDao.queryBuilder();
    paragraphParentQueryBuilder.where(ParagraphDao.Properties.Parent_id.eq(null));
    Query<Paragraph> paragraphQueryParent = paragraphParentQueryBuilder.build();

    //表格查询初始化
    TableViewDao tableViewDao = GreenDaoHelper.getDaoSessionBook().getTableViewDao();
    TRViewDao trViewDao = GreenDaoHelper.getDaoSessionBook().getTRViewDao();
    TDViewDao tdViewDao = GreenDaoHelper.getDaoSessionBook().getTDViewDao();
    //tr查询
    QueryBuilder<TRView> trViewDaoQueryBuilder = trViewDao.queryBuilder().where(TRViewDao.Properties.TableView_id.eq(null));
    Query<TRView> trViewQuery = trViewDaoQueryBuilder.build();
    //td查询
    QueryBuilder<TDView> tdViewQueryBuilder = tdViewDao.queryBuilder().where(TDViewDao.Properties.TrView_id.eq(null));
    Query<TDView> tdViewQuery = tdViewQueryBuilder.build();

    //keyword点读查询初始化
    KeyWordDao keyWordDao = GreenDaoHelper.getDaoSessionBook().getKeyWordDao();
    QueryBuilder<KeyWord> keyWordQueryBuilder = keyWordDao.queryBuilder();
    keyWordQueryBuilder.where(KeyWordDao.Properties.Paragraph_id.eq(null));
    Query<KeyWord> keyWordQuery = keyWordQueryBuilder.build();

    //MediaBlock 多媒体图片数据
    MediaBlockDao mediaBlockDao = GreenDaoHelper.getDaoSessionBook().getMediaBlockDao();
    MediasDao mediasDao = GreenDaoHelper.getDaoSessionBook().getMediasDao();
    QueryBuilder<MediaBlock> mediaBlockQueryBuilder = mediaBlockDao.queryBuilder();
    mediaBlockQueryBuilder.where(MediaBlockDao.Properties.Paragraph_id.eq(null));
    Query<MediaBlock> mediaBlockQuery = mediaBlockQueryBuilder.build();

    //节内容
    SectionDao sectionDao = GreenDaoHelper.getDaoSessionBook().getSectionDao();
    QueryBuilder<Section> sectionQueryBuilder = sectionDao.queryBuilder();
    sectionQueryBuilder.where(SectionDao.Properties.Id.eq(null));
    Query<Section> sectionQuery = sectionQueryBuilder.build();

    //节段落关联
    QueryBuilder<SectionParagraph> sectionParagraphQueryBuilder = sectionDetailParagraphDao.queryBuilder();
    sectionParagraphQueryBuilder.where(SectionParagraphDao.Properties.ParagraphsId.eq(null));
    Query<SectionParagraph> sectionParagraphQuery = sectionParagraphQueryBuilder.build();

    //多媒体
      QueryBuilder<Medias> mediasQueryBuilder = mediasDao.queryBuilder();
      mediasQueryBuilder.where(MediasDao.Properties.MediaBlock_id.eq(null));
      Query<Medias> mediasQuery = mediasQueryBuilder.build();


    List<Paragraph> paragraphList = new ArrayList<>();
    for(int i= 0; i<sectionDetailParagraphs.size(); i++){
      SectionParagraph sectionDetailParagraph = sectionDetailParagraphs.get(i);
      paragraphQuery.setParameter(0,sectionDetailParagraph.getParagraphsId());
      Paragraph paragraph = paragraphQuery.unique();//获取段落
      //段落中的点读
      keyWordQuery.setParameter(0,paragraph.getId());
      List<KeyWord> keyWords = keyWordQuery.list();
      for(KeyWord keyword : keyWords){
        String link = basePath + keyword.getLink();
        String readAudio = basePath + keyword.getReadAudio();
        keyword.setLink(link);
        keyword.setReadAudio(readAudio);
      }
      //段落中的图片，视频
      mediaBlockQuery.setParameter(0,paragraph.getId());
      List<MediaBlock> mediaBlocks = mediaBlockQuery.list();
      paragraph.setKeyWord(keyWords);
      mediaBlocks = getMediaBlock(mediaBlocks,mediasQuery,basePath);
      paragraph.setMediaBlocks(mediaBlocks);
      //段落中的表格
      List<TableView> tableViews = getTableView(paragraph,tableViewDao,trViewQuery,tdViewQuery,basePath);
      paragraph.setTableViews(tableViews);
      Map<String,Object> author = getParagraphAuthor(paragraph);
      List<String> titles = (List<String>) author.get("titles");
      List<Map<String,Object>> authors = (List<Map<String, Object>>) author.get("authors");
      paragraph.setTitles(titles);
      paragraph.setAuthors(authors);
      List<Paragraph> sections = getParagraphContent(paragraph.getId(),paragraphQueryParent,
          keyWordQuery,mediaBlockQuery,
          sectionParagraphQuery,
          mediasQuery,
          sectionQuery,
          tableViewDao,
          trViewQuery,
          tdViewQuery,
          basePath);
      paragraph.setSection(sections);
      paragraphList.add(paragraph);
    }
    return paragraphList;
  }
  /**
   * 获取章节段落
   */
  private static List<Paragraph> getParagraphContent(String parentId,Query<Paragraph> paragraphQueryParent,
      Query<KeyWord> keyWordQuery,Query<MediaBlock> mediaBlockQuery,
      Query<SectionParagraph> sectionParagraphQuery,
      Query<Medias> mediasQuery,
      Query<Section> sectionQuery,
      TableViewDao tableViewDao,
      Query<TRView> trViewQuery,
      Query<TDView> tdViewQuery,
      String basePath
  )
      throws JSONException {
    paragraphQueryParent.setParameter(0,parentId);
    List<Paragraph> paragraphs = new ArrayList<>();
    //段落下的子段落
    List<Paragraph> dbParagraphs = paragraphQueryParent.list();
    for(int i=0; i<dbParagraphs.size(); i++){
      Paragraph paragraph = dbParagraphs.get(i);
      //子段落的图片，视频。。
      keyWordQuery.setParameter(0,paragraph.getId());
      List<KeyWord> keyWords = keyWordQuery.list();
      for(KeyWord keyword : keyWords){
        String link = basePath + keyword.getLink();
        String readAudio = basePath + keyword.getReadAudio();
        keyword.setLink(link);
        keyword.setReadAudio(readAudio);
      }
      mediaBlockQuery.setParameter(0,paragraph.getId());
      List<MediaBlock> mediaBlocks = mediaBlockQuery.list();
      mediaBlocks = getMediaBlock(mediaBlocks,mediasQuery,basePath);
      paragraph.setKeyWord(keyWords);
      paragraph.setMediaBlocks(mediaBlocks);
      //段落表格
      List<TableView> tableViews = getTableView(paragraph,tableViewDao,trViewQuery,tdViewQuery,basePath);
      paragraph.setTableViews(tableViews);
      //段落作者
      Map<String,Object> author = getParagraphAuthor(paragraph);
      List<String> titles = (List<String>) author.get("titles");
      List<Map<String,Object>> authors = (List<Map<String, Object>>) author.get("authors");
      paragraph.setTitles(titles);
      paragraph.setAuthors(authors);
      //查找段落对应的节
      List<SectionParagraph> spList = sectionParagraphQuery.setParameter(0,paragraph.getId()).list();
      Map<String,String> extendMap = new HashMap<>();
      if(spList != null){
        for(SectionParagraph sectionParagraph : spList){
          if(sectionParagraph != null){
            Section section = sectionQuery.setParameter(0,sectionParagraph.getSectionId()).unique();
            if(section != null && TextUtils.equals("extend",section.getTemplate())){
              //该段落下有拓展段落
              extendMap.put(section.getExtend(),section.getId());
            }
          }
        }
        paragraph.setSectionExtend(extendMap);
      }

      if(!TextUtils.equals(paragraph.getParent_id(),"0") || paragraph.getParent_id() != null){
        List<Paragraph> sections = getParagraphContent(paragraph.getId(),paragraphQueryParent,
            keyWordQuery,mediaBlockQuery,
            sectionParagraphQuery,
            mediasQuery,
            sectionQuery,
            tableViewDao,
            trViewQuery,
            tdViewQuery,
            basePath);
        paragraph.setSection(sections);
      }
      paragraphs.add(paragraph);
    }
    return paragraphs;
  }

  /**
   * 获取表格数据
   */
  private static List<TableView> getTableView(Paragraph paragraph,TableViewDao tableViewDao,
      Query<TRView> trViewQuery,Query<TDView> tdViewQuery,String basePath){
    //查询段落下的表格
    List<TableView> tableViews = tableViewDao.queryBuilder().where(TableViewDao.Properties.Paragraph_id.eq(paragraph.getId())).list();
    for(TableView tableView : tableViews){
      //查table下的tr
      trViewQuery.setParameter(0,tableView.getId());
      List<TRView> trViews = trViewQuery.list();
      for(TRView trView : trViews){
        //查tr下的td
        tdViewQuery.setParameter(0,trView.getId());
        List<TDView> tdViews = tdViewQuery.list();
        //表格由图片的情况
        for(TDView tdView : tdViews){
          String type = tdView.getType();
          if("picture".equals(type)){
            String pic = basePath + tdView.getText();
            tdView.setText(pic);
          }
        }
        trView.setTdViewList(tdViews);
      }
      tableView.setTrViews(trViews);
    }
    return tableViews;
  }

  /**
   *  获取多媒体数据
   */
  private static List<MediaBlock> getMediaBlock(List<MediaBlock> mediaBlockList,Query<Medias> mediasQuery,String basePath){
    for(MediaBlock mediaBlock : mediaBlockList){
      mediasQuery.setParameter(0,mediaBlock.getId());
      List<Medias> mediasList = mediasQuery.list();
      for(Medias medias : mediasList){
        String abbreviation = basePath + medias.getAbbreviation();
        medias.setAbbreviation(abbreviation);
      }
      mediaBlock.setPictures(mediasList);
    }
    return mediaBlockList;
  }
}
