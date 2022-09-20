package com.beiming.uhf_test;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beiming.uhf_test.bean.LibAssetBean;
import com.beiming.uhf_test.databinding.ActivityTestBinding;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.fragment.FileImport;
import com.beiming.uhf_test.fragment.FileXls;
import com.beiming.uhf_test.greendao.gen.LibAssetBeanDao;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.utils.LogPrintUtil;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;
    private String s;
    private List<LibAssetBean> list;
    private LibAssetBeanDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dao = GreenDaoManager.getInstance().getSession().getLibAssetBeanDao();

        binding.b1111.setOnClickListener(view -> {

            new exportExcelTask().execute();

        });

        binding.b2.setOnClickListener(v -> {
            List<LibAssetBean> data = dao.loadAll();
            LogPrintUtil.zhangshi("s:size=" + data.size());
        });
    }

    public class exportExcelTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            list = FileXls.readXLSToLibAssetList("/sdcard/UHF/bb.xls", 0);
//            dao.insertInTx(list);
            return (null != list);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (result) {
                for (LibAssetBean item : list) {
                    LogPrintUtil.zhangshi("s:" + item.getAssetId());
                }
            } else {

                LogPrintUtil.zhangshi("s:" + "fail");
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(TestActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("导出中...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }
}
