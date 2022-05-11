package com.beiming.uhf_test.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.base.BaseDialog;
import com.beiming.uhf_test.listener.OnHintDialogClicklistener;


/**
 * Created by htj on 2018/3/30.
 */

public class HintDialog extends BaseDialog implements View.OnClickListener {


    private final String content;
    private final String cancel;
    private final String confrim;
    private OnHintDialogClicklistener onClicklistener;
    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvConfirm;

    public HintDialog(Context context, String content, String cancel, String confrim, OnHintDialogClicklistener onClicklistener) {
        super(context);
        this.content = content;
        this.cancel = cancel;
        this.confrim = confrim;
        this.onClicklistener = onClicklistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);

        findViewById(R.id.tv_confirm).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);

        tvContent = (TextView) findViewById(R.id.tv_content);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);

        tvContent.setText(content);
        tvConfirm.setText(confrim);
        tvCancel.setText(cancel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                dismiss();
                onClicklistener.onConfirm();
                break;
            case R.id.tv_cancel:
                dismiss();
                onClicklistener.onCancel();
                break;
        }
    }
}
