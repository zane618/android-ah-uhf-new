package com.beiming.uhf_test.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.MainActivity;
import com.beiming.uhf_test.activity.fenzhix.FenzhixActivity;
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
import com.beiming.uhf_test.view.DoorInfoShowLayout;
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
    @BindView(R.id.et_search)
    EditText etSearch;
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

    @BindView(R.id.tv_chang)
    TextView tv_chang;
    @BindView(R.id.tv_kuan)
    TextView tv_kuan;
    @BindView(R.id.tv_caizhi)
    TextView tv_caizhi;
    @BindView(R.id.tv_qx_detail) //缺陷详情
    TextView tv_qx_detail;

    @BindView(R.id.content)
    LinearLayout content;

    @BindView(R.id.doorInfoLayout)
    DoorInfoShowLayout doorInfoLayout;
    private TextView tv_fenzhix_tianjia;
    private TextView tv_fenzhix_bianma;


    private MainActivity mContext;
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
        Log.i("CMCC", Tag + ":" + System.currentTimeMillis());
        View view = inflater.inflate(R.layout.activity_data_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tv_fenzhix_tianjia = view.findViewById(R.id.tv_fenzhix_tianjia);
        tv_fenzhix_tianjia.setOnClickListener(v -> { //添加分支箱
            MeasBoxBean boxBean = boxListAdapter.getData().get(boxListAdapter.getSelectedPosition());
            FenzhixActivity.Companion.startActivity(mContext, boxBean);
        });
        tv_fenzhix_bianma = view.findViewById(R.id.tv_fenzhix_bianma);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        initAdapter();
        initListener();
        getData();
    }

    private int creatOrDetails = 0;

    private void initListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                queryMeterData(editable.toString());
            }
        });
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
                    Intent intent = new Intent(DataRecordFragment.this.mContext, PreviewPhotoActivity.class);
                    intent.putExtra("ID", i);
                    intent.putExtra(ConstantUtil.CREAT_OR_DETAILS, creatOrDetails);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantUtil.PHOTO_BEAN_LIST, (Serializable) photoBeanList);
                    intent.putExtras(bundle);
                    DataRecordFragment.this.mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
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
        rvMeter.setNestedScrollingEnabled(false);
        rvMeter.setHasFixedSize(true);
        rvMeter.setLayoutManager(new LinearLayoutManager(mContext));
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
        attachmentAdapter = new AttachmentAdapter(this.mContext, photoBeanList, CHOOSE_PIC_MAX, -1);
        noScrollgridview.setAdapter(attachmentAdapter);
    }

    //切换右侧布局数据
    private void changeRightData(int position) {
        if (boxBeanList.size() == 0) {
            content.setVisibility(View.GONE);
        } else {
            meterBeanList.clear();
            photoBeanList.clear();
            content.setVisibility(View.VISIBLE);
            MeasBoxBean measBoxBean = boxBeanList.get(position);
            meterBeanList.addAll(measBoxBean.getMeters());
            photoBeanList.addAll(measBoxBean.getBoxImages());

            tvAddr.setText(measBoxBean.getInstAddr());
            tv_chang.setText(measBoxBean.getGao());
            tv_kuan.setText(measBoxBean.getKuan());
            tv_caizhi.setText(measBoxBean.getCaizhi());
            if (!TextUtils.isEmpty(measBoxBean.getNote())) {
                tvNote.setText(measBoxBean.getNote());
            }
            tv_qx_detail.setText(measBoxBean.getHasQx() + "\n" + measBoxBean.getQxDetail());
            tv_fenzhix_bianma.setText(measBoxBean.getFenzhixCode());
            doorInfoLayout.setDatas(measBoxBean);

        }
        //刷新右侧表列表
        meterAdapter.setNewData(meterBeanList);
        tvMeterNumber.setText("表数：" + meterBeanList.size() + "个");
        //刷新左侧箱列表
        boxListAdapter.setSelectedPosition(position);
        boxListAdapter.setNewData(boxBeanList);
        //刷新右侧图片列表
        attachmentAdapter.modifyData(photoBeanList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }

    //查询表
    private void queryMeterData(String content) {
        boxBeanList.clear();
        MeterBeanDao biaoDao = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao();
        //当输入框为空时，则不用查询电表的箱集合
        if (!TextUtils.isEmpty(content)) {
            //查询到的所有电表
            List<MeterBean> biaoList = biaoDao.queryBuilder().where(MeterBeanDao.Properties.BarCode.like("%" + content + "%")).build().list();
            //1 遍历查询到的所有电表，根据电表的箱code查询箱数据
            for (int i = 0; i < biaoList.size(); i++) {
                List<MeasBoxBean> boxList = measBoxBeanDao.queryBuilder().where(/*MeasBoxBeanDao.Properties.BarCode.like("%" + content + "%"),*/
                        MeasBoxBeanDao.Properties.BarCode.eq(biaoList.get(i).getMeasBarCode())).build().list();
                boxBeanList.addAll(boxList);
            }
            Log.i("zhangshi",boxBeanList.toString());
            //查询箱列表数据
            boxBeanList.addAll(measBoxBeanDao.queryBuilder().where(MeasBoxBeanDao.Properties.BarCode.like("%" + content + "%")).build().list());
        } else {
            //查询箱列表数据
            boxBeanList = measBoxBeanDao.loadAll();
        }
        //2 去重
        Set<MeasBoxBean> userSet = new HashSet<>(boxBeanList);
        boxBeanList = new ArrayList<>(userSet);

        //刷新页面数据
        changeRightData(0);
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
//            if (attachmentUpdate.getData() != null) { //修改分支箱后，只需要同步分支箱编码
//                MeasBoxBean data = (MeasBoxBean) attachmentUpdate.getData();
//                tv_fenzhix_bianma.setText(data.getFenzhixCode());
//            } else {
                queryMeterData(etSearch.getText().toString().trim());
//            }
        }
    }
}
