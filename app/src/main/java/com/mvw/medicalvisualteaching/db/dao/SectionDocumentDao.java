package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.SectionDocument;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Section_Document".
*/
public class SectionDocumentDao extends AbstractDao<SectionDocument, String> {

    public static final String TABLENAME = "Section_Document";

    /**
     * Properties of entity SectionDocument.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "id");
    }


    public SectionDocumentDao(DaoConfig config) {
        super(config);
    }
    
    public SectionDocumentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SectionDocument entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SectionDocument entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SectionDocument readEntity(Cursor cursor, int offset) {
        SectionDocument entity = new SectionDocument( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0) // id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SectionDocument entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SectionDocument entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(SectionDocument entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SectionDocument entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
