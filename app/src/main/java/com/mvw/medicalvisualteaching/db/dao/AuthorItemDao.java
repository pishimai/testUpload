package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.AuthorItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "AuthorItem".
*/
public class AuthorItemDao extends AbstractDao<AuthorItem, Void> {

    public static final String TABLENAME = "AuthorItem";

    /**
     * Properties of entity AuthorItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "id");
        public final static Property Order = new Property(1, String.class, "order", false, "f_order");
        public final static Property Name = new Property(2, String.class, "name", false, "name");
        public final static Property Author_id = new Property(3, String.class, "author_id", false, "author_id");
        public final static Property Parent_id = new Property(4, String.class, "parent_id", false, "parent_id");
    }


    public AuthorItemDao(DaoConfig config) {
        super(config);
    }
    
    public AuthorItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AuthorItem entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String order = entity.getOrder();
        if (order != null) {
            stmt.bindString(2, order);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String author_id = entity.getAuthor_id();
        if (author_id != null) {
            stmt.bindString(4, author_id);
        }
 
        String parent_id = entity.getParent_id();
        if (parent_id != null) {
            stmt.bindString(5, parent_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AuthorItem entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String order = entity.getOrder();
        if (order != null) {
            stmt.bindString(2, order);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String author_id = entity.getAuthor_id();
        if (author_id != null) {
            stmt.bindString(4, author_id);
        }
 
        String parent_id = entity.getParent_id();
        if (parent_id != null) {
            stmt.bindString(5, parent_id);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public AuthorItem readEntity(Cursor cursor, int offset) {
        AuthorItem entity = new AuthorItem( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // order
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // author_id
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // parent_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AuthorItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setOrder(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAuthor_id(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setParent_id(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(AuthorItem entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(AuthorItem entity) {
        return null;
    }

    @Override
    public boolean hasKey(AuthorItem entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
