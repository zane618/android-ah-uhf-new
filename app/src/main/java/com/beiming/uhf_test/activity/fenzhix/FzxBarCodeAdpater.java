package com.beiming.uhf_test.activity.fenzhix;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.BarCodeBean;
import com.beiming.uhf_test.bean.FenzhiBoxBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.dialog.InputDialog;
import com.beiming.uhf_test.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 扫描条形码的适配器,分支箱 显示勾选
 */

public class FzxBarCodeAdpater extends BaseQuickAdapter<FenzhiBoxBean, BaseViewHolder> {

    public FzxBarCodeAdpater(@Nullable List<FenzhiBoxBean> data) {
        super(R.layout.listtag_items_fenzhix, data);
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
            new InputDialog(mContext, note -> {
                item.setNote("备注：" + note);
                notifyDataSetChanged();
            }).show();
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
