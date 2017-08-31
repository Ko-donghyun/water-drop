package com.waterdrop.k.waterdrop.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyRegion extends SQLiteOpenHelper {
    public MyRegion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // 테이블 생성
        database.execSQL("CREATE TABLE myregion (_id INTEGER PRIMARY KEY AUTOINCREMENT, region_id INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS myregion");
        onCreate(database);
    }
}