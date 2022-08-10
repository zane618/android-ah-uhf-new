package com.beiming.uhf_test.activity.fenzhix;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.BarCodeBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 扫描条形码的适配器,分支箱 显示勾选
 */

public class FzxBarCodeAdpater extends BaseQuickAdapter<BarCodeBean, BaseViewHolder> {

    public FzxBarCodeAdpater(@Nullable List<BarCodeBean> data) {
        super(R.layout.listtag_items_fenzhix, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, BarCodeBean item) {

        if (helper.getAdapterPosition() % 2 == 0)
            helper.setBackgroundColor(R.id.item, mContext.getResources().getColor(R.color.bg_gray));
        else
            helper.setBackgroundColor(R.id.item, mContext.getResources().getColor(R.color.white));

        if (!TextUtils.isEmpty(item.getBarCode())) {
            helper.setText(R.id.tv_tag_uii, "分支箱：" + item.getBarCode());
            helper.setTextColor(R.id.tv_tag_uii, mContext.getResources().getColor(R.color.black));

        }
        ImageView iv = helper.getView(R.id.iv);
        if (item.isSelect()) {
            iv.setImageResource(R.drawable.rb_sel);
        } else {
            iv.setImageResource(R.drawable.rb_nol);
        }
        iv.setOnClickListener(v -> {
            boolean old = item.isSelect();
            if (old) { //选中状态，再点击 不给效果
                return;
            }
            boolean newer = true;
            for (BarCodeBean bean : getData()) {
                bean.setSelect(!newer);
            }
            item.setSelect(newer);

            notifyDataSetChanged();
        });
    }
}
