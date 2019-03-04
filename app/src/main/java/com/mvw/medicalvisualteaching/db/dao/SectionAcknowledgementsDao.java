package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.SectionAcknowledgements;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Section_Acknowledgements".
*/
public class SectionAcknowledgementsDao extends AbstractDao<SectionAcknowledgements, String> {

    public static final String TABLENAME = "Section_Acknowledgements";

    /**
     * Properties of entity SectionAcknowledgements.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
    }


    public SectionAcknowledgementsDao(DaoConfig config) {
        super(config);
    }
    
    public SectionAcknowledgementsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SectionAcknowledgements entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SectionAcknowledgements entity) {
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
    public SectionAcknowledgements readEntity(Cursor cursor, int offset) {
        SectionAcknowledgements entity = new SectionAcknowledgements( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0) // id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SectionAcknowledgements entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SectionAcknowledgements entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(SectionAcknowledgements entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SectionAcknowledgements entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
