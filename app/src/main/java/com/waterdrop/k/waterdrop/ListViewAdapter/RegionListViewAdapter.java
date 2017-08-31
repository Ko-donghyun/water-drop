package com.waterdrop.k.waterdrop.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waterdrop.k.waterdrop.ListViewItem.Region;
import com.waterdrop.k.waterdrop.R;

import java.util.ArrayList;

public class RegionListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Region> listViewItemList = new ArrayList<Region>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int mLayout;

    // TextListViewAdapter 생성자
    public RegionListViewAdapter(Context context, int layout) {
        this.mContext = context;
        this.mLayout = layout;
        this.mInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
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
            convertView = inflater.inflate(R.layout.region_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView todo = (TextView) convertView.findViewById(R.id.region_text);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Region listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        todo.setText(listViewItem.getCity1() + " " + listViewItem.getCity2() + " " + listViewItem.getCity3());
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Region getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(long id, String city1, String city2, String city3) {
        Region item = new Region();

        item.setId(id);
        item.setCity1(city1);
        item.setCity2(city2);
        item.setCity3(city3);

        listViewItemList.add(item);
    }

    public void removeItem(int index) {
        listViewItemList.remove(listViewItemList.get(index));
    }
}