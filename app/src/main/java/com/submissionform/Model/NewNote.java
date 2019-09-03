package com.submissionform.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes")
public class NewNote {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "noteTitle")
    public String notesTitle;

    @ColumnInfo(name = "note")
    public String note;

}
