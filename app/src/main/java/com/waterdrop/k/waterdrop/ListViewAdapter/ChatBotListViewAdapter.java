package com.waterdrop.k.waterdrop.ListViewAdapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

    TextView answer;
    TextView question;
    private LayoutInflater mInflater;

    // TextListViewAdapter 생성자
    public ChatBotListViewAdapter(Context context) {
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
        ViewHolder holder = null;
        int type = getItemViewType(position);
//        System.out.println("getView " + position + " " + convertView + " type = " + type);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 0:
                    convertView = mInflater.inflate(R.layout.chat_bot_answer_item, null);
                    holder.textView = (TextView)convertView.findViewById(R.id.answer);
                    holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                    holder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.parent_layout);
                    break;
                case 1:
                    convertView = mInflater.inflate(R.layout.chat_bot_question_item, null);
                    holder.textView = (TextView)convertView.findViewById(R.id.question);
                    holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                    holder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.parent_layout);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(listViewItemList.get(position).getText());
        if (type == 0) {
            holder.linearLayout.setBackgroundResource(R.drawable.leftchat);
//            holder.linearLayout.setAli
            holder.linearLayout2.setGravity(Gravity.LEFT);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.rightchat);
            holder.linearLayout2.setGravity(Gravity.RIGHT);
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


    public static class ViewHolder {
        public TextView textView;
        public LinearLayout linearLayout;
        public LinearLayout linearLayout2;
    }
    public static class ViewHolder2 {
        public TextView question;
    }
}