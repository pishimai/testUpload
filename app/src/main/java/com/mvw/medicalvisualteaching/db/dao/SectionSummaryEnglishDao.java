package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.SectionSummaryEnglish;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Summary_english".
*/
public class SectionSummaryEnglishDao extends AbstractDao<SectionSummaryEnglish, Void> {

    public static final String TABLENAME = "Summary_english";

    /**
     * Properties of entity SectionSummaryEnglish.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Summary_id = new Property(0, String.class, "Summary_id", false, "Summary_id");
        public final static Property English = new Property(1, String.class, "english", false, "english");
    }


    public SectionSummaryEnglishDao(DaoConfig config) {
        super(config);
    }
    
    public SectionSummaryEnglishDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SectionSummaryEnglish entity) {
        stmt.clearBindings();
 
        String Summary_id = entity.getSummary_id();
        if (Summary_id != null) {
            stmt.bindString(1, Summary_id);
        }
 
        String english = entity.getEnglish();
        if (english != null) {
            stmt.bindString(2, english);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SectionSummaryEnglish entity) {
        stmt.clearBindings();
 
        String Summary_id = entity.getSummary_id();
        if (Summary_id != null) {
            stmt.bindString(1, Summary_id);
        }
 
        String english = entity.getEnglish();
        if (english != null) {
            stmt.bindString(2, english);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public SectionSummaryEnglish readEntity(Cursor cursor, int offset) {
        SectionSummaryEnglish entity = new SectionSummaryEnglish( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Summary_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // english
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SectionSummaryEnglish entity, int offset) {
        entity.setSummary_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setEnglish(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(SectionSummaryEnglish entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(SectionSummaryEnglish entity) {
        return null;
    }

    @Override
    public boolean hasKey(SectionSummaryEnglish entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
