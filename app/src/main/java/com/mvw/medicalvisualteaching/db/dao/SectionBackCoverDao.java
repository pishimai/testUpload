package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.SectionBackCover;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Section_BackCover".
*/
public class SectionBackCoverDao extends AbstractDao<SectionBackCover, Void> {

    public static final String TABLENAME = "Section_BackCover";

    /**
     * Properties of entity SectionBackCover.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "ID");
        public final static Property Introduce = new Property(1, String.class, "introduce", false, "introduce");
        public final static Property Isbn = new Property(2, String.class, "isbn", false, "isbn");
    }


    public SectionBackCoverDao(DaoConfig config) {
        super(config);
    }
    
    public SectionBackCoverDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SectionBackCover entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String introduce = entity.getIntroduce();
        if (introduce != null) {
            stmt.bindString(2, introduce);
        }
 
        String isbn = entity.getIsbn();
        if (isbn != null) {
            stmt.bindString(3, isbn);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SectionBackCover entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String introduce = entity.getIntroduce();
        if (introduce != null) {
            stmt.bindString(2, introduce);
        }
 
        String isbn = entity.getIsbn();
        if (isbn != null) {
            stmt.bindString(3, isbn);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public SectionBackCover readEntity(Cursor cursor, int offset) {
        SectionBackCover entity = new SectionBackCover( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // introduce
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // isbn
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SectionBackCover entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setIntroduce(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIsbn(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(SectionBackCover entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(SectionBackCover entity) {
        return null;
    }

    @Override
    public boolean hasKey(SectionBackCover entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
