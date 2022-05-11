package com.beiming.uhf_test.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.MeterBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

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
        helper.setText(R.id.tv_zc_type, item.getBarCode());
        TextView textView = helper.getView(R.id.tv_zc_type);
    }

}
