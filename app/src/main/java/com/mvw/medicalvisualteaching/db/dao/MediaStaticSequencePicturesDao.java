package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.MediaStaticSequencePictures;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MediaStaticSequence_pictures".
*/
public class MediaStaticSequencePicturesDao extends AbstractDao<MediaStaticSequencePictures, Void> {

    public static final String TABLENAME = "MediaStaticSequence_pictures";

    /**
     * Properties of entity MediaStaticSequencePictures.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property MediaStaticSequence_id = new Property(0, String.class, "MediaStaticSequence_id", false, "MediaStaticSequence_id");
        public final static Property Pictures = new Property(1, String.class, "pictures", false, "pictures");
    }


    public MediaStaticSequencePicturesDao(DaoConfig config) {
        super(config);
    }
    
    public MediaStaticSequencePicturesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MediaStaticSequencePictures entity) {
        stmt.clearBindings();
 
        String MediaStaticSequence_id = entity.getMediaStaticSequence_id();
        if (MediaStaticSequence_id != null) {
            stmt.bindString(1, MediaStaticSequence_id);
        }
 
        String pictures = entity.getPictures();
        if (pictures != null) {
            stmt.bindString(2, pictures);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MediaStaticSequencePictures entity) {
        stmt.clearBindings();
 
        String MediaStaticSequence_id = entity.getMediaStaticSequence_id();
        if (MediaStaticSequence_id != null) {
            stmt.bindString(1, MediaStaticSequence_id);
        }
 
        String pictures = entity.getPictures();
        if (pictures != null) {
            stmt.bindString(2, pictures);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public MediaStaticSequencePictures readEntity(Cursor cursor, int offset) {
        MediaStaticSequencePictures entity = new MediaStaticSequencePictures( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // MediaStaticSequence_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // pictures
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MediaStaticSequencePictures entity, int offset) {
        entity.setMediaStaticSequence_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setPictures(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(MediaStaticSequencePictures entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(MediaStaticSequencePictures entity) {
        return null;
    }

    @Override
    public boolean hasKey(MediaStaticSequencePictures entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
