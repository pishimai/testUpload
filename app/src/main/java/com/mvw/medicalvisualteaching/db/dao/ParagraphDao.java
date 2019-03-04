package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.Paragraph;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "Paragraph".
*/
public class ParagraphDao extends AbstractDao<Paragraph, Void> {

    public static final String TABLENAME = "Paragraph";

    /**
     * Properties of entity Paragraph.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "id");
        public final static Property Title = new Property(1, String.class, "title", false, "title");
        public final static Property ParagraphId = new Property(2, String.class, "paragraphId", false, "paragraphId");
        public final static Property Text = new Property(3, String.class, "text", false, "text");
        public final static Property Order = new Property(4, String.class, "order", false, "f_order");
        public final static Property TypeEnum = new Property(5, String.class, "typeEnum", false, "typeEnum");
        public final static Property Layout = new Property(6, String.class, "layout", false, "layout");
        public final static Property Parent_id = new Property(7, String.class, "parent_id", false, "parent_id");
    }


    public ParagraphDao(DaoConfig config) {
        super(config);
    }
    
    public ParagraphDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Paragraph entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String paragraphId = entity.getParagraphId();
        if (paragraphId != null) {
            stmt.bindString(3, paragraphId);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(4, text);
        }
 
        String order = entity.getOrder();
        if (order != null) {
            stmt.bindString(5, order);
        }
 
        String typeEnum = entity.getTypeEnum();
        if (typeEnum != null) {
            stmt.bindString(6, typeEnum);
        }
 
        String layout = entity.getLayout();
        if (layout != null) {
            stmt.bindString(7, layout);
        }
 
        String parent_id = entity.getParent_id();
        if (parent_id != null) {
            stmt.bindString(8, parent_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Paragraph entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String paragraphId = entity.getParagraphId();
        if (paragraphId != null) {
            stmt.bindString(3, paragraphId);
        }
 
        String text = entity.getText();
        if (text != null) {
            stmt.bindString(4, text);
        }
 
        String order = entity.getOrder();
        if (order != null) {
            stmt.bindString(5, order);
        }
 
        String typeEnum = entity.getTypeEnum();
        if (typeEnum != null) {
            stmt.bindString(6, typeEnum);
        }
 
        String layout = entity.getLayout();
        if (layout != null) {
            stmt.bindString(7, layout);
        }
 
        String parent_id = entity.getParent_id();
        if (parent_id != null) {
            stmt.bindString(8, parent_id);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Paragraph readEntity(Cursor cursor, int offset) {
        Paragraph entity = new Paragraph( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // paragraphId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // text
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // order
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // typeEnum
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // layout
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // parent_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Paragraph entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setParagraphId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setText(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setOrder(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTypeEnum(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLayout(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setParent_id(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Paragraph entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Paragraph entity) {
        return null;
    }

    @Override
    public boolean hasKey(Paragraph entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
