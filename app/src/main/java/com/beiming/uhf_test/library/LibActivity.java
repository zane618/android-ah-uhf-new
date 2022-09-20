package com.beiming.uhf_test.library;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.databinding.ActivityCheckBinding;
import com.beiming.uhf_test.library.adapter.SpinnerAdapter;
import com.beiming.uhf_test.library.adapter.SpinnerData;
import com.beiming.uhf_test.tools.rfid.IRfidListener;
import com.beiming.uhf_test.tools.rfid.RfidHelper;
import com.beiming.uhf_test.utils.LogPrintUtil;

import java.util.ArrayList;
import java.util.List;

public class LibActivity extends BaseActivity {

    private ActivityCheckBinding binding;



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LibActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        binding = ActivityCheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    @Override
    protected void initView() {
        binding.inTitle.tvTitleName.setText("库房盘点");
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
        SpinnerAdapter adapter = new SpinnerAdapter(dataList, activity);
        binding.spDanwei.setAdapter(adapter);
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
                            boolean exist = false;

                        }
                    }

                    @Override
                    public void onStop() {
                        binding.btnScan.setText("开始盘点");
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {

    }
}
