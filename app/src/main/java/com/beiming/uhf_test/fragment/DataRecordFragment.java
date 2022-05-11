package com.beiming.uhf_test.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.UHFMainActivity;
import com.beiming.uhf_test.activity.login.LoginActivity;
import com.beiming.uhf_test.activity.pic.PreviewPhotoActivity;
import com.beiming.uhf_test.adapter.BoxListAdapter;
import com.beiming.uhf_test.adapter.MeterListAdapter;
import com.beiming.uhf_test.adapter.pic.AttachmentAdapter;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeterBeanDao;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.widget.ScrollGridView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DataRecordFragment extends KeyDwonFragment implements View.OnClickListener {

    @BindView(R.id.rv_box)
    RecyclerView rvBox;
    @BindView(R.id.rv_meter)
    RecyclerView rvMeter;
    Unbinder unbinder;
    @BindView(R.id.et_box)
    EditText etBox;
    @BindView(R.id.tv_box_search)
    TextView tvBoxSearch;
    @BindView(R.id.et_meter)
    EditText etMeter;
    @BindView(R.id.tv_meter_search)
    TextView tvMeterSearch;
    @BindView(R.id.tv_meter_number)
    TextView tvMeterNumber;
    @BindView(R.id.tv_addr)
    TextView tvAddr;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.noScrollgridview)
    ScrollGridView noScrollgridview;
    @BindView(R.id.ll_pic_show)
    LinearLayout llPicShow;

    private UHFMainActivity mContext;
    private List<MeasBoxBean> boxBeanList;
    private List<MeterBean> meterBeanList;
    private List<String> boxBarCodeList;//用来记录电表查询时遍历添加的箱，作用用于去重
    private BoxListAdapter boxListAdapter;
    private MeterListAdapter meterAdapter;
    private MeasBoxBeanDao measBoxBeanDao;
    private List<PhotoBean> photoBeanList;//图片集合
    private AttachmentAdapter attachmentAdapter;
    private static final int CHOOSE_PIC_MAX = 10;

    private String Tag = LoginActivity.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("CMCC", Tag+":"+System.currentTimeMillis());
        View view = inflater.inflate(R.layout.activity_data_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (UHFMainActivity) getActivity();
        initAdapter();
        initListener();
    }

    private int creatOrDetails = 0;

    private void initListener() {
        tvBoxSearch.setOnClickListener(this);
        tvMeterSearch.setOnClickListener(this);

        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                creatOrDetails = 1;
                if (i == photoBeanList.size()) {
                    /*if (!PermissionUtils.isPermissionsGranted(mContext)) {
                        PermissionUtils.getPermissions(mContext, MY_PERMISSIONS_REQUEST);
                    } else {
                        choosePic();
                    }*/
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

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
        boxBeanList = measBoxBeanDao.loadAll();
        changeRightData(0);
    }

    private void initAdapter() {
        rvBox.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        boxBeanList = new ArrayList<>();
        meterBeanList = new ArrayList<>();
        photoBeanList = new ArrayList<>();

//        getTestData();

        boxListAdapter = new BoxListAdapter(R.layout.item_box_list, boxBeanList);
        boxListAdapter.setContext(mContext);
        rvBox.setAdapter(boxListAdapter);

        boxListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (boxListAdapter.getSelectedPosition() != position) {
//                    boxListAdapter.setSelectedPosition(position);
//                    boxListAdapter.notifyDataSetChanged();
                    //改变右侧布局数据
                    changeRightData(position);
                }
            }
        });

        rvMeter.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        meterAdapter = new MeterListAdapter(R.layout.item_meter_list, meterBeanList);
        meterAdapter.setContext(mContext);
        rvMeter.setAdapter(meterAdapter);

        meterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showToast("当前索引：" + position);
            }
        });

        //图片列表
        //选择的图片列表
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        attachmentAdapter = new AttachmentAdapter(context, photoBeanList, CHOOSE_PIC_MAX, -1);
        noScrollgridview.setAdapter(attachmentAdapter);
    }

    //切换右侧布局数据
    private void changeRightData(int position) {
        if (boxBeanList.size() == 0) {
            meterBeanList.clear();
            photoBeanList.clear();
            tvAddr.setText("无");
        } else {
            MeasBoxBean measBoxBean = boxBeanList.get(position);
            meterBeanList = measBoxBean.getMeters();
            photoBeanList = measBoxBean.getBoxImages();
            tvAddr.setText(measBoxBean.getInstAddr());
        }
        //刷新右侧表列表
        meterAdapter.setNewData(meterBeanList);
        tvMeterNumber.setText("表数：" + meterBeanList.size() + "个");
        //刷新左侧箱列表
        boxListAdapter.setSelectedPosition(position);
        boxListAdapter.setNewData(boxBeanList);
        //刷新右侧图片列表
        if (photoBeanList == null) {
            photoBeanList = new ArrayList<>();
        }
        attachmentAdapter.modifyData(photoBeanList);
    }

    private void getTestData() {
        for (int i = 0; i < 20; i++) {
            MeasBoxBean boxBean = new MeasBoxBean();
            boxBean.setBarCode("箱：343000545123456789011" + i);
            boxBeanList.add(boxBean);
        }
        for (int i = 0; i < 12; i++) {
            MeterBean meterBean = new MeterBean();
            meterBean.setBarCode("表:343000545123456789999" + i);
            meterBeanList.add(meterBean);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_box_search:
                queryBoxData();
                break;
            case R.id.tv_meter_search:
                queryMeterData();
                break;
        }

    }

    //查询箱列表
    private void queryBoxData() {
        String boxContent = etBox.getText().toString().trim();
        MeasBoxBeanDao measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
        if (TextUtils.isEmpty(boxContent)) {
            boxBeanList = measBoxBeanDao.loadAll();
        } else {
            boxBeanList = measBoxBeanDao.queryBuilder().where(MeasBoxBeanDao.Properties.BarCode.like("%" + boxContent + "%")).build().list();
        }
        changeRightData(0);
//        boxListAdapter.setSelectedPosition(0);
//        boxListAdapter.setNewData(boxBeanList);
    }

    //查询表
    private void queryMeterData() {
        String meterContent = etMeter.getText().toString().trim();
        MeterBeanDao meterBeanDao = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao();
        if (TextUtils.isEmpty(meterContent)) {
            showToast("请输入电表条形码");
            return;
        }
        //查询到的所有电表
        List<MeterBean> queryMeterList = meterBeanDao.queryBuilder().where(MeterBeanDao.Properties.BarCode.like("%" + meterContent + "%")).build().list();
        //遍历查询到的所有电表，获取表箱集合
        //无匹配数据时，直接刷新数据
        if (queryMeterList.size() == 0) {
            boxBeanList.clear();
            changeRightData(0);
//            boxListAdapter.setSelectedPosition(0);
//            boxListAdapter.setNewData(boxBeanList);
            return;
        }
        //有匹配数据时，先清楚原有箱，再遍历添加箱，需要去重
        MeasBoxBeanDao measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
        boxBeanList.clear();
        List<MeasBoxBean> boxList;
        for (int i = 0; i < queryMeterList.size(); i++) {
            boxList = measBoxBeanDao.queryBuilder().where(MeasBoxBeanDao.Properties.BarCode.eq(queryMeterList.get(i).getMeasBarCode())).build().list();
            boxBeanList.addAll(boxList);
        }
        //去重
        Set<MeasBoxBean> userSet = new HashSet<>(boxBeanList);
        boxBeanList = new ArrayList<>(userSet);

        //刷新页面数据
        changeRightData(0);
//        boxListAdapter.setSelectedPosition(0);
//        boxListAdapter.setNewData(boxBeanList);
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
        if (ConstantUtil.CLEAR_READ_TAG_DATA.equals(attachmentUpdate.getTag())) {
            //todo 本地保存成功，此处清除数据数据
            boxListAdapter.setSelectedPosition(0);
            initData();
        }
    }
}
