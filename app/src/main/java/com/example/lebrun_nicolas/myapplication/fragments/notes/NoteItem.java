package com.example.lebrun_nicolas.myapplication.fragments.notes;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by LEBRUN_NICOLAS on 27/09/2016.
 */

public class NoteItem {

    private long id;
    private String title;
    private String note;
    private Date date;

    public NoteItem(long id, String title, String note, Date date) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public NoteItem(String title, String note, Date date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public NoteItem(){}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
