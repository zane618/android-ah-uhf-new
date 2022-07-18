package com.beiming.uhf_test.activity;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.MeterBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.Locale;

/**
 * created by zhangshi on 2022/7/16.
 */
public class RdAdapter extends BaseQuickAdapter<MeterBean, BaseViewHolder> {

    public RdAdapter(@Nullable List<MeterBean> data) {
        super(R.layout.item_r_d_adapter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeterBean item) {
        TextView tv_code = helper.getView(R.id.tv_code);
        TextView xiang = helper.getView(R.id.tv_axiang);
        if (!TextUtils.isEmpty(item.getBarCode())) {
            tv_code.setText(item.getBarCode());
        }
        if (!TextUtils.isEmpty(item.getPhase())) {
            xiang.setText("[" + item.getPhase().toUpperCase(Locale.ROOT) + "相]");
        } else {
            xiang.setText("");
        }
    }
}
