package com.beiming.uhf_test.library;

import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;

import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.databinding.ActivityCheckBinding;

public class CheckActivity extends BaseActivity {

    private ActivityCheckBinding binding;
    private Spinner spDanwei;
    private Spinner spCangku;



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
        binding.inTitle.ivBack.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }
}
