package com.waterdrop.k.waterdrop.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CheckList extends SQLiteOpenHelper {
    public CheckList(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // 테이블 생성
        database.execSQL("CREATE TABLE checklist (_id INTEGER PRIMARY KEY AUTOINCREMENT, index INTEGER, todo TEXT);");

        database.execSQL("INSERT INTO checklist VALUES(null, 1, '하천에 주차된 자동차는 안전한 곳으로 이동하고, 침수가 예상되는 건물의 지하공간에는 주차하지 마세요.');");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '물이 집안으로 흘러가는 것을 막기 위한 모래주머니나 튜브 등을 준비해두세요.');");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '물에 떠내려갈 수 있는 물건은 안전한 장소로 옮기세요.');");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '주요물품은 방수 비닐팩에 보관하세요.');");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '저지대 주택과 침수피해 우려가 있는 가정에서는 역류방지시설 또는 차수판을 설치하세요.');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS checklist");
        onCreate(database);
    }
}