package com.beiming.uhf_test.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.MainActivity;
import com.beiming.uhf_test.activity.fenzhix.gj.GjFzxActivity;
import com.beiming.uhf_test.activity.login.LoginActivity;
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
import com.beiming.uhf_test.utils.GlideEngine;
import com.beiming.uhf_test.utils.TimeUtils;
import com.beiming.uhf_test.view.DoorInfoShowLayout;
import com.beiming.uhf_test.widget.ScrollGridView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.TitleBarStyle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @BindView(R.id.tv_shen)
    TextView tv_shen;
    @BindView(R.id.tv_caizhi)
    TextView tv_caizhi;
    @BindView(R.id.tv_row_col)
    TextView tv_row_col;
    @BindView(R.id.tv_qx_detail) //????????????
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
    private BoxListAdapter boxListAdapter;
    private MeterListAdapter meterAdapter;
    private MeasBoxBeanDao measBoxBeanDao;
    private List<PhotoBean> photoBeanList;//????????????
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
        tv_fenzhix_tianjia.setOnClickListener(v -> { //???????????????
//            MeasBoxBean boxBean = boxListAdapter.getData().get(boxListAdapter.getSelectedPosition());
//            FenzhixActivity.Companion.startActivity(mContext, boxBean);
            GjFzxActivity.Companion.startActivity(mContext);
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

                    ArrayList<LocalMedia> phtos = new ArrayList<>();
                    for (PhotoBean bean : photoBeanList) {
                        LocalMedia media = LocalMedia.generateLocalMedia(mContext, bean.getImageSrc());
                        phtos.add(media);
                    }
                    PictureSelectorStyle selectorStyle = new PictureSelectorStyle();
                    // ??????TitleBar ??????
                    TitleBarStyle numberTitleBarStyle = new TitleBarStyle();
                    numberTitleBarStyle.setHideCancelButton(true);
                    numberTitleBarStyle.setAlbumTitleRelativeLeft(true);
                    numberTitleBarStyle.setTitleAlbumBackgroundResource(R.drawable.ps_album_bg);
                    numberTitleBarStyle.setTitleDrawableRightResource(R.drawable.ps_ic_grey_arrow);
                    numberTitleBarStyle.setPreviewTitleLeftBackResource(R.drawable.ps_ic_normal_back);
                    numberTitleBarStyle.setPreviewDeleteBackgroundResource(R.drawable.ps_album_bg);
                    selectorStyle.setTitleBarStyle(numberTitleBarStyle);


                    PictureSelector.create(mContext)
                            .openPreview()
                            .setImageEngine(GlideEngine.createGlideEngine())
//                            .setInjectLayoutResourceListener(new OnInjectLayoutResourceListener() {
//                                @Override
//                                public int getLayoutResourceId(Context context, int resourceSource) {
//                                    return 0;
//                                }
//                            })
                            .setSelectorUIStyle(selectorStyle)
                            .setExternalPreviewEventListener(new OnExternalPreviewEventListener() {
                                @Override
                                public void onPreviewDelete(int position) {

                                }

                                @Override
                                public boolean onLongPressDownload(LocalMedia media) {
                                    return false;
                                }
                            }).startActivityPreview(i, true, phtos);

//                    Intent intent = new Intent(DataRecordFragment.this.mContext, PreviewPhotoActivity.class);
//                    intent.putExtra("ID", i);
//                    intent.putExtra(ConstantUtil.CREAT_OR_DETAILS, creatOrDetails);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(ConstantUtil.PHOTO_BEAN_LIST, (Serializable) photoBeanList);
//                    intent.putExtras(bundle);
//                    DataRecordFragment.this.mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    private void getData() {
        measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
//        boxBeanList = measBoxBeanDao.loadAll();
        boxBeanList = measBoxBeanDao.queryBuilder().where(MeasBoxBeanDao.Properties.Ts.ge(TimeUtils.toTs(TimeUtils.getY_M_D_Time()))).build().list();
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
                    //????????????????????????
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
                showToast("???????????????" + position);
            }
        });

        //????????????
        //?????????????????????
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        attachmentAdapter = new AttachmentAdapter(this.mContext, photoBeanList, CHOOSE_PIC_MAX, -1);
        noScrollgridview.setAdapter(attachmentAdapter);
    }

    //????????????????????????
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
            tv_shen.setText(measBoxBean.getShen());
            tv_caizhi.setText(measBoxBean.getCaizhi());
            tv_row_col.setText(measBoxBean.getBoxRows() + " ?????? " + measBoxBean.getBoxCols() + " ???" );
            if (!TextUtils.isEmpty(measBoxBean.getNote())) {
                tvNote.setText(measBoxBean.getNote());
            }
            tv_qx_detail.setText(measBoxBean.getHasQx() + "\n" + measBoxBean.getQxDetail());
            tv_fenzhix_bianma.setText(measBoxBean.getFenzhixCode());
            doorInfoLayout.setDatas(measBoxBean);

        }
        //?????????????????????
        meterAdapter.setNewData(meterBeanList);
        tvMeterNumber.setText("?????????" + meterBeanList.size() + "???");
        //?????????????????????
        boxListAdapter.setSelectedPosition(position);
        boxListAdapter.setNewData(boxBeanList);
        //????????????????????????
        attachmentAdapter.modifyData(photoBeanList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }

    //?????????
    private void queryMeterData(String content) {
        boxBeanList.clear();
        MeterBeanDao biaoDao = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao();
        //?????????????????????????????????????????????????????????
        if (!TextUtils.isEmpty(content)) {
            //????????????????????????
            List<MeterBean> biaoList = biaoDao.queryBuilder().where(MeterBeanDao.Properties.BarCode.like("%" + content + "%"),
                    MeasBoxBeanDao.Properties.Ts.ge(TimeUtils.toTs(TimeUtils.getY_M_D_Time()))).build().list();
            //1 ???????????????????????????????????????????????????code???????????????
            for (int i = 0; i < biaoList.size(); i++) {
                List<MeasBoxBean> boxList = measBoxBeanDao.queryBuilder().where(/*MeasBoxBeanDao.Properties.BarCode.like("%" + content + "%"),*/
                        MeasBoxBeanDao.Properties.BarCode.eq(biaoList.get(i).getMeasBarCode())).build().list();
                boxBeanList.addAll(boxList);
            }
            Log.i("zhangshi", boxBeanList.toString());
            //?????????????????????
            boxBeanList.addAll(measBoxBeanDao.queryBuilder().where(MeasBoxBeanDao.Properties.BarCode.like("%" + content + "%")).build().list());
        } else {
            //?????????????????????
//            boxBeanList = measBoxBeanDao.loadAll();
            boxBeanList = measBoxBeanDao.queryBuilder().where(MeasBoxBeanDao.Properties.Ts.ge(TimeUtils.toTs(TimeUtils.getY_M_D_Time()))).build().list();
        }
        //2 ??????
        Set<MeasBoxBean> userSet = new HashSet<>(boxBeanList);
        boxBeanList = new ArrayList<>(userSet);

        //??????????????????
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

    //???eventBus????????????bean
    @Subscribe
    public void onRefreshList(AttachmentUpdate attachmentUpdate) {
        if (ConstantUtil.CLEAR_READ_TAG_DATA.equals(attachmentUpdate.getTag())) {
            //todo ?????????????????????????????????????????????
            boxListAdapter.setSelectedPosition(0);
//            if (attachmentUpdate.getData() != null) { //???????????????????????????????????????????????????
//                MeasBoxBean data = (MeasBoxBean) attachmentUpdate.getData();
//                tv_fenzhix_bianma.setText(data.getFenzhixCode());
//            } else {
            queryMeterData(etSearch.getText().toString().trim());
//            }
        }
    }
}
