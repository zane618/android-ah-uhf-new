package com.beiming.uhf_test.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.beiming.uhf_test.utils.PermissionUtils;
import com.beiming.uhf_test.utils.SharedPreferencesUtil;
import com.beiming.uhf_test.utils.ToastUtils;
import com.beiming.uhf_test.widget.ScrollGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

    private List<PhotoBean> photoBeanList = new ArrayList<>();//图片集合
    private AttachmentAdapter attachmentAdapter;
    private MeasBoxBean measBoxBean;
    private static final int CHOOSE_PIC_MAX = 10;
    private int MY_PERMISSIONS_REQUEST = 10011;//图片请求码
    private static final int PICK_PHOTO = 101;
    private MeasBoxBean boxBean;

    @Override
    protected int onCreateView() {
        return R.layout.activity_record_data;
    }

    @Override
    protected void initView() {
        boxBean = (MeasBoxBean) getIntent().getSerializableExtra("box");
        initAdapter();
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
                        finish();
                        showToast("保存成功");
                    }

                    @Override
                    public void onCancel() {

                    }
                });

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
        boxBean.setBoxImages(photoBeanList);
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        EventBus.getDefault().register(this);

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
}