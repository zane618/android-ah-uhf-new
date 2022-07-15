package com.beiming.uhf_test.activity.login;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.beiming.uhf_test.MyApplication;
import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.UHFMainActivity;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.LoginBean;
import com.beiming.uhf_test.bean.UserBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.LoginBeanDao;
import com.beiming.uhf_test.utils.TimeUtils;
import com.beiming.uhf_test.utils.ToastUtils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;
import pub.devrel.easypermissions.EasyPermissions;


public class LoginActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private EditText mEtName;
    private EditText mEtPwd;
    private String name;
    private String pwd;
    private LoginBeanDao loginBeanDao;
    private ProgressDialog progressDialog;

    /**
     * unimp小程序实例缓存
     **/
//    HashMap<String, IUniMP> mUniMPCaches = new HashMap<>();
    @Override
    protected int onCreateView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
//        String jsonFromTaskInfoMap = GsonUtil.getInstance().getJsonFromTaskInfoMap(TaskInfoMap.taskInfoMap);
//        Map<Integer, String> taskInfoMapFromJson = GsonUtil.getInstance().getTaskInfoMapFromJson(jsonFromTaskInfoMap);

        // 动态添加权限
        initPermission();
/*        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    0x11);
        } else {
//            try {
//                ExcleUtil.readExcel("");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }*/
        loginBeanDao = GreenDaoManager.getInstance().getNewSession().getLoginBeanDao();
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        //显示上次登录的账号和密码
        SharedPreferences sp = getSharedPreferences("userName", 0);
        if (sp != null) {
            mEtName.setText(sp.getString("userName", ""));
//            mEtPwd.setText(sp.getString("password", ""));
        }
        findViewById(R.id.ib_login).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);


       /* String SerialNumber = android.os.Build.SERIAL;//获取序列号
        LogUtils.i("序列号=" + SerialNumber);
        String generate = MD5Utils.createActionCode(SerialNumber);//对序列号进行第一次加密
        LogUtils.i("第一次加密后=" + generate);
        String generate1 = MD5Utils.generate(generate);//对加密过后的序列号再一次加密
        LogUtils.i("", "initView: 加密后的序列号=" + generate1);
        mEtName.setText(generate1);*/
        initProgress();

    }

    private void initProgress() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("init...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initData() {
        initTest();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initTest() {
        HashMap<Integer, UserBean> users = new HashMap<>();
        users.put(1, new UserBean("张三", 25));
        users.put(3, new UserBean("李四", 22));
        users.put(2, new UserBean("王五", 28));
        System.out.println(users);
        HashMap<Integer, UserBean> sortHashMap = sortHashMap(users);
        Log.i("CMCC", sortHashMap.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private HashMap<Integer, UserBean> sortHashMap(HashMap<Integer, UserBean> users) {
        Set<Map.Entry<Integer, UserBean>> entries = users.entrySet();
        ArrayList<Map.Entry<Integer, UserBean>> list = new ArrayList<>(entries);
        list.sort(new Comparator<Map.Entry<Integer, UserBean>>() {
            @Override
            public int compare(Map.Entry<Integer, UserBean> integerUserBeanEntry, Map.Entry<Integer, UserBean> t1) {
                return t1.getValue().getAge() - integerUserBeanEntry.getValue().getAge();
            }
        });
        HashMap<Integer, UserBean> returnMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, UserBean> userBeanEntry : list) {
            returnMap.put(userBeanEntry.getKey(), userBeanEntry.getValue());
        }
        return returnMap;
    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_login://登录按钮
                name = mEtName.getText().toString().trim();
                pwd = mEtPwd.getText().toString().trim();

                if (checkUseNameAndPassword()) {
                    if (progressDialog != null) {
                        progressDialog.show();
                    }
                    SharedPreferences useName = getSharedPreferences("userName", 0);
                    SharedPreferences.Editor edit = useName.edit();
                    edit.putString("userName", name);
                    edit.putString("password", pwd);
                    edit.commit();
                    startActivity(new Intent(LoginActivity.this, UHFMainActivity.class));
                    finish();
                }
                break;
            case R.id.tv_register://注册按钮
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                startUniSdk();
                break;
        }
    }

    /**
     * 打开uni小程序
     */
    private void startUniSdk() {
        //判断uni-sdk是否初始化成功
        if (DCUniMPSDK.getInstance().isInitialize()) {
            // 启动小程序并传入参数 "Hello uni microprogram"
            try {
                UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                uniMPOpenConfiguration.extraData.put("MSG", "Hello DCUniMPConfiguration");
                SoftReference<IUniMP> mallMP = new SoftReference<>(DCUniMPSDK.getInstance()
                        .openUniMP(LoginActivity.this, "__UNI__48FDC60", uniMPOpenConfiguration));
                showToast("uni-sdk完成初始化");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showToast("uni-sdk未完成初始化");
        }
    }


    //判断用户名和密码是否正确
    public boolean checkUseNameAndPassword() {
        List<LoginBean> beanLists = loginBeanDao.
                queryRaw("WHERE USER_NAME = ?", name);
        //先验证账号是否存在
        if (!TextUtils.isEmpty(name)) {
            if (beanLists == null || beanLists.size() <= 0) {
                ToastUtils.showToastForLong(this, "该户名不存在");
                return false;
            } else {
                //账号存在再查询密码是否正确
                LoginBean loginBean = beanLists.get(0);
                String password = loginBean.getPassword();
                if (password.equals(pwd)) {
                    //判断是否为超级管理员
                    if (loginBean.getType().equals("1")) {
                        MyApplication.loginBean = loginBean;
                        return true;
                    }
                    //todo 判断该用户是否启用或有效时间是否超时
                    if (loginBean.getIsEnable().equals("1")) {
                        //启用
                        if (TimeUtils.isOutTime(loginBean.getAvailableTimeType(), loginBean.getAvailableTime(), loginBean.getStartEffectiveTime())) {
                            //有效期内
                            MyApplication.loginBean = loginBean;
                            return true;
                        } else {
                            //超过有效期
                            ToastUtils.showToastForLong(this, "该账号已不在有效期内,请联系管理员");
                            return false;
                        }
                    } else {
                        //禁用
                        ToastUtils.showToastForLong(this, "该账号已被禁用,请联系管理员");
                        return false;
                    }
                } else {
                    ToastUtils.showToastForLong(this, "密码不正确");
                    return false;
                }
            }
        } else {
            ToastUtils.showToastForLong(this, "用户名不能为空");
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void initPermission() {
        if (!isPermissionsGranted())
            getPermissions(NULL_PERMISSIONS_REQUEST);

    }

    private int NULL_PERMISSIONS_REQUEST = 1000;//进入界面时的请求码

    public static String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
//            Manifest.permission.CALL_PHONE,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.READ_CONTACTS
    };

    private void getPermissions(int requestNumber) {
        EasyPermissions.requestPermissions(this, "请允许必要的权限", requestNumber, permissions);
    }

    private boolean isPermissionsGranted() {
        boolean isPermission;
        if (EasyPermissions.hasPermissions(this, permissions)) {
            isPermission = true;
        } else {
            isPermission = false;
        }
        return isPermission;
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NULL_PERMISSIONS_REQUEST) {//权限请求成功
        }
    }

    private String Tag = LoginActivity.class.getName();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
