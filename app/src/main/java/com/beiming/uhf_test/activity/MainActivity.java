package com.beiming.uhf_test.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.TestActivity;
import com.beiming.uhf_test.adapter.MainViewPagerAdapter;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.databinding.ActivityMainBinding;
import com.beiming.uhf_test.fragment.DataRecordFragment;
import com.beiming.uhf_test.fragment.ExportExcelFragment;
import com.beiming.uhf_test.fragment.UHFReadTagFragment;
import com.beiming.uhf_test.fragment.UHFSetFragment;
import com.beiming.uhf_test.helper.map.LocationHelper;
import com.beiming.uhf_test.tools.rfid.RfidHelper;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.FastJson;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.MyOnTransitionTextListener;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.hc.pda.HcPowerCtrl;
import com.kongzue.baseframework.util.SettingsUtil;
import com.pow.api.cls.RfidPower;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.common.util.RuningAcitvityUtil;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * UHF使用demo
 * <p>
 * 1、使用前请确认您的机器已安装此模块。
 * 2、要正常使用模块需要在\libs\armeabi\目录放置libDeviceAPI.so文件，同时在\libs\目录下放置DeviceAPIver20160728.jar文件。
 * 3、在操作设备前需要调用 init()打开设备，使用完后调用 free() 关闭设备
 * <p>
 * <p>
 * 更多函数的使用方法请查看API说明文档
 *
 * @author 更新于 2016年8月9日
 */

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityMainBinding binding;

    private final static String TAG = "UHFMainActivity:";

    public ArrayList<HashMap<String, String>> tagList;
    public HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 非小程序进程（这里的unimp 关键字 可以根据宿主的具体情况进行调整）
        if (!RuningAcitvityUtil.getAppName(getBaseContext()).contains("unimp")) {
            //请在此处初始化其他三方SDK
            initLocation();
        }
        RfidHelper.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {

    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_LOCATION = 1;

    //初始化定位信息
    private void initLocation() {
        //检查版本是否大于M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_CALL_LOCATION);
            } else {
                //"权限已申请";
                showLocation();
            }
        }
    }

    private void showLocation() {
        new LocationHelper().setListener(locationBean -> {
            if (null != locationBean) {
                LogPrintUtil.zhangshi("定位成功 getNewAppLocation:" + FastJson.toJSONString(locationBean));
                SharedPreferencesUtil.getInstance().setObjectToShare(ConstantUtil.LAST_LOCATION, locationBean);
            } else {
                //定位失败
                LogPrintUtil.zhangshi("定位失败 getNewAppLocation:" + FastJson.toJSONString(locationBean));
            }
        }).startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //"权限已申请"
                showLocation();
            } else {
                showToast("权限已拒绝,不能定位");

            }
        }
    }


    public void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RfidHelper.getInstance().exitUHF();
    }

    @Override
    protected void setContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private IndicatorViewPager indicatorViewPager;
    private MainViewPagerAdapter mAdapter;

    @Override
    protected void initView() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new UHFReadTagFragment());
        fragments.add(new DataRecordFragment());
        fragments.add(new ExportExcelFragment());
        fragments.add(new UHFSetFragment());

        String[] names = {getString(R.string.uhf_msg_tab_scan), getString(R.string.uhf_msg_tab_local_data),
                getString(R.string.uhf_msg_tab_export), getString(R.string.uhf_msg_tab_set)};
        binding.indicator.setOnTransitionListener(getOnTabTransitionTextListener());
//        binding.indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY).setSize(13, (float) (13*1.3)));
        binding.indicator.setScrollBar(new ColorBar(this, 0xFF0B7671, 4));
        indicatorViewPager = new IndicatorViewPager(binding.indicator, binding.viewpager);
        mAdapter = new MainViewPagerAdapter(this, getSupportFragmentManager(), fragments, names);
        indicatorViewPager.setAdapter(mAdapter);
//        indicatorViewPager.setOnIndicatorItemClickListener(getOnIndicatorItemClickLis());
//        indicatorViewPager.setOnIndicatorPageChangeListener(getOnIndicatorPageChangeLis());
        indicatorViewPager.setPageOffscreenLimit(3);
        indicatorViewPager.setCurrentItem(0, false);    //默认显示tab

        startActivity(new Intent(activity, TestActivity.class));
    }

    /**
     * 设置切换文字颜色
     */
    private Indicator.OnTransitionListener getOnTabTransitionTextListener() {
        return new MyOnTransitionTextListener() {
            @Override
            public TextView getTextView(View tabItemView, int position) {

                return (TextView) tabItemView.findViewById(R.id.tv);
            }
        }.setColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.black1));
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    protected void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

}
