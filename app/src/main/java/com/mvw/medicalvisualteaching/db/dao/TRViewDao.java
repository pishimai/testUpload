package com.mvw.medicalvisualteaching.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.mvw.medicalvisualteaching.bean.offlinebook.TRView;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRView".
*/
public class TRViewDao extends AbstractDao<TRView, Void> {

    public static final String TABLENAME = "TRView";

    /**
     * Properties of entity TRView.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "id");
        public final static Property TableClass = new Property(1, String.class, "tableClass", false, "tableClass");
        public final static Property TableView_id = new Property(2, String.class, "tableView_id", false, "tableView_id");
    }


    public TRViewDao(DaoConfig config) {
        super(config);
    }
    
    public TRViewDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TRView entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String tableClass = entity.getTableClass();
        if (tableClass != null) {
            stmt.bindString(2, tableClass);
        }
 
        String tableView_id = entity.getTableView_id();
        if (tableView_id != null) {
            stmt.bindString(3, tableView_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TRView entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String tableClass = entity.getTableClass();
        if (tableClass != null) {
            stmt.bindString(2, tableClass);
        }
 
        String tableView_id = entity.getTableView_id();
        if (tableView_id != null) {
            stmt.bindString(3, tableView_id);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public TRView readEntity(Cursor cursor, int offset) {
        TRView entity = new TRView( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // tableClass
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // tableView_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TRView entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTableClass(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTableView_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(TRView entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(TRView entity) {
        return null;
    }

    @Override
    public boolean hasKey(TRView entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
