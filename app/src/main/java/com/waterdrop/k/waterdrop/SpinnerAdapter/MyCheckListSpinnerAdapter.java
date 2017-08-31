package com.waterdrop.k.waterdrop.SpinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.waterdrop.k.waterdrop.R;
import com.waterdrop.k.waterdrop.SpinnerItem.CheckListInventory;

import java.util.ArrayList;


public class MyCheckListSpinnerAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CheckListInventory> listViewItemList = new ArrayList<CheckListInventory>();

    // TextListViewAdapter 생성자
    public MyCheckListSpinnerAdapter() {

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
            convertView = inflater.inflate(R.layout.check_list_inventory_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView todo = (TextView) convertView.findViewById(R.id.text_item);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CheckListInventory listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        todo.setText(listViewItem.getName());
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    public Long getItemValue(int position) {
        return listViewItemList.get(position).getId();
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public CheckListInventory getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(long id, String name) {
        CheckListInventory item = new CheckListInventory();

        item.setId(id);
        item.setName(name);

        listViewItemList.add(item);
    }

    //private method of your class
    public int getIndex(Spinner spinner, Long lastSelectedCheckListInventoryId) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (getItemValue(i) == lastSelectedCheckListInventoryId) {
                index = i;
                break;
            }
        }
        return index;
    }



    public void removeItem(int index) {
        listViewItemList.remove(listViewItemList.get(index));
    }
}