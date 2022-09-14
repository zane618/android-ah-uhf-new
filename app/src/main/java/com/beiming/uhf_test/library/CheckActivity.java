package com.beiming.uhf_test.library;

import android.content.Context;
import android.content.Intent;

import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.databinding.ActivityCheckBinding;

public class CheckActivity extends BaseActivity {

    private ActivityCheckBinding binding;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CheckActivity.class);
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
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
