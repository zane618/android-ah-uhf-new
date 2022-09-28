package com.beiming.uhf_test.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.LibAssetBean;
import com.beiming.uhf_test.data.ConstData;
import com.beiming.uhf_test.databinding.ActivityCheckBinding;
import com.beiming.uhf_test.fragment.FileImport;
import com.beiming.uhf_test.library.adapter.LibAssetAdapter;
import com.beiming.uhf_test.library.adapter.SpinnerAdapter;
import com.beiming.uhf_test.library.bean.LibSpnnerBean;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.tools.rfid.IRfidListener;
import com.beiming.uhf_test.tools.rfid.RfidHelper;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.TimeUtils;
import com.beiming.uhf_test.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class LibCheckActivity extends BaseActivity {

    private ActivityCheckBinding binding;
    private List<LibAssetBean> assetBeanList = new ArrayList<>();
    private LibAssetAdapter assetAdapter;
    private String checkAssetType = "01"; //默认盘点电能表
    private final static String ALL_TYPE = "00"; //全部类型


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LibCheckActivity.class);
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
        Drawable right = ResourcesCompat.getDrawable(getResources(), R.drawable.menu_iv_selector, null);
        binding.inTitle.tvRight.setText("菜单");
        right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        binding.inTitle.tvRight.setCompoundDrawables(null, null, right, null);
        binding.inTitle.tvRight.setCompoundDrawablePadding(10);
        binding.inTitle.tvRight.setVisibility(View.VISIBLE);
        binding.inTitle.ivBack.setOnClickListener(view -> {
            finish();
        });
        binding.spAssetType.setDropDownVerticalOffset(50);
        binding.spAssetType.setDropDownHorizontalOffset(10);
//        ArrayAdapter danweiAdapter = new ArrayAdapter(activity, R.layout.spinner_box_kind,
//                list);
        SpinnerAdapter adapter = new SpinnerAdapter(ConstData.INSTANCE.getLIB_ASSETS(), activity);
        binding.spAssetType.setAdapter(adapter);
        binding.spAssetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                LogPrintUtil.zhangshi("上spinner:" + ((LibSpnnerBean) binding.spAssetType.getAdapter().getItem(position)).getAssectType());
                LibSpnnerBean spnnerBean = (LibSpnnerBean) binding.spAssetType.getAdapter().getItem(position);
                checkAssetType = spnnerBean.getAssectType();
                binding.tvAssetName.setText(spnnerBean.getAssectName());
                RfidHelper.getInstance().stopScan();
                clearRv();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                LogPrintUtil.zhangshi("上spinner:onNothingSelected");
            }
        });
        assetAdapter = new LibAssetAdapter(assetBeanList);
        binding.recycleview.setLayoutManager(new LinearLayoutManager(activity));
        binding.recycleview.setAdapter(assetAdapter);
    }

    @Override
    protected void initListener() {
        binding.btnScan.setOnClickListener(view -> {
            binding.llTips.setVisibility(View.GONE);
            if (!"停止盘点".equals(binding.btnScan.getText())) {
                binding.btnScan.setText("停止盘点");
                RfidHelper.getInstance().startScan(new IRfidListener() {
                    @Override
                    public void onRfidResult(@Nullable String s) {
                        if (s != null) {
                            String barCode = s.substring(0, s.length() - 2);
                            if (checkAssetType.equals(ALL_TYPE) ||
                                    s.startsWith(checkAssetType, 5)) {
                                //是全部类型或者是指定类型资产
                                LibAssetBean assetBean = new LibAssetBean(barCode);
                                boolean exit = assetBeanList.contains(assetBean);
                                if (!exit) {
                                    String assetNo = barCode.substring(barCode.length() - 15,
                                            barCode.length() - 1);
                                    assetBean.setAssetNo(assetNo);
                                    assetAdapter.addData(0, assetBean);
                                    binding.tvCount.setText(assetAdapter.getData().size() + " 个");
                                }
                            }
                        }
                    }

                    @Override
                    public void onStop() {
                        binding.btnScan.setText("开始盘点");
                    }
                });
            } else {
                RfidHelper.getInstance().stopScan();
            }
        });
        binding.inTitle.tvRight.setOnClickListener(view -> {

        });
        binding.btnSure.setOnClickListener(view -> {
            //导出盘点的库存
            if (assetBeanList.isEmpty()) {
                UIHelper.ToastMessage(activity, "请先开始盘点");
                return;
            }
            if ("停止盘点".equals(binding.btnScan.getText().toString())) {
                UIHelper.ToastMessage(activity, "请先停止盘点");
                return;
            }
            new ExportExcelTask().execute();
        });
    }

    private String xlsFileName = "";

    private void clearRv() {
        assetBeanList.clear();
        assetAdapter.notifyDataSetChanged();
        binding.tvCount.setText("");
    }

    /**
     * @author liuruifeng
     */
    public class ExportExcelTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            xlsFileName = TimeUtils.getTime() + "-" + binding.tvAssetName.getText().toString() + ".xls";
            return DcLibAssets.daochu(TimeUtils.getY_M_D_Time(), xlsFileName, assetBeanList);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            clearRv();
            mypDialog.cancel();

            if (result) {
                UIHelper.ToastMessage(activity, "导出成功");
                binding.llTips.setVisibility(View.VISIBLE);
                binding.tvFileName.setText(xlsFileName);
//                tvFileName.setText(fileName);
            } else {
                UIHelper.ToastMessage(activity, "导出失败");
//                tvFileName.setText("导出失败");
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(activity);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("导出中...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        RfidHelper.getInstance().stopScan();
    }
}