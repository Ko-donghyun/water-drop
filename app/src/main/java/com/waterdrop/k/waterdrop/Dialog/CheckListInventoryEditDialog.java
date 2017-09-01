package com.waterdrop.k.waterdrop.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.waterdrop.k.waterdrop.ListViewAdapter.TextListViewAdapter;
import com.waterdrop.k.waterdrop.R;

public class CheckListInventoryEditDialog extends Dialog {

    private Context mContext;
    private View.OnClickListener cancelClickListener;
    private View.OnClickListener okayClickListener;
    AdapterView.OnItemLongClickListener checkListInventoryEditDialogListViewClickListener;

    ListView inventoryListView;
    TextListViewAdapter inventoryListViewAdapter;
    EditText inventoryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.check_list_inventory_edit_dialog);

        inventoryListView = (ListView) findViewById(R.id.check_list_inventory_list_view);
        inventoryListView.setAdapter(inventoryListViewAdapter);
        inventoryListView.setOnItemLongClickListener(checkListInventoryEditDialogListViewClickListener);

        inventoryEditText = (EditText) findViewById(R.id.inventory_edit_text);

        Button okayButton = (Button) findViewById(R.id.dialog_okay);
        okayButton.setOnClickListener(okayClickListener);
        Button cancelButton = (Button) findViewById(R.id.dialog_cancel);
        cancelButton.setOnClickListener(cancelClickListener);
    }

    public CheckListInventoryEditDialog(Context context,
                                        View.OnClickListener okayClickListener,
                                        View.OnClickListener cancelClickListener,
                                        TextListViewAdapter inventoryListViewAdapter,
                                        AdapterView.OnItemLongClickListener checkListInventoryEditDialogListViewClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mContext = context;
        this.okayClickListener = okayClickListener;
        this.cancelClickListener = cancelClickListener;
        this.inventoryListViewAdapter = inventoryListViewAdapter;
        this.checkListInventoryEditDialogListViewClickListener = checkListInventoryEditDialogListViewClickListener;

    }

    public String getInventoryEditText() {
        return inventoryEditText.getText().toString();
    }

    public void updateInventoryListViewAdapter(TextListViewAdapter inventoryListViewAdapter) {
        this.inventoryListViewAdapter = inventoryListViewAdapter;
        inventoryListView.setAdapter(inventoryListViewAdapter);
    }
}