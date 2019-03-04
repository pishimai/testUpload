package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.SectionParagraph;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Section_Paragraph".
*/
public class SectionParagraphDao extends AbstractDao<SectionParagraph, Void> {

    public static final String TABLENAME = "Section_Paragraph";

    /**
     * Properties of entity SectionParagraph.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property SectionId = new Property(0, String.class, "sectionId", false, "sections_id");
        public final static Property ParagraphsId = new Property(1, String.class, "paragraphsId", false, "paragraphs_id");
    }


    public SectionParagraphDao(DaoConfig config) {
        super(config);
    }
    
    public SectionParagraphDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SectionParagraph entity) {
        stmt.clearBindings();
 
        String sectionId = entity.getSectionId();
        if (sectionId != null) {
            stmt.bindString(1, sectionId);
        }
 
        String paragraphsId = entity.getParagraphsId();
        if (paragraphsId != null) {
            stmt.bindString(2, paragraphsId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SectionParagraph entity) {
        stmt.clearBindings();
 
        String sectionId = entity.getSectionId();
        if (sectionId != null) {
            stmt.bindString(1, sectionId);
        }
 
        String paragraphsId = entity.getParagraphsId();
        if (paragraphsId != null) {
            stmt.bindString(2, paragraphsId);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public SectionParagraph readEntity(Cursor cursor, int offset) {
        SectionParagraph entity = new SectionParagraph( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // sectionId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // paragraphsId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SectionParagraph entity, int offset) {
        entity.setSectionId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setParagraphsId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(SectionParagraph entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(SectionParagraph entity) {
        return null;
    }

    @Override
    public boolean hasKey(SectionParagraph entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
