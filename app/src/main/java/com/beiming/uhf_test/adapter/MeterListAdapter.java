package com.beiming.uhf_test.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.MeterBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MeterListAdapter extends BaseQuickAdapter<MeterBean, BaseViewHolder> {

    private List<MeterBean> datas;
    private int selectedPosition = 0;//当前选中索引选中
    private Context context;

    public MeterListAdapter(int layoutResId, @Nullable List<MeterBean> data) {
        super(layoutResId, data);
        datas = data;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeterBean item) {
        TextView textView = helper.getView(R.id.tv_zc_type);
        textView.setText(item.getBarCode());

        TextView xw = helper.getView(R.id.tv_axiang);
        if (!TextUtils.isEmpty(item.getPhase())) {
            xw.setText("[" + item.getPhase().toUpperCase(Locale.ROOT) + "相]");
        } else {
            String[] abc = new String[] {"[A相]", "[B相]","[C相]"};

            xw.setText(abc[new Random().nextInt(2)]);
        }
    }

}
