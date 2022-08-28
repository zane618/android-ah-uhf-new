package com.beiming.uhf_test.activity.fenzhix.gj;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.FenzhiBoxBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 扫描条形码的适配器,分支箱 显示勾选
 */

public class GjBoxAdpater extends BaseQuickAdapter<MeasBoxBean, BaseViewHolder> {

    public GjBoxAdpater(@Nullable List<MeasBoxBean> data) {
        super(R.layout.gj_box_adapter, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasBoxBean item) {

        if (helper.getAbsoluteAdapterPosition() % 2 == 0)
            helper.setBackgroundColor(R.id.item, mContext.getResources().getColor(R.color.white));
        else
            helper.setBackgroundColor(R.id.item, mContext.getResources().getColor(R.color.bg_gray));

        if (!TextUtils.isEmpty(item.getBarCode())) {
            helper.setText(R.id.tv_tag_uii, "分支箱：" + item.getBarCode());
            helper.setTextColor(R.id.tv_tag_uii, mContext.getResources().getColor(R.color.black));
        }

        /*TextView tvInput = helper.getView(R.id.tv_input);
        tvInput.setOnClickListener(view -> {
            new InputDialog(mContext, note -> {
                item.setNote("备注：" + note);
                notifyDataSetChanged();
            }).show();
        });*/
        ImageView ivCheckbox = helper.getView(R.id.iv_checkbox);
        if (item.isChecked()) {
            ivCheckbox.setImageResource(R.drawable.cb_sel);
        } else {
            ivCheckbox.setImageResource(R.drawable.cb_nol);
        }
        View view = helper.getView(R.id.item);

        view.setOnClickListener(layout -> {
            item.setChecked(!item.isChecked());
            notifyDataSetChanged();
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
//            tvInput.setVisibility(View.VISIBLE);
        } else {
//            tvInput.setVisibility(View.GONE);
            tvNote.setText(item.getNote());
        }


    }
}
