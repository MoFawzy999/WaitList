package com.fawzy.waitlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public  class Wait_DB extends SQLiteOpenHelper {

    private static final String DB_NAME = "WaitList";
    private static final int DB_VERSION = 1 ;


    public Wait_DB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ WaitListContract.WaitListEntry.TABLE_NAME+"("+ WaitListContract.WaitListEntry._ID
        +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ WaitListContract.WaitListEntry.COULUMN_GUEST_NAME + " TEXT NOT NULL ,"+
                WaitListContract.WaitListEntry.COULUMN_PARTY_SIZE + " TEXT NOT NULL ,"+ WaitListContract.WaitListEntry.COULUMN_TIMESTAP
        +" TIMESTAMP DEFAULT CURRENT_TIMESTAMP " + ");" ) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(" DROP TABLE IF EXISTS " + WaitListContract.WaitListEntry.TABLE_NAME);
            onCreate(db);
    }

}
