package com.beiming.uhf_test.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.pic.PhotoPickerActivity;
import com.beiming.uhf_test.activity.pic.PreviewPhotoActivity;
import com.beiming.uhf_test.adapter.pic.AttachmentAdapter;
import com.beiming.uhf_test.base.BaseActivity;
import com.beiming.uhf_test.bean.LocationBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeterBeanDao;
import com.beiming.uhf_test.listener.OnHintDialogClicklistener;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.DialogUtils;
import com.beiming.uhf_test.utils.FastJson;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.PermissionUtils;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.beiming.uhf_test.utils.ToastUtils;
import com.beiming.uhf_test.view.DoorInfoInputLayout;
import com.beiming.uhf_test.widget.ScrollGridView;

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
    @BindView(R.id.noScrollgridview)
    ScrollGridView noScrollgridview;
    @BindView(R.id.ll_pic_show)
    LinearLayout llPicShow;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.iv_back)
    FrameLayout ivBack;
    @BindView(R.id.tv_xiang_bh)//箱号
    TextView tv_xiang_bh;
    @BindView(R.id.et_xiang_chang)
    EditText et_xiang_chang;//长
    @BindView(R.id.tv_xiang_kuan)
    EditText tv_xiang_kuan;//宽
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.radioGroup_qx)
    RadioGroup radioGroup_qx;
    @BindView(R.id.ll_quexian)
    LinearLayout ll_quexian;
    @BindView(R.id.jiaolian_y)
    RadioButton jiaolian_y; //铰链
    @BindView(R.id.laogu_y)
    RadioButton laogu_y; //牢固
    @BindView(R.id.suo_y)
    RadioButton suo_y; //锁
    @BindView(R.id.zawu_y)
    RadioButton zawu_y; //杂物
    @BindView(R.id.rb_qx_you)
    RadioButton rb_qx_you; //缺陷
    @BindView(R.id.rg_caizhi)
    RadioGroup rg_caizhi;
    @BindView(R.id.tv_x_luru)
    TextView tv_x_luru;
    @BindView(R.id.doorInfoLayout)
    DoorInfoInputLayout doorInfoInputLayout;

    private List<PhotoBean> photoBeanList = new ArrayList<>();//图片集合
    private AttachmentAdapter attachmentAdapter;
    private static final int CHOOSE_PIC_MAX = 10;
    private int MY_PERMISSIONS_REQUEST = 10011;//图片请求码
    private static final int PICK_PHOTO = 101;
    private MeasBoxBean boxBean;
    private RdAdapter rdAdapter;
    private String caizhi = "金属";

    @Override
    protected int onCreateView() {
        return R.layout.activity_record_data;
    }

    @Override
    protected void initView() {
        boxBean = (MeasBoxBean) getIntent().getSerializableExtra("box");
        initAdapter();
        rdAdapter = new RdAdapter(boxBean.getMeters());
        recycleview.setNestedScrollingEnabled(false);
        recycleview.setHasFixedSize(true);
        recycleview.setLayoutManager(new LinearLayoutManager(context));
        recycleview.setAdapter(rdAdapter);
        radioGroup_qx.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (R.id.rb_qx_you == i) {
                    //有缺陷
                    ll_quexian.setVisibility(View.VISIBLE);
                } else {//无缺陷
                    ll_quexian.setVisibility(View.GONE);
                }
            }
        });
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
    }

    private void initAdapter() {
        //选择的图片列表
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        attachmentAdapter = new AttachmentAdapter(context, photoBeanList, CHOOSE_PIC_MAX, 0);
        noScrollgridview.setAdapter(attachmentAdapter);
    }

    @Override
    protected void initToolbar() {
        tvTitleName.setText("现场信息录入");
    }

    private int creatOrDetails = 0;

    @Override
    protected void initListener() {
        btSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                creatOrDetails = 0;
                if (i == photoBeanList.size()) {
                    if (!PermissionUtils.isPermissionsGranted(RecordDataActivity.this)) {
                        PermissionUtils.getPermissions(RecordDataActivity.this, MY_PERMISSIONS_REQUEST);
                    } else {
                        choosePic();
                    }
                } else {
                    Intent intent = new Intent(context, PreviewPhotoActivity.class);
                    intent.putExtra("ID", i);
                    intent.putExtra(ConstantUtil.CREAT_OR_DETAILS, creatOrDetails);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantUtil.PHOTO_BEAN_LIST, (Serializable) photoBeanList);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }

    private void choosePic() {
        int havePicSize = photoBeanList.size();
        if (CHOOSE_PIC_MAX > havePicSize) {
            int chooseNum = CHOOSE_PIC_MAX - havePicSize;
            //选择图片
            Intent intent = new Intent(context, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, chooseNum);

            // 总共选择的图片数量
            context.startActivity(intent);

        } else {
            ToastUtils.showToast(context, "选择已经达到上限");
        }
    }

    @Override
    protected void initData() {
        //展示定位数据
        LocationBean lastLocationBean = (LocationBean) SharedPreferencesUtil.getInstance().getObjectFromShare(ConstantUtil.LAST_LOCATION);
        if (lastLocationBean != null && lastLocationBean.getAddress() != null) {
            tvAddr.setText(lastLocationBean.getAddress());
            boxBean.setInstAddr(lastLocationBean.getAddress());
            boxBean.setGps_X(lastLocationBean.getLongitude());
            boxBean.setGps_Y(lastLocationBean.getLatitude());
            boxBean.setGps_Z("");
        } else {
            tvAddr.setText("暂未获取到定位信息");
        }
        tv_xiang_bh.setText(boxBean.getBarCode());
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
        if (photoBeanList == null || photoBeanList.size() == 0) {
            showToast("图片不能为空");
            return;
        }

        boxBean.setNote(etNote.getText().toString());
        boxBean.setHasQx(rb_qx_you.isChecked() ? "有" : "无");
        boxBean.setQxJiaolian(jiaolian_y.isChecked() ? "是" : "否");
        boxBean.setQxLaogu(laogu_y.isChecked() ? "是" : "否");
        boxBean.setQxSuo(suo_y.isChecked() ? "是" : "否");
        boxBean.setQxZawu(zawu_y.isChecked() ? "是" : "否");
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
        if (TextUtils.isEmpty(et_xiang_chang.getText().toString())) {
            boxBean.setChang("未填写");
        } else {
            boxBean.setChang(et_xiang_chang.getText().toString());
        }
        if (TextUtils.isEmpty(tv_xiang_kuan.getText().toString())) {
            boxBean.setKuan("未填写");
        } else {
            boxBean.setKuan(tv_xiang_kuan.getText().toString());
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
        // TODO: add setContentView(...) invocation
        EventBus.getDefault().register(this);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //用eventBus接收电表bean
    @Subscribe
    public void onRefreshList(AttachmentUpdate attachmentUpdate) {
        if (ConstantUtil.REFRESH_PIC_RES_LIST_FROM_CAMERA.equals(attachmentUpdate.getTag())) {
            //todo 此处刷新数据
            if (photoBeanList == null)
                photoBeanList = new ArrayList<>();
            for (PhotoBean photoBean : attachmentUpdate.getPhotoBeans()) {
                photoBeanList.add(photoBean);
            }
            attachmentAdapter.modifyData(photoBeanList);
        }
        //图片预览界面返回
        if (ConstantUtil.REFRESH_PIC_DES_LIST_FROM_PREVIEW.equals(attachmentUpdate.getTag())) {
            //todo 此处刷新数据
            photoBeanList.clear();
            photoBeanList.addAll(attachmentUpdate.getPhotoBeans());
            attachmentAdapter.modifyData(photoBeanList);
        }
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