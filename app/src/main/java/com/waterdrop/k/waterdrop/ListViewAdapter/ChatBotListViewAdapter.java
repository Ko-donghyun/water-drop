package com.waterdrop.k.waterdrop.ListViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waterdrop.k.waterdrop.ListViewItem.TextItem2;
import com.waterdrop.k.waterdrop.R;

import java.util.ArrayList;

/**
 * Created by kodong on 2017. 9. 1..
 */

public class ChatBotListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<TextItem2> listViewItemList = new ArrayList<TextItem2>();

    // TextListViewAdapter 생성자
    public ChatBotListViewAdapter() {

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
        int viewType = getItemViewType(position) ;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            TextItem2 listViewItem = listViewItemList.get(position);

            switch (viewType) {
                case 0:
                    convertView = inflater.inflate(R.layout.chat_bot_answer_item, parent, false);
                    TextView answer = (TextView) convertView.findViewById(R.id.answer) ;

                    answer.setText(listViewItem.getText());
                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.chat_bot_question_item, parent, false);

                    TextView question = (TextView) convertView.findViewById(R.id.question) ;

                    question.setText(listViewItem.getText());
                    break;
            }
        }

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴 : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public TextItem2 getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType() ;
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(long id, String text, int type) {
        TextItem2 item = new TextItem2();

        item.setId(id);
        item.setText(text);
        item.setType(type);

        listViewItemList.add(item);
    }

    public void removeItem(int index) {
        listViewItemList.remove(listViewItemList.get(index));
    }
}