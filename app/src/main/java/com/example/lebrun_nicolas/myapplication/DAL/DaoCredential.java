package com.example.lebrun_nicolas.myapplication.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lebrun_nicolas.myapplication.fragments.notes.NoteItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LEBRUN_NICOLAS on 26/10/2016.
 */


public class DaoCredential extends SQLiteOpenHelper{

    private static DaoCredential instance;

    public DaoCredential(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DaoCredential getInstance(Context context) {
        if (instance == null) {
            instance = new DaoCredential(context,"CozyNotes.db",null,1);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String rq = "CREATE TABLE params (" +
                "key varchar(255) primary key" +
                "value varchar(255))";
        db.execSQL(rq);
        this.populateDB(db);
    }

    private void populateDB(SQLiteDatabase db) {
        ContentValues content = new ContentValues();
        content.put("key", "login");
        content.put("value", "");
        db.insert("params", null, content);
        ContentValues content2 = new ContentValues();
        content2.put("key", "password");
        content2.put("value", "");
        db.insert("params", null, content2);
    }

    public void updateParam(String key, String value) {
        ContentValues content = new ContentValues();
        content.put("value", value);
        this.getWritableDatabase().update("params", content, "key=?", new String[]{key} );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
