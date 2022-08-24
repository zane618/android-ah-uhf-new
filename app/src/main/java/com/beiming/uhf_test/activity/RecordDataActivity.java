package com.beiming.uhf_test.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.pic.PhotoPickerActivity;
import com.beiming.uhf_test.activity.pic.PreviewPhotoActivity;
import com.beiming.uhf_test.adapter.pic.AttachmentAdapter;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeterBeanDao;
import com.beiming.uhf_test.helper.map.LocationHelper;
import com.beiming.uhf_test.listener.OnHintDialogClicklistener;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.DialogUtils;
import com.beiming.uhf_test.utils.FastJson;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.PermissionUtils;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.beiming.uhf_test.utils.ToastUtils;
import com.beiming.uhf_test.view.BoxSizeInputLayout;
import com.beiming.uhf_test.view.DefectInputLayout;
import com.beiming.uhf_test.view.DoorInfoInputLayout;
import com.beiming.uhf_test.view.picinput.PictureInputLayout;
import com.beiming.uhf_test.widget.ScrollGridView;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.feature.sdk.DCUniMPSDK;
import io.dcloud.feature.sdk.Interface.IOnUniMPEventCallBack;
import io.dcloud.feature.sdk.Interface.IUniMP;
import io.dcloud.feature.unimp.DCUniMPJSCallback;
import io.dcloud.feature.unimp.config.UniMPOpenConfiguration;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 记录数据并保存的页面
 *
 * @author Yuz created at 2021/6/10 17:43
 */
public class RecordDataActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {


    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.tv_addr)
    TextView tvAddr;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.iv_back)
    FrameLayout ivBack;
    @BindView(R.id.tv_xiang_bh)//箱号
    TextView tv_xiang_bh;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.rg_caizhi)
    RadioGroup rg_caizhi;
    @BindView(R.id.tv_x_luru)
    TextView tv_x_luru;
    @BindView(R.id.doorInfoLayout)
    DoorInfoInputLayout doorInfoInputLayout;
    @BindView(R.id.defect)
    DefectInputLayout defect;
    @BindView(R.id.picture_input_layout)
    PictureInputLayout pictureInputLayout;

    private List<PhotoBean> photoBeanList = new ArrayList<>();//图片集合
    private static final int CHOOSE_PIC_MAX = 10;
    private int MY_PERMISSIONS_REQUEST = 10011;//图片请求码
    private static final int PICK_PHOTO = 101;
    private MeasBoxBean boxBean;
    private RdAdapter rdAdapter;
    private String caizhi = "金属";
    private BoxSizeInputLayout boxSizeInputLayout;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_record_data);
    }

    @Override
    protected void initView() {
        boxSizeInputLayout = findViewById(R.id.boxSizeInputLayout);
        boxBean = (MeasBoxBean) getIntent().getSerializableExtra("box");
        rdAdapter = new RdAdapter(boxBean.getMeters());
        recycleview.setNestedScrollingEnabled(false);
        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(new LinearLayoutManager(activity));
        recycleview.setAdapter(rdAdapter);
        rg_caizhi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.caizhi_Y == i) {
                    caizhi = "金属";
                } else {
                    caizhi = "非金属";
                }
            }
        });
        tv_x_luru.setOnClickListener(this);
        tvTitleName.setText("现场信息录入");
    }

    private int creatOrDetails = 0;

    @Override
    protected void initListener() {
        btSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void choosePic() {
        int havePicSize = photoBeanList.size();
        if (CHOOSE_PIC_MAX > havePicSize) {
            int chooseNum = CHOOSE_PIC_MAX - havePicSize;
            //选择图片
            Intent intent = new Intent(activity, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, chooseNum);

            // 总共选择的图片数量
            activity.startActivity(intent);

        } else {
            ToastUtils.showToast(activity, "选择已经达到上限");
        }
    }

    @Override
    protected void initData() {
        tv_xiang_bh.setText(boxBean.getBarCode());

        //展示定位数据
        new LocationHelper().setListener(locationBean -> {
            if (null != locationBean) {
                LogPrintUtil.zhangshi("定位成功 getNewAppLocation:" + FastJson.toJSONString(locationBean));
                SharedPreferencesUtil.getInstance().setObjectToShare(ConstantUtil.LAST_LOCATION, locationBean);

                if (locationBean != null && locationBean.getAddress() != null) {
                    tvAddr.setText(locationBean.getAddress());
                    boxBean.setInstAddr(locationBean.getAddress());
                    boxBean.setGps_X(locationBean.getLongitude());
                    boxBean.setGps_Y(locationBean.getLatitude());
                    boxBean.setGps_Z(locationBean.getAltitude());
                } else {
                    tvAddr.setText("暂未获取到定位信息");
                }
            } else {
                //定位失败
                tvAddr.setText("暂未获取到定位信息");
                LogPrintUtil.zhangshi("定位失败 getNewAppLocation:" + FastJson.toJSONString(locationBean));
            }
        }).startLocation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                //
                DialogUtils.showHintDialog(this, "确认保存吗", "取消", "确定", new OnHintDialogClicklistener() {
                    @Override
                    public void onConfirm() {
                        //本地保存
                        saveData();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                break;
            case R.id.tv_x_luru:
                //打开小程序
                startUniSdk();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 本地保存
     */
    private void saveData() {
        if (null == pictureInputLayout.getPhotoBeans()) {
            return;
        }
        photoBeanList.clear();
        photoBeanList.addAll(pictureInputLayout.getPhotoBeans());
        boxBean.setNote(etNote.getText().toString());
        boxBean.setHasQx(defect.getDj());
        boxBean.setQxDetail(defect.getKindsDetail());
        boxBean.setCaizhi(caizhi);
        //四门尺寸
        boxBean.setZsGao(doorInfoInputLayout.getZsGao());
        boxBean.setZsKuan(doorInfoInputLayout.getZsKuan());
        boxBean.setZxGao(doorInfoInputLayout.getZxGao());
        boxBean.setZxKuan(doorInfoInputLayout.getZxKuan());
        boxBean.setYsGao(doorInfoInputLayout.getYsGao());
        boxBean.setYsKuan(doorInfoInputLayout.getYsKuan());
        boxBean.setYxGao(doorInfoInputLayout.getYxGao());
        boxBean.setYxKuan(doorInfoInputLayout.getYxKuan());
        boxBean.setBoxImages(photoBeanList);
        if (TextUtils.isEmpty(boxSizeInputLayout.getGao())) {
            boxBean.setGao("未填写");
        } else {
            boxBean.setGao(boxSizeInputLayout.getGao());
        }
        if (TextUtils.isEmpty(boxSizeInputLayout.getKuan())) {
            boxBean.setKuan("未填写");
        } else {
            boxBean.setKuan(boxSizeInputLayout.getKuan());
        }
        //保存箱
        MeasBoxBeanDao measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
        int size = measBoxBeanDao.loadAll().size();
        if (size == 0) {
            boxBean.setMeasBoxId((long) 0);
        }
        measBoxBeanDao.insert(boxBean);
        //保存表
        MeterBeanDao meterBeanDao = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao();
        int meterSize = measBoxBeanDao.loadAll().size();
        List<MeterBean> meters = boxBean.getMeters();
        for (int i = 0; i < meters.size(); i++) {
            if (meterSize == 0 && i == 0) {
                meters.get(i).setMeterId((long) 0);
            }
            meters.get(i).setMeasBarCode(boxBean.getBarCode());
            meterBeanDao.insert(meters.get(i));
        }
        AttachmentUpdate attachmentUpdate = new AttachmentUpdate();
        attachmentUpdate.setTag(ConstantUtil.CLEAR_READ_TAG_DATA);
        EventBus.getDefault().post(attachmentUpdate);
        showToast("保存成功");

        List<MeterBean> biao = meterBeanDao.loadAll();
        for (MeterBean item : biao) {
            LogPrintUtil.zhangshi("表:" + item.toString());
        }
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DCUniMPSDK.getInstance().setOnUniMPEventCallBack(new IOnUniMPEventCallBack() {
            @Override
            public void onUniMPEventReceive(String appid, String event, Object data, DCUniMPJSCallback callback) {
                Log.d("cs", "onUniMPEventReceive    event=" + event);
                if ("completeFileCheck".equals(event)) {
                    JSONObject ajo = (JSONObject) data;
                    String listStr = ajo.getString("list");
                    Log.d("cs", "onUniMPEventReceive    listStr=" + ajo.getString("list"));
                    List<MeterBean> tmpMeters = FastJson.parseArray(listStr, MeterBean.class);
                    //刷新电能表列表，abc 相位
                    List<MeterBean> meterBeans = boxBean.getMeters();
                    for (int i = 0; i < meterBeans.size(); i++) {
                        for (int j = 0; j < tmpMeters.size(); j++) {
                            if (meterBeans.get(i).getBarCode().equals(tmpMeters.get(j).getBarCode())) {
                                meterBeans.get(i).setPhase(tmpMeters.get(j).getPhase());
                                break;
                            }
                        }
                    }
                    if (rdAdapter != null) {
                        rdAdapter.notifyDataSetChanged();
                    }

                    //回传数据给小程序er
                    callback.invoke("收到消息");
                }

            }
        });
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
        if (requestCode == MY_PERMISSIONS_REQUEST) {//权限请求成功导，出所有word
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePic();
            } else {
                Toast.makeText(RecordDataActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (requestCode == ConstantUtil.NULL_PERMISSIONS_REQUEST) {//进入界面时的权限请求
            return;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    /**
     * 打开uni小程序
     */
    private void startUniSdk() {
        //判断uni-sdk是否初始化成功
        if (DCUniMPSDK.getInstance().isInitialize()) {
            // 启动小程序并传入参数 "Hello uni microprogram"
            try {
                List<MeterBean> meters = null;
                if (boxBean != null) {
                    meters = boxBean.getMeters();
                }
                UniMPOpenConfiguration uniMPOpenConfiguration = new UniMPOpenConfiguration();
                uniMPOpenConfiguration.extraData.put("MSG", JSONObject.toJSON(meters));
                SoftReference<IUniMP> mallMP = new SoftReference<>(DCUniMPSDK.getInstance()
                        .openUniMP(RecordDataActivity.this, "__UNI__6411EDB", uniMPOpenConfiguration));
//                mallMP.get().closeUniMP();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showToast("uni-sdk未完成初始化");
        }

    }


}