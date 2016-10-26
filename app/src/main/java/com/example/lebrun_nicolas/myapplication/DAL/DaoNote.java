package com.example.lebrun_nicolas.myapplication.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lebrun_nicolas.myapplication.fragments.notes.NoteItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by LEBRUN_NICOLAS on 13/10/2016.
 */

public class DaoNote extends SQLiteOpenHelper{

    private static DaoNote instance;

    public DaoNote(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static DaoNote getInstance(Context context) {
        if(instance == null) {
            instance = new DaoNote(context,"CozyNotes.db",null,1);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String rq = "CREATE TABLE notes (" +
                "_id integer PRIMARY KEY AUTOINCREMENT," +
                "title varchar(255)," +
                "note text,"+
                "date_edition datetime)";

        db.execSQL(rq);
        this.populateDB(db);
    }

    private void populateDB(SQLiteDatabase db) {

        for(int i = 0; i < 2 ; i++) {
            NoteItem note = new NoteItem("Note Title "+i, "Note Desc "+i, null);
            insertNote(note, db);
        }
    }

    public void insertNote(NoteItem item, SQLiteDatabase db) {
        ContentValues content = new ContentValues();
        content.put("title", item.getTitle());
        content.put("note", item.getNote());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = format.format(new Date());
        content.put("date_edition", date);
        db.insert("notes", null, content);
    }

    public void insertNote(NoteItem item) {
        ContentValues content = new ContentValues();
        content.put("title", item.getTitle());
        content.put("note", item.getNote());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = format.format(new Date());
        content.put("date_edition", date);
        this.getWritableDatabase().insert("notes", null, content);
    }

    public void updateNote(NoteItem item) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date = format.format(new Date());

        ContentValues content = new ContentValues();
        content.put("title", item.getTitle());
        content.put("note", item.getNote());
        content.put("date_edition", date);
        this.getWritableDatabase().update("notes", content, "_id=?", new String[]{Long.toString(item.getId())} );
    }

    public void deleteNote(int id) {
        this.getWritableDatabase().delete("notes", "_id=?", new String[]{Long.toString(id)} );
    }

    public List<NoteItem> getNotes() {

        List<NoteItem> list = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("select * from notes order by date_edition desc", null);

        if (cursor.moveToFirst()) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            while (!cursor.isAfterLast()) {

                try{
                    Date date = format.parse(cursor.getString(cursor.getColumnIndex("date_edition")));
                    NoteItem noteItem = new NoteItem(
                            cursor.getLong(cursor.getColumnIndex("_id")),
                            cursor.getString(cursor.getColumnIndex("title")),
                            cursor.getString(cursor.getColumnIndex("note")),
                            date
                    );
                    list.add(noteItem);
                    cursor.moveToNext();

                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
