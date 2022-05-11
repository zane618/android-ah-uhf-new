package com.beiming.uhf_test.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.MeterBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 扫描条形码已保存时弹出弹窗的适配器
 * Created by htj on 2021/5/20.
 */

public class ConflictBarCodeAdpater extends BaseQuickAdapter<MeterBean, BaseViewHolder> {
    List<MeterBean> datas;
    private Context context;
    private String selectBarCode;

    public ConflictBarCodeAdpater(Context context, int layoutResId, @Nullable List<MeterBean> data, String selectBarCode) {
        super(layoutResId, data);
        datas = data;
        this.selectBarCode = selectBarCode;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeterBean item) {
        if (!TextUtils.isEmpty(item.getBarCode())) {
            helper.setText(R.id.tv_tag_uii, "表：" + item.getBarCode());

            helper.addOnClickListener(R.id.tv_tag_rssi);
            if (item.getBarCode().equals(selectBarCode)) {
                helper.setVisible(R.id.tv_tag_rssi, true);
                helper.setVisible(R.id.tv_cicle, true);
//                helper.setBackgroundColor(R.id.ll_base, context.getResources().getColor(R.color.blue2));
            } else {
                //正式使用时设置为false
                helper.setVisible(R.id.tv_tag_rssi, false);
                helper.setVisible(R.id.tv_cicle, false);
//                helper.setBackgroundColor(R.id.ll_base, 0);
            }

        }
    }

    public void setSelectBarCode(String selectBarCode) {
        this.selectBarCode = selectBarCode;
    }
}