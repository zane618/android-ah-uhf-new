package com.beiming.uhf_test.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class BoxListAdapter extends BaseQuickAdapter<MeasBoxBean, BaseViewHolder> {

    private List<MeasBoxBean> datas;
    private int selectedPosition = 0;//当前选中索引选中
    private Context context;

    public BoxListAdapter(int layoutResId, @Nullable List<MeasBoxBean> data) {
        super(layoutResId, data);
        datas = data;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeasBoxBean item) {
        helper.setText(R.id.tv_zc_type, "箱：" + item.getBarCode());
        TextView textView = helper.getView(R.id.tv_zc_type);
        if (selectedPosition == helper.getAdapterPosition()) {
            helper.setVisible(R.id.iv_cb, true);
            textView.setTextColor(context.getResources().getColor(R.color.green_0b7671));
            textView.setBackgroundColor(context.getResources().getColor(R.color.gray_f8f8f8));
        } else {
            helper.setVisible(R.id.iv_cb, false);
            textView.setTextColor(context.getResources().getColor(R.color.gray_666));
            textView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }
}
