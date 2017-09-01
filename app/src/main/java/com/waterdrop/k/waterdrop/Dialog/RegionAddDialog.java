package com.waterdrop.k.waterdrop.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.waterdrop.k.waterdrop.ListViewAdapter.TextListViewAdapter;
import com.waterdrop.k.waterdrop.R;

public class RegionAddDialog extends Dialog {
    private View.OnClickListener okayClickListener;
    private View.OnClickListener cancelClickListener;
    private AdapterView.OnItemClickListener city1ClickListener;
    private AdapterView.OnItemClickListener city2ClickListener;
    private AdapterView.OnItemClickListener city3ClickListener;
    GridView city1GridView;
    GridView city2GridView;
    GridView city3GridView;
    TextListViewAdapter city1ListViewAdapter;
    TextListViewAdapter city2ListViewAdapter;
    TextListViewAdapter city3ListViewAdapter;

    TextView city;

    private Context context;

    ViewFlipper regionListViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.region_add_dialog);

        Button okayButton = (Button) findViewById(R.id.okay_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        regionListViewFlipper = (ViewFlipper) findViewById(R.id.add_region_view_flipper);
        city = (TextView) findViewById(R.id.city_text);

        city1GridView = (GridView) findViewById(R.id.city_1);
        city2GridView = (GridView) findViewById(R.id.city_2);
        city3GridView = (GridView) findViewById(R.id.city_3);

        city1GridView.setAdapter(city1ListViewAdapter);
        city2GridView.setAdapter(city2ListViewAdapter);
        city3GridView.setAdapter(city3ListViewAdapter);

        city1GridView.setOnItemClickListener(city1ClickListener);
        city2GridView.setOnItemClickListener(city2ClickListener);
        city3GridView.setOnItemClickListener(city3ClickListener);

        okayButton.setOnClickListener(okayClickListener);
        cancelButton.setOnClickListener(cancelClickListener);
    }

    public RegionAddDialog(Context context, View.OnClickListener okayClickListener, View.OnClickListener cancelClickListener,
                           AdapterView.OnItemClickListener city1ClickListener, AdapterView.OnItemClickListener city2ClickListener, AdapterView.OnItemClickListener city3ClickListener,
                           TextListViewAdapter city1ListViewAdapter, TextListViewAdapter city2ListViewAdapter, TextListViewAdapter city3ListViewAdapter) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.okayClickListener = okayClickListener;
        this.cancelClickListener = cancelClickListener;
        this.city1ClickListener = city1ClickListener;
        this.city2ClickListener = city2ClickListener;
        this.city3ClickListener = city3ClickListener;
        this.city1ListViewAdapter = city1ListViewAdapter;
        this.city2ListViewAdapter = city2ListViewAdapter;
        this.city3ListViewAdapter = city3ListViewAdapter;
    }

    public void updateCity2Adapter(TextListViewAdapter city2ListViewAdapter) {
        this.city2ListViewAdapter = city2ListViewAdapter;

        city2GridView.setAdapter(city2ListViewAdapter);

    }
    public void updateCity3Adapter(TextListViewAdapter city3ListViewAdapter) {
        this.city3ListViewAdapter = city3ListViewAdapter;

        city3GridView.setAdapter(city3ListViewAdapter);
    }
    public void nextViewFlipper() {
        regionListViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in));
        regionListViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out));
        regionListViewFlipper.showNext();
    }
    public void previousViewFlipper() {
        regionListViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
        regionListViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
        regionListViewFlipper.showPrevious();
    }
    public void updateCityText(String text) {
        city.setText(text);
    }
    public int getRegionListViewFlipperDisplayedChild() {
        return regionListViewFlipper.getDisplayedChild();
    }
}