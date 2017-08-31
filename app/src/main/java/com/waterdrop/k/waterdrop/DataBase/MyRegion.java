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
        database.execSQL("CREATE TABLE myregion (_id INTEGER PRIMARY KEY AUTOINCREMENT, city1 TEXT, city2 TEXT, city3 TEXT);");

        database.execSQL("INSERT INTO myregion VALUES(null, '제주특별자치도', '서귀포시', '남원읍');");
        database.execSQL("INSERT INTO myregion VALUES(null, '부산광역시', '금정구', '장전3동');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS myregion");
        onCreate(database);
    }
}