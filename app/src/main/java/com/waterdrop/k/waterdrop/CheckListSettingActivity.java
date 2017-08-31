package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CheckListSettingActivity extends Activity {

    TextView checkListItemAddButton;
    AddDialog addDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_list_setting);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        checkListItemAddButton = (TextView) findViewById(R.id.check_list_item_add_button);

        checkListItemAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addDialog = new AddDialog(CheckListSettingActivity.this, okayClickListener);
                addDialog.show();
            }
        });
    }


    private View.OnClickListener okayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            addDialog.dismiss();
        }
    };
}
