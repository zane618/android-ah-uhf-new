package com.beiming.uhf_test.library.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.FenzhiBoxBean;
import com.beiming.uhf_test.bean.LibAssetBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;

import java.util.List;

/**
 * 扫描条形码的适配器,分支箱 显示勾选
 */

public class LibAssetAdapter extends BaseQuickAdapter<LibAssetBean, BaseViewHolder> {

    public LibAssetAdapter(@Nullable List<LibAssetBean> data) {
        super(R.layout.adapter_items_lib_asset, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, LibAssetBean item) {

        if (helper.getAbsoluteAdapterPosition() % 2 == 0)
            helper.setBackgroundColor(R.id.ll_item, mContext.getResources().getColor(R.color.white));
        else
            helper.setBackgroundColor(R.id.ll_item, mContext.getResources().getColor(R.color.bg_gray));

        if (!TextUtils.isEmpty(item.getBarCode())) {
            helper.setText(R.id.tv_asset_no, "资产编号：" + item.getAssetNo());
            helper.setTextColor(R.id.tv_asset_no, mContext.getResources().getColor(R.color.black));
        }

    }
}
