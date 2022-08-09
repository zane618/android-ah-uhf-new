package com.beiming.uhf_test.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.beiming.uhf_test.R;
import com.beiming.uhf_test.adapter.MainViewPagerAdapter;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.LocationBean;
import com.beiming.uhf_test.databinding.ActivityMainBinding;
import com.beiming.uhf_test.fragment.DataRecordFragment;
import com.beiming.uhf_test.fragment.ExportExcelFragment;
import com.beiming.uhf_test.fragment.UHFReadTagFragment;
import com.beiming.uhf_test.fragment.UHFSetFragment;
import com.beiming.uhf_test.helper.map.LocationHelper;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.FastJson;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.MyOnTransitionTextListener;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.beiming.uhf_test.utils.TimeUtils;
import com.hc.pda.HcPowerCtrl;
import com.kongzue.baseframework.util.SettingsUtil;
import com.pow.api.cls.RfidPower;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
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

public class UHFMainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private ActivityMainBinding binding;

    private final static String TAG = "UHFMainActivity:";
    public UHFMainActivity context;

    public boolean inventoryEpc = false;//盘存模式，EPC 或 TID
    public ArrayList<HashMap<String, String>> tagList;
    public HashMap<String, String> map;
//	public AppContext appContext;// ȫ��Context
//
    // public Reader mReader;
//	public RFIDWithUHF mReader;

//	public void playSound(int id) {
//		appContext.playSound(id);
//	}

    //合并新版本后添加的变量
    HcPowerCtrl ctrl;
    RfidPower power;
    public Reader uhfReader;

    public MutableLiveData<Boolean> initState = new MutableLiveData<>();
    public boolean isR2000 = true;
    public SettingsUtil settingsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        // 非小程序进程（这里的unimp 关键字 可以根据宿主的具体情况进行调整）
        if (!RuningAcitvityUtil.getAppName(getBaseContext()).contains("unimp")) {
            //请在此处初始化其他三方SDK
            initLocation();
        }
        //初始化uhf
        settingsUtil = new SettingsUtil("uhf");
        inventoryEpc = settingsUtil.getBoolean("inventory", false);
        initUHF();
        initSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

    }

    private void initUHF() {
        ctrl = new HcPowerCtrl();
        ctrl.identityPower(1);
        if (uhfReader == null) {
            uhfReader = new Reader();
            new InitUHFTask().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    private void exitUHF() {
        power.PowerDown();
        ctrl.identityPower(0);
        uhfReader.CloseReader();
        uhfReader = null;
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {

    }

    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    private SoundPool soundPool;

    private void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool.load(this, R.raw.beeper_short, 1));
        soundMap.put(2, soundPool.load(this, R.raw.beeper, 1));
        soundMap.put(3, soundPool.load(this, R.raw.scan_buzzer, 1));
        soundMap.put(4, soundPool.load(this, R.raw.beep330, 1));
        soundMap.put(5, soundPool.load(this, R.raw.scan_new, 1));
        soundMap.put(6, soundPool.load(this, R.raw.beep333, 1));
    }

    /**
     * 播放提示音
     *
     * @param id 成功1，失败2
     */
    public void playSound(int id) {
        soundPool.play(soundMap.get(id), 1, // 左声道音量
                1, // 右声道音量
                1, // 优先级，0为最低
                0, // 循环次数，0无不循环，-1无永远循环
                1 // 回放速度 ，该值在0.5-2.0之间，1为正常速度
        );
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
        Toast.makeText(UHFMainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitUHF();
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

    public class InitUHFTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            power = new RfidPower(RfidPower.PDATYPE.NONE, getApplicationContext());
            if (power.PowerUp()) {
//                Reader.READER_ERR reader_err = uhfReader.InitReader_Notype("/dev/ttyS1", 1);
                Reader.READER_ERR reader_err = uhfReader.InitReader_Notype("/dev/ttysWK1", 1);
                Log.e(TAG, "doInBackground: " + reader_err);
                if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                    initState.postValue(true);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mypDialog.cancel();
            if (result) {
                Reader.AntPowerConf apcf = uhfReader.new AntPowerConf();
                apcf.antcnt = 1;
                Reader.AntPowerConf apcf2 = uhfReader.new AntPowerConf();
                //获取功率
                uhfReader.ParamGet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf2);
                int session = settingsUtil.getInt("session", 1);
                //设置session 值
                uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION, new int[]{session});
                //设置附加数据内容,如果纯盘EPC 就传null
//                if (inventoryEpc) {
                uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, null);
//                } else {
//                    Reader.EmbededData_ST subjoinSet = uhfReader.new EmbededData_ST();//如果不用附加数据时 需将此参数设null ,否则会影响读取效率
//                    subjoinSet.bank = 2;//附加数据的块区，值位 0,1,2,3 对应Gen2 标签的4个区
//                    subjoinSet.startaddr = 0;//附加数据的起始读地址，字节为单位
//                    subjoinSet.bytecnt = 12;//附近数据的读取长度地址，字节为单位
//                    subjoinSet.accesspwd = null;//不用密码的时候置空
//                    uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, subjoinSet);
//                }
                Reader.HardwareDetails val = uhfReader.new HardwareDetails();
                uhfReader.GetHardwareDetails(val);
                if (val.module.toString().equals("MODOULE_SLR5300")) {
                    isR2000 = false;
                } else {
                    isR2000 = true;
                }
                Log.e(TAG, "模块: " + val.module.toString());
                Toast.makeText(getApplicationContext(), " 初始化成功,功率 " + apcf2.Powers[0].readPower / 100 + "db", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "初始化失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mypDialog = new ProgressDialog(UHFMainActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("初始化...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();

        }
    }

    //获取模块温度
    public int getModuleTemperature() {
        int[] val = new int[1];
        Reader.READER_ERR er = uhfReader.ParamGet(
                Reader.Mtr_Param.MTR_PARAM_RF_TEMPERATURE, val);
        if (er == Reader.READER_ERR.MT_OK_ERR) {
            return val[0];
        } else {
            return -1;
        }
    }
}
