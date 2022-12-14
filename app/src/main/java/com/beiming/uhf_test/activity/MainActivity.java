package com.beiming.uhf_test.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.TestActivity;
import com.beiming.uhf_test.activity.fenzhix.ReadFzxActivity;
import com.beiming.uhf_test.activity.fenzhix.gj.GjFzxActivity;
import com.beiming.uhf_test.adapter.MainViewPagerAdapter;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.databinding.ActivityMainBinding;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.fragment.DataRecordFragment;
import com.beiming.uhf_test.fragment.ExportExcelFragment;
import com.beiming.uhf_test.fragment.UHFReadTagFragment;
import com.beiming.uhf_test.fragment.UHFSetFragment;
import com.beiming.uhf_test.greendao.gen.FenzhiBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeterBeanDao;
import com.beiming.uhf_test.helper.map.LocationHelper;
import com.beiming.uhf_test.library.LibActivity;
import com.beiming.uhf_test.library.LibCheckActivity;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.FastJson;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.MyOnTransitionTextListener;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.beiming.uhf_test.utils.TimeUtils;
import com.beiming.uhf_test.utils.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.AttachListPopupView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.dcloud.common.util.RuningAcitvityUtil;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * UHF??????demo
 * <p>
 * 1??????????????????????????????????????????????????????
 * 2?????????????????????????????????\libs\armeabi\????????????libDeviceAPI.so??????????????????\libs\???????????????DeviceAPIver20160728.jar?????????
 * 3????????????????????????????????? init()????????????????????????????????? free() ????????????
 * <p>
 * <p>
 * ????????????????????????????????????API????????????
 *
 * @author ????????? 2016???8???9???
 */

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    //layout
    private ActivityMainBinding binding;

    private final static String TAG = "UHFMainActivity:";

    public ArrayList<HashMap<String, String>> tagList;
    public HashMap<String, String> map;
    private AttachListPopupView attachListPopupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ??????????????????????????????unimp ????????? ????????????????????????????????????????????????
        if (!RuningAcitvityUtil.getAppName(getBaseContext()).contains("unimp")) {
            //?????????????????????????????????SDK
            initLocation();
        }
        // TODO: 2022/8/23 ???????????????
//        RfidHelper.getInstance();

        /*List a = new ArrayList();
        a.add("/sdcard/UHF/image/2022-09-29/IMG_20220921122557851.jpeg");
        Luban.with(this)
                .load(a)
                .ignoreBy(100)
                .setTargetDir(ConstantUtil.IMAGE_STR + TimeUtils.getY_M_D_Time())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        LogPrintUtil.zhangshi("??????onStart:");
                    }

                    @Override
                    public void onSuccess(int index, File compressFile) {
                        LogPrintUtil.zhangshi("?????? onSuccess:" + compressFile.getAbsolutePath());
                    }

                    @Override
                    public void onError(int index, Throwable e) {
                        LogPrintUtil.zhangshi("??????onError:");
                    }

                }).launch();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

//        startActivity(new Intent(activity, TestActivity.class));

//        MeasBoxBean boxBean = new MeasBoxBean();
//        boxBean.setBarCode("barcode010209129299");
//        boxBean.setScanTime("2022-08.15");
//        boxBean.setTs(System.currentTimeMillis());
//        boxBean.setGps_X("100010001000100011");
//        boxBean.setGps_Y("200020002000200022");
//        boxBean.setInstAddr("?????????????????????????????????");
//        boxBean.setInstLoc("????????????");
//        boxBean.setDescribe("??????");
//        boxBean.setTmnlAddr("????????????");
//        boxBean.setTgName("????????????");
//        boxBean.setBoxRows("??????");
//        boxBean.setBoxCols("??????");
//        boxBean.setNote("??????");
//        boxBean.setHasQx("??????");
//        boxBean.setQxDetail("(1)???????????????????????????????????????;\n" +
//                "(2)????????????????????????;\n" +
//                "(3)?????????????????????????????????;\n" +
//                "(4)???????????????????????????;\n" +
//                "(5)???????????????????????????????????????\n");
//        boxBean.setFenzhixCode("?????????????????????");
//        boxBean.setCaizhi("?????????");
//        boxBean.setGao("888");
//        boxBean.setKuan("666");
//        Intent intent = new Intent(activity, RecordDataActivity.class);
//        intent.putExtra("box", boxBean);
//        startActivity(intent);

//        startActivity(new Intent(activity, ReadFzxActivity.class));
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {

    }

    private static final int MY_PERMISSIONS_REQUEST_CALL_LOCATION = 1;

    //?????????????????????
    private void initLocation() {
        //????????????????????????M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_CALL_LOCATION);
            } else {
                //"???????????????";
                showLocation();
            }
        }
    }

    private void showLocation() {
        new LocationHelper().setListener(locationBean -> {
            if (null != locationBean) {
                LogPrintUtil.zhangshi("???????????? getNewAppLocation:" + FastJson.toJSONString(locationBean));
                SharedPreferencesUtil.getInstance().setObjectToShare(ConstantUtil.LAST_LOCATION, locationBean);
            } else {
                //????????????
                LogPrintUtil.zhangshi("???????????? getNewAppLocation:" + FastJson.toJSONString(locationBean));
            }
        }).startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //"???????????????"
                showLocation();
            } else {
                showToast("???????????????,????????????");

            }
        }
    }


    public void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: 2022/8/23 ???????????????
//        RfidHelper.getInstance().exitUHF();
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
        indicatorViewPager.setCurrentItem(0, false);    //????????????tab
        binding.tvMenu.setOnClickListener(view -> {
            attachListPopupView = new XPopup.Builder(activity)
                    .hasShadowBg(false)
                    .isCenterHorizontal(false)
                    .atView(binding.tvMenu)  // ?????????????????????View???????????????????????????????????????????????????
                    .autoDismiss(false)
                    .asAttachList(new String[]{"???????????????", "???????????????", "????????????", "clear", "test"},
                            new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
                            (position, text) -> attachListPopupView.dismissWith(() -> {
                                ToastUtils.showToast(text);
                                if ("???????????????".equals(text)) {
                                    startActivity(new Intent(activity, ReadFzxActivity.class));
                                } else if ("???????????????".equals(text)) {
                                    GjFzxActivity.Companion.startActivity(activity);
                                } else if ("????????????".equals(text)) {
                                    LibCheckActivity.startActivity(activity);
                                } else if ("clear".equals(text)) {
                                    MeasBoxBeanDao measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
                                    measBoxBeanDao.deleteAll();
                                    MeterBeanDao meterBeanDao = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao();
                                    meterBeanDao.deleteAll();
                                    FenzhiBoxBeanDao fenzhiBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getFenzhiBoxBeanDao();
                                    fenzhiBoxBeanDao.deleteAll();
                                    GreenDaoManager.getInstance().getNewSession().getLibAssetBeanDao().deleteAll();
                                    AttachmentUpdate attachmentUpdate = new AttachmentUpdate();
                                    attachmentUpdate.setTag(ConstantUtil.CLEAR_READ_TAG_DATA);
                                    EventBus.getDefault().post(attachmentUpdate);
                                } else if ("test".equals(text)) {
                                    startActivity(new Intent(activity, TestActivity.class));
                                }
                            }), 0, 0, Gravity.LEFT);
            attachListPopupView.show();
        });

    }

    /**
     * ????????????????????????
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
        //s/dfsdf
    }

    /**
     * ????????????????????????
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //????????????????????????
        }
        return false;
    }

    /**
     * ??????????????????
     */
    private static Boolean isExit = false;

    protected void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // ????????????
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // ????????????
                }
            }, 2000); // ??????2?????????????????????????????????????????????????????????????????????????????????

        } else {
            finish();
            System.exit(0);
        }
    }

}
