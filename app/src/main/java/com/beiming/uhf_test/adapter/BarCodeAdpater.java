package com.beiming.uhf_test.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.text.TextUtils;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.BarCodeBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 扫描条形码的适配器
 * Created by htj on 2021/5/20.
 */

public class BarCodeAdpater extends BaseQuickAdapter<BarCodeBean, BaseViewHolder> {
    private final List<MeasBoxBean> boxList;
    private Context context;

    public BarCodeAdpater(@Nullable List<BarCodeBean> data, List<MeasBoxBean> measBoxBeanList) {
        super(R.layout.listtag_items, data);
        this.boxList = measBoxBeanList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BarCodeBean item) {
        if (helper.getAdapterPosition() % 2 == 0)
            helper.setBackgroundColor(R.id.ll_item, context.getResources().getColor(R.color.bg_gray));
        else
            helper.setBackgroundColor(R.id.ll_item, context.getResources().getColor(R.color.white));
        if (!TextUtils.isEmpty(item.getBarCode())) {
            switch (item.getBarCodeType()) {
                case "0":
                    helper.setText(R.id.tv_tag_uii, "箱：" + item.getBarCode());
                    helper.setTextColor(R.id.tv_tag_uii, context.getResources().getColor(R.color.black));
                    if (item.isExsit()) {
                        helper.setText(R.id.tv_count, "已存在");
                        helper.setTextColor(R.id.tv_count, context.getResources().getColor(R.color.red));
                        helper.setText(R.id.tv_tag_rssi, "删除");
                        helper.setVisible(R.id.tv_tag_rssi, true);
                    } else {
                        helper.setText(R.id.tv_count, "可用");
                        helper.setTextColor(R.id.tv_count, context.getResources().getColor(R.color.black));

                        //为箱，箱在本地不存在时，判断扫描到的箱数量，若不等于1的话则显示删除按钮
                        if (boxList.size() != 1) {
                            helper.setText(R.id.tv_tag_rssi, "删除");
                            helper.setVisible(R.id.tv_tag_rssi, true);
                        } else {
                            helper.setVisible(R.id.tv_tag_rssi, false);
                        }
                    }
                    break;
                case "1":
                    helper.setText(R.id.tv_tag_uii, "表：" + item.getBarCode());
                    helper.setTextColor(R.id.tv_tag_uii, context.getResources().getColor(R.color.black));
                    if (item.isExsit()) {
                        helper.setText(R.id.tv_tag_rssi, "管理");
                        helper.setText(R.id.tv_count, "已存在");
                        helper.setTextColor(R.id.tv_count, context.getResources().getColor(R.color.red));
                        helper.setVisible(R.id.tv_tag_rssi, true);
                    } else {
                        helper.setText(R.id.tv_count, "可用");
                        helper.setTextColor(R.id.tv_count, context.getResources().getColor(R.color.black));
                        helper.setText(R.id.tv_tag_rssi, "删除");
                        helper.setVisible(R.id.tv_tag_rssi, true);
                    }
                    break;
                default:
                    helper.setText(R.id.tv_tag_uii, "异常：" + item.getBarCode());
                    helper.setTextColor(R.id.tv_tag_uii, context.getResources().getColor(R.color.red));
                    break;
            }
        }
        helper.addOnClickListener(R.id.tv_tag_rssi);

    }
}
