package com.waterdrop.k.waterdrop.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CheckList extends SQLiteOpenHelper {
    public CheckList(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

//
//    낙뢰시 낮은지역 또는 건물안 등 안전지대로 대피
//    노약자나 어린이는 외출 자제
//    라디오, TV 등에 의한 기상예보 및 특보상황 청취
//    아파트등 고층건물 옥상, 지하실 및 하수도 맨홀 등 접근금지
//    농촌ㆍ산간지역의 제반시설 보호 및 보강
//    정전대비 비상대처준비 및 비상시 연락방법, 교통이용수단 확인
//    응급약품·손전등·식수·비상식량 등의 생필품은 미리준비
//    등산 중일 때에는 빨리 하산하거나 급히 높은 지대로 피신하되, 물살이 거센 계곡은 절대로 건너지 마세요.
//    도시가스를 사용하는 가정은 중간밸브 뿐만 아니라 계량기 옆의 메인 밸브까지 잠그고 대피하세요.
//    LP가스를 사용하는 가정은 용기에 부착된 용기밸브를 잠그고 체인 등을 이용, 안전한 장소에 고정시켜 놓고 대피하세요
//    침수된 집안은 가스누출의 위험이 있으니 반드시 전원을 차단하고 환기시킨 후 들어가세요.
//    물에 젖었던 가스보일러를 점검 받지 않은 채 전원 플러그를 꽂으면 보일러 내부의 기기판이 타버리는 것은 물론, 안전장치가 타서 가스사고의 위험을 초래할 수 있습니다.
//    대피했다가 집에 돌아오면 바로 들어가지 말고, 구조적 붕괴가능성을 반드시 점검합시다.
//    수돗물이나 저장식수도 오염 여부를 반드시 조사한 후에 사용합시다.

    @Override
    public void onCreate(SQLiteDatabase database) {
        // 테이블 생성
        database.execSQL("CREATE TABLE checklistinventory (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");
        database.execSQL("CREATE TABLE checklist (_id INTEGER PRIMARY KEY AUTOINCREMENT, checklistinventory_id INTEGER, todo TEXT, ischecked INTEGER);");

        database.execSQL("INSERT INTO checklistinventory VALUES(null, '일반');");
        database.execSQL("INSERT INTO checklistinventory VALUES(null, '농촌');");

        database.execSQL("INSERT INTO checklist VALUES(null, 1, '낙뢰 시 낮은 지역 또는 건물안 등 안전지대로 대피하세요.', 0);");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '노약자나 어린이는 외출을 자제하세요.', 0);");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '라디오, TV 등에 의한 기상예보 및 특보상황 청취하세요.', 0);");
        database.execSQL("INSERT INTO checklist VALUES(null, 1, '아파트등 고층건물 옥상, 지하실 및 하수도 맨홀 등 접근 금지하세요.', 0);");
        database.execSQL("INSERT INTO checklist VALUES(null, 2, '농촌ㆍ산간지역의 제반시설 보호 및 보강하세요.', 0);");
        database.execSQL("INSERT INTO checklist VALUES(null, 2, '정전대비 비상대처준비 및 비상시 연락방법, 교통이용수단을 확인하세요.', 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS checklistinventory");
        database.execSQL("DROP TABLE IF EXISTS checklist");
        onCreate(database);
    }
}