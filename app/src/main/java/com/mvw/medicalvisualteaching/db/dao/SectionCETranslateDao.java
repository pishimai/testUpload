package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.SectionCETranslate;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CETranslate".
*/
public class SectionCETranslateDao extends AbstractDao<SectionCETranslate, String> {

    public static final String TABLENAME = "CETranslate";

    /**
     * Properties of entity SectionCETranslate.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "id");
        public final static Property ChineseToEnglish_id = new Property(1, String.class, "chineseToEnglish_id", false, "chineseToEnglish_id");
    }


    public SectionCETranslateDao(DaoConfig config) {
        super(config);
    }
    
    public SectionCETranslateDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SectionCETranslate entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String chineseToEnglish_id = entity.getChineseToEnglish_id();
        if (chineseToEnglish_id != null) {
            stmt.bindString(2, chineseToEnglish_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SectionCETranslate entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String chineseToEnglish_id = entity.getChineseToEnglish_id();
        if (chineseToEnglish_id != null) {
            stmt.bindString(2, chineseToEnglish_id);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SectionCETranslate readEntity(Cursor cursor, int offset) {
        SectionCETranslate entity = new SectionCETranslate( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // chineseToEnglish_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SectionCETranslate entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setChineseToEnglish_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SectionCETranslate entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(SectionCETranslate entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SectionCETranslate entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
