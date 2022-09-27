package com.beiming.uhf_test.library;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.LibAssetBean;
import com.beiming.uhf_test.databinding.ActivityCheckBinding;
import com.beiming.uhf_test.databinding.ActivityLibBinding;
import com.beiming.uhf_test.library.adapter.LibAssetAdapter;
import com.beiming.uhf_test.library.adapter.SpinnerAdapter;
import com.beiming.uhf_test.library.adapter.SpinnerData;
import com.beiming.uhf_test.tools.rfid.IRfidListener;
import com.beiming.uhf_test.tools.rfid.RfidHelper;
import com.beiming.uhf_test.utils.LogPrintUtil;

import java.util.ArrayList;
import java.util.List;

public class LibActivity extends BaseActivity {

    private ActivityLibBinding binding;
    private List<LibAssetBean> listData = new ArrayList<>();
    private LibAssetAdapter assetAdapter;



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LibActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        binding = ActivityLibBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    @Override
    protected void initView() {
        binding.inTitle.tvTitleName.setText("库房盘点");
        Drawable right = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_iv_selector, null);
        binding.inTitle.tvRight.setText("菜单");
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        binding.inTitle.tvRight.setCompoundDrawables(null, null, right, null);
        binding.inTitle.tvRight.setCompoundDrawablePadding(10);
        binding.inTitle.tvRight.setVisibility(View.VISIBLE);
        binding.inTitle.ivBack.setOnClickListener(view -> {
            finish();
        });
        binding.spDanwei.setDropDownVerticalOffset(50);
        binding.spDanwei.setDropDownHorizontalOffset(10);
//        ArrayAdapter danweiAdapter = new ArrayAdapter(activity, R.layout.spinner_box_kind,
//                list);
        List<SpinnerData> dataList = new ArrayList<>();
        SpinnerData data = new SpinnerData(0, "0", "000");
        SpinnerData data1 = new SpinnerData(0, "1", "000");
        dataList.add(data);
        dataList.add(data1);
//        SpinnerAdapter adapter = new SpinnerAdapter(dataList, activity);
//        binding.spDanwei.setAdapter(adapter);
        binding.spDanwei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                LogPrintUtil.zhangshi("上spinner:" + ((SpinnerData)binding.spDanwei.getAdapter().getItem(position)).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                LogPrintUtil.zhangshi("上spinner:onNothingSelected");
            }
        });
        assetAdapter = new LibAssetAdapter(listData);
        binding.recycleview.setLayoutManager(new LinearLayoutManager(activity));
        binding.recycleview.setAdapter(assetAdapter);
    }

    @Override
    protected void initListener() {
        binding.btnScan.setOnClickListener(view -> {
            if (!"停止盘点".equals(binding.btnScan.getText())) {
                binding.btnScan.setText("停止盘点");
                RfidHelper.getInstance().startScan(new IRfidListener() {
                    @Override
                    public void onRfidResult(@Nullable String s) {
                        if (s != null) {
                            String barCode  = s.substring(0, s.length() - 2);
                            LibAssetBean assetBean = new LibAssetBean(barCode);
                            boolean exit = listData.contains(assetBean);
                            if (!exit) {
                                String assetNo = barCode.substring(barCode.length() - 15,
                                        barCode.length() - 1);
                                assetBean.setAssetNo(assetNo);
//                                listData.add(assetBean);
                                assetAdapter.addData(0, assetBean);
                            }

                        }
                    }

                    @Override
                    public void onStop() {
                        binding.btnScan.setText("开始盘点");
                    }
                });
            }
        });
        binding.inTitle.tvRight.setOnClickListener(view -> {

        });
    }

    @Override
    protected void initData() {

    }
}
