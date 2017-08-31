package com.waterdrop.k.waterdrop;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.waterdrop.k.waterdrop.Dialog.CheckListAddDialog;

public class CheckListSettingActivity extends Activity {

    TextView checkListItemAddButton;
    CheckListAddDialog checkListAddDialog;

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

                checkListAddDialog = new CheckListAddDialog(CheckListSettingActivity.this, okayClickListener);
                checkListAddDialog.show();
            }
        });
    }


    private View.OnClickListener okayClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            checkListAddDialog.dismiss();
        }
    };
}
