package com.beiming.uhf_test.activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.LocationBean;
import com.beiming.uhf_test.fragment.DataRecordFragment;
import com.beiming.uhf_test.fragment.ExportExcelFragment;
import com.beiming.uhf_test.fragment.UHFReadTagFragment;
import com.beiming.uhf_test.fragment.UHFSetFragment;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.beiming.uhf_test.utils.TimeUtils;
import com.hc.pda.HcPowerCtrl;
import com.kongzue.baseframework.util.SettingsUtil;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
public class UHFMainActivity extends BaseTabFragmentActivity implements AMapLocationListener, EasyPermissions.PermissionCallbacks {

    private final static String TAG = "UHFMainActivity:";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//			appContext = (AppContext) getApplication();
        initViewPageData();
        initViewPager();
        initTabs();

        // 非小程序进程（这里的unimp 关键字 可以根据宿主的具体情况进行调整）
//        if (!RuningAcitvityUtil.getAppName(getBaseContext()).contains("unimp")) {
            //请在此处初始化其他三方SDK
            initLocation();
//        }
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
    protected void initViewPageData() {
        lstFrg.add(new UHFReadTagFragment());
        lstFrg.add(new DataRecordFragment());
        lstFrg.add(new ExportExcelFragment());
        lstFrg.add(new UHFSetFragment());


        lstTitles.add(getString(R.string.uhf_msg_tab_scan));
        lstTitles.add(getString(R.string.uhf_msg_tab_local_data));
        lstTitles.add(getString(R.string.uhf_msg_tab_export));
        lstTitles.add(getString(R.string.uhf_msg_tab_set));

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
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;

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
        try {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(5000);
            mLocationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mLocationOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
			/*mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
			mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
			mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
			mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
			mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
			mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
			mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
			AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
			mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
			mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
			mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
			mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）*/
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //启动定位
            mlocationClient.startLocation();
        } catch (Exception e) {

        }
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

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        try {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    LocationBean lastLocationBean = new LocationBean();
                    lastLocationBean.setLongitude(amapLocation.getLongitude() + "");
                    lastLocationBean.setLatitude(amapLocation.getLatitude() + "");
                    lastLocationBean.setAddress(amapLocation.getAddress());
                    lastLocationBean.setTime(TimeUtils.getTime());

                    SharedPreferencesUtil.getInstance().setObjectToShare(ConstantUtil.LAST_LOCATION, lastLocationBean);
                    Log.i("CMCC", "定位信息" + lastLocationBean.toString());
					/*//获取当前定位结果来源，如网络定位结果，详见定位类型表
					Log.i("定位类型", amapLocation.getLocationType() + "");
					Log.i("获取纬度", amapLocation.getLatitude() + "");
					Log.i("获取经度", amapLocation.getLongitude() + "");
					Log.i("获取精度信息", amapLocation.getAccuracy() + "");

					//如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
					Log.i("地址", amapLocation.getAddress());
					Log.i("国家信息", amapLocation.getCountry());
					Log.i("省信息", amapLocation.getProvince());
					Log.i("城市信息", amapLocation.getCity());
					Log.i("城区信息", amapLocation.getDistrict());
					Log.i("街道信息", amapLocation.getStreet());
					Log.i("街道门牌号信息", amapLocation.getStreetNum());
					Log.i("城市编码", amapLocation.getCityCode());
					Log.i("地区编码", amapLocation.getAdCode());
					Log.i("获取当前定位点的AOI信息", amapLocation.getAoiName());
					Log.i("获取当前室内定位的建筑物Id", amapLocation.getBuildingId());
					Log.i("获取当前室内定位的楼层", amapLocation.getFloor());
					Log.i("获取GPS的当前状态", amapLocation.getGpsAccuracyStatus() + "");

					//获取定位时间
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(amapLocation.getTime());

					Log.i("获取定位时间", df.format(date));*/


                    // 停止定位
//					mlocationClient.stopLocation();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        } catch (Exception e) {
            Log.i("CMCC", "定位异常" + e.getMessage());
        }
    }

    private void showToast(String string) {
        Toast.makeText(UHFMainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exitUHF();
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
