package com.waterdrop.k.waterdrop.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.waterdrop.k.waterdrop.ListViewItem.CheckList;
import com.waterdrop.k.waterdrop.R;

import java.util.ArrayList;

public class CheckListViewAdapter2 extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CheckList> listViewItemList = new ArrayList<CheckList>();

    boolean isLocal;

    // TextListViewAdapter 생성자
    public CheckListViewAdapter2(boolean isLocal) {
        this.isLocal = isLocal;


    }

    // Adapter에 사용되는 데이터의 개수를 리턴 : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "todo_list_item" Layout을 inflate하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.check_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView todo = (TextView) convertView.findViewById(R.id.text_item);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_list_is_checked);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CheckList listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        todo.setText(listViewItem.getTodo());
        checkBox.setChecked(isLocal);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public CheckList getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(long id, int checkListInventoryId, String todo, int isChecked) {
        CheckList item = new CheckList();

        item.setId(id);
        item.setCheckListInventoryId(checkListInventoryId);
        item.setTodo(todo);
        item.setIsChecked(isChecked);

        listViewItemList.add(item);
    }

    public void removeItem(int index) {
        listViewItemList.remove(listViewItemList.get(index));
    }
}