package com.beiming.uhf_test.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.MainActivity;
import com.beiming.uhf_test.activity.login.LoginActivity;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.utils.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//导出excel表格
public class ExportExcelFragment extends KeyDwonFragment implements View.OnClickListener {


    @BindView(R.id.tv_box_number)
    TextView tvBoxNumber;
    @BindView(R.id.tv_file_name)
    TextView tvFileName;
    @BindView(R.id.bt_export)
    Button btExport;
    private MainActivity mContext;
    private List<MeasBoxBean> allBoxBeanList;
    Unbinder unbinder;
    private int MY_PERMISSIONS_REQUEST_FOR_EXCLE = 0x11;
    private String fileName;
    private String fileDirString;

    private String Tag = LoginActivity.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("CMCC", Tag+":"+System.currentTimeMillis());
        View view = inflater.inflate(R.layout.activity_export_excel, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        initAdapter();
        initListener();
    }

    private void initListener() {
        btExport.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    private void initAdapter() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_export:
                checkImportExcel();
                break;
        }

    }

    private void checkImportExcel() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_FOR_EXCLE);
        } else {
            ImportExcel();
        }
    }

    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_FOR_EXCLE) {//权限请求成功导，出所有word
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImportExcel();
            } else {
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    //导出excel表格
    private void ImportExcel() {
//        allBoxBeanList = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao().loadAll();
        allBoxBeanList = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao()
                .queryBuilder().where(MeasBoxBeanDao.Properties.Ts.ge(TimeUtils.toTs(TimeUtils.getY_M_D_Time()))).build().list();
        if (allBoxBeanList.size() == 0) {
            UIHelper.ToastMessage(mContext, "本地数据为空");
            return;
        }
        fileName = TimeUtils.getTime() + ".xls";
        fileDirString = TimeUtils.getY_M_D_Time();
        new exportExcelTask().execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @author liuruifeng
     */
    public class exportExcelTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return FileImport.daochu(fileDirString, fileName, allBoxBeanList);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (result) {
                UIHelper.ToastMessage(mContext, "导出成功");
                tvFileName.setText(fileName);
            } else {
                UIHelper.ToastMessage(mContext, "导出失败");
                tvFileName.setText("导出失败");
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(mContext);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("导出中...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }
}
