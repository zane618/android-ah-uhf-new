package com.beiming.uhf_test.library.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.FenzhiBoxBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;

import java.util.List;

/**
 * 扫描条形码的适配器,分支箱 显示勾选
 */

public class LibAssetAdpater extends BaseQuickAdapter<FenzhiBoxBean, BaseViewHolder> {

    public LibAssetAdpater(@Nullable List<FenzhiBoxBean> data) {
        super(R.layout.adapter_items_lib_asset, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, FenzhiBoxBean item) {

        if (helper.getAbsoluteAdapterPosition() % 2 == 0)
            helper.setBackgroundColor(R.id.item, mContext.getResources().getColor(R.color.white));
        else
            helper.setBackgroundColor(R.id.item, mContext.getResources().getColor(R.color.bg_gray));

        if (!TextUtils.isEmpty(item.getBarCode())) {
            helper.setText(R.id.tv_tag_uii, "分支箱：" + item.getBarCode());
            helper.setTextColor(R.id.tv_tag_uii, mContext.getResources().getColor(R.color.black));
        }

        TextView tvInput = helper.getView(R.id.tv_input);
        tvInput.setOnClickListener(view -> {

            new XPopup.Builder(mContext)
                    .hasStatusBarShadow(false)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗对象，推荐设置这个
                    .autoOpenSoftInput(true)
                    .isDarkTheme(true)
                    .hasStatusBarShadow(true)
                    .setPopupCallback(new SimpleCallback())
                    .asInputConfirm("备注", null, null, "请输入备注信息",
                            text -> {
                                if (!TextUtils.isEmpty(text)) {
                                    item.setNote("备注：" + text);
                                    notifyDataSetChanged();
                                }
                            }, null, R.layout.my_confim_popup)
                    .show();
        });

        TextView tvLocation = helper.getView(R.id.tv_location);
        if (TextUtils.isEmpty(item.getInstAddr())) {
            tvLocation.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(item.getInstAddr());
        }

        TextView tvNote = helper.getView(R.id.tv_note);
        if ("未备注".equals(item.getNote())) {
            tvNote.setText(item.getNote());
            tvInput.setVisibility(View.VISIBLE);
        } else {
            tvInput.setVisibility(View.GONE);
            tvNote.setText(item.getNote());
        }


    }
}
