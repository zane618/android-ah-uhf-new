package com.beiming.uhf_test.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.BarCodeBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 扫描条形码的适配器
 * Created by htj on 2021/5/20.
 */

public class BarCodeAdpater extends BaseQuickAdapter<BarCodeBean, BaseViewHolder> {
    List<BarCodeBean> datas;
    private Context context;
    private int boxNumber = 0;

    public BarCodeAdpater(int layoutResId, @Nullable List<BarCodeBean> data, int boxNumber) {
        super(layoutResId, data);
        datas = data;
        this.boxNumber = boxNumber;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BarCodeBean item) {
        if (!TextUtils.isEmpty(item.getBarCode())) {
            switch (item.getBarCodeType()) {
                case "0":
                    helper.setText(R.id.tv_tag_uii, "箱：" + item.getBarCode());
                    helper.setTextColor(R.id.tv_tag_uii, context.getResources().getColor(R.color.black));
                    helper.setText(R.id.tv_tag_rssi, "删除");
                    break;
                case "1":
                    helper.setText(R.id.tv_tag_uii, "表：" + item.getBarCode());
                    helper.setTextColor(R.id.tv_tag_uii, context.getResources().getColor(R.color.black));
                    helper.setText(R.id.tv_tag_rssi, "管理");
                    break;
                default:
                    helper.setText(R.id.tv_tag_uii, "异常：" + item.getBarCode());
                    helper.setTextColor(R.id.tv_tag_uii, context.getResources().getColor(R.color.red));
                    break;
            }
        }
        helper.addOnClickListener(R.id.tv_tag_rssi);
        if (item.isExsit()) {
            helper.setText(R.id.tv_count, "已存在");
            helper.setTextColor(R.id.tv_count, context.getResources().getColor(R.color.red));

            helper.setVisible(R.id.tv_tag_rssi, true);
        } else {
            helper.setText(R.id.tv_count, "可用");
            helper.setTextColor(R.id.tv_count, context.getResources().getColor(R.color.black));

            //正式使用时设置为false
            if (item.getBarCodeType().equals("0")) {
                //为箱，箱在本地不存在时，判断扫描到的箱数量，若不等于1的话则显示管理按钮
                if (boxNumber != 1)
                    helper.setVisible(R.id.tv_tag_rssi, true);
                else
                    helper.setVisible(R.id.tv_tag_rssi, false);
            } else {
                //是表则直接不显示
                helper.setVisible(R.id.tv_tag_rssi, false);
            }
        }
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }
}
