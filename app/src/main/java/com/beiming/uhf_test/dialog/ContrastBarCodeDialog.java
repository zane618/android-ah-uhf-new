package com.beiming.uhf_test.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.adapter.ConflictBarCodeAdpater;
import com.beiming.uhf_test.base.BaseDialog;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeterBeanDao;
import com.beiming.uhf_test.listener.OnHintDialogClicklistener;
import com.beiming.uhf_test.utils.DialogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 重复条码对比的dialog
 */
public class ContrastBarCodeDialog extends BaseDialog implements View.OnClickListener {

    RecyclerView rvCurrent;
    TextView tvBoxBarcode;
    RecyclerView rvSaved;
    Button btSave;
    private List<PhotoBean> photoBeanList = new ArrayList<>();//图片集合
    private static final int CHOOSE_PIC_MAX = 10;
    private int MY_PERMISSIONS_REQUEST = 10011;//图片请求码
    private static final int PICK_PHOTO = 101;
    private Context context;
    private List<MeterBean> exsitBarCode = new ArrayList<>();
    private List<MeterBean> savedBarCode = new ArrayList<>();
    private List<MeterBean> deleteMeterList = new ArrayList<>();//记录被删除的电表bean
    private List<String> deleteBarCodeList = new ArrayList<>();//记录被删除的条码集合与deleteMeterList的大小保持一致
    private MeasBoxBean currentBox;//删除了表所属箱的集合
    private String selectBarCode;
    private ConflictBarCodeAdpater currentAdapter;
    private ConflictBarCodeAdpater savedAdapter;
    private int type;//hintDialog中的处理逻辑：0:当前冲突，1：本地保存，2确认按钮

    public ContrastBarCodeDialog(Context context, List<MeterBean> exsitBarCode, String selectBarCode) {
        super(context);
        this.context = context;
        this.selectBarCode = selectBarCode;
        this.exsitBarCode = exsitBarCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contrast_bar_code);
        initView();
        initAdapter();
        initOnClick();
        initData();
    }

    private void initView() {
        rvCurrent = findViewById(R.id.rv_current);
        tvBoxBarcode = findViewById(R.id.tv_box_barcode);
        rvSaved = findViewById(R.id.rv_saved);
        btSave = findViewById(R.id.bt_save);
    }

    private void initData() {
        //获取本地存储的数据并更新ui
        updateSavedData();
    }

    private int currentPosition = 0;

    private void initOnClick() {
        btSave.setOnClickListener(this);

        currentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                type = 0;
                currentPosition = position;
                if (hintDialog == null)
                    showHint("是否删除该表条形码？");
                else
                    hintDialog.show();
            }
        });
        savedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                type = 1;
                currentPosition = position;
                if (hintDialog == null)
                    showHint("是否删除该表条形码？");
                else
                    hintDialog.show();
            }
        });
    }

    private HintDialog hintDialog;

    private void showHint(String content) {
        hintDialog = DialogUtils.showHintDialog(context, content, "否", "是", new OnHintDialogClicklistener() {
            @Override
            public void onConfirm() {
                String selectBarCode = "";
                switch (type) {
                    case 0:
                        deleteMeterList.add(exsitBarCode.get(currentPosition));
                        deleteBarCodeList.add(exsitBarCode.get(currentPosition).getBarCode());
                        exsitBarCode.remove(currentPosition);
                        if (exsitBarCode.size() == 0) {
                            selectBarCode = "";
                        } else {
                            selectBarCode = exsitBarCode.get(0).getBarCode();
                        }
                        break;
                    case 1:
                        //1.删除上面的集合
                        //todo 记录本地要被删除的数据，该用来记录的集合在dialogdismiss的时候需要清除
                        MeterBean meterBean = savedBarCode.get(currentPosition);
                        exsitBarCode.remove(currentPosition);
                        int position = -1;
                        for (int i = 0; i < exsitBarCode.size(); i++) {
                            if (exsitBarCode.get(i).getBarCode().equals(meterBean.getBarCode())) {
                                position = i;
                            }
                        }
                        if (position != -1) {
                            deleteMeterList.add(exsitBarCode.get(position));
                            deleteBarCodeList.add(exsitBarCode.get(position).getBarCode());
                            exsitBarCode.remove(position);
                        }
                        if (exsitBarCode.size() == 0) {
                            selectBarCode = "";
                        } else {
                            selectBarCode = exsitBarCode.get(0).getBarCode();
                        }
                        //删除本地数据
                        deleteData(meterBean);
                        break;
                }
                updateData(exsitBarCode, selectBarCode);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void deleteData(MeterBean meterBean) {
        //删除本地电表
        MeterBeanDao meterBeanDao = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao();
        List<MeterBean> meterBeans = meterBeanDao.queryBuilder().where(
                MeterBeanDao.Properties.BarCode.eq(meterBean.getBarCode())).build().list();
        if (meterBeans.size() > 0) {
            meterBeanDao.delete(meterBeans.get(0));
        }
        //删除箱中表
        MeasBoxBeanDao measBoxBeanDao = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao();
        savedBarCode.remove(meterBean);
        currentBox.setMeters(savedBarCode);
        measBoxBeanDao.update(currentBox);
        savedAdapter.setNewData(savedBarCode);
    }

    private void initAdapter() {
        rvCurrent.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false));
        currentAdapter = new ConflictBarCodeAdpater(context, R.layout.item_conflict_barcode_dialog, exsitBarCode, selectBarCode);
        rvCurrent.setAdapter(currentAdapter);

        rvSaved.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false));
        savedAdapter = new ConflictBarCodeAdpater(context, R.layout.item_conflict_barcode_dialog, savedBarCode, selectBarCode);
        rvSaved.setAdapter(savedAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
               /* if (deleteBeanList.size() > 0) {
                    type = 2;
                    if (hintDialog == null)
                        showHint("删除后将无法恢复，确认删除吗？");
                    else
                        hintDialog.show();
                } else {
                    dismiss();
                }*/
                break;
        }
    }

    public void updateDialogData(List<MeterBean> exsitBarCode, String selectBarCode){
        deleteMeterList.clear();
        deleteBarCodeList.clear();
        updateData(exsitBarCode,selectBarCode);
    }

    private void updateData(List<MeterBean> exsitBarCode, String selectBarCode) {
        this.exsitBarCode = exsitBarCode;
        if (!this.selectBarCode.equals(selectBarCode)) {
            this.selectBarCode = selectBarCode;
            //若与原条码不一致，刷新上下两个adapter布局
            this.exsitBarCode = exsitBarCode;
            currentAdapter.setSelectBarCode(selectBarCode);
            currentAdapter.setNewData(exsitBarCode);
            //获取本地存储的数据并更新ui
            updateSavedData();
        } else {
            //若与原条码一致，刷新上面一个adapter布局
            currentAdapter.setSelectBarCode(selectBarCode);
            currentAdapter.setNewData(exsitBarCode);
        }
    }

    //刷新本地存储的数据
    private void updateSavedData() {
        //根据选中的电表查出所属箱，并展示
        List<MeterBean> meterBeans = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao().queryBuilder().where(
                MeterBeanDao.Properties.BarCode.eq(selectBarCode)).build().list();
        //查箱
        if (meterBeans.size() > 0) {
            List<MeasBoxBean> boxBeans = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao().queryBuilder().where(
                    MeasBoxBeanDao.Properties.BarCode.eq(meterBeans.get(0).getMeasBarCode())).build().list();
            if (boxBeans.size() > 0) {
                currentBox = boxBeans.get(0);
                savedBarCode = currentBox.getMeters();
                tvBoxBarcode.setText("所存在表箱资产：" + currentBox.getBarCode());
            }
        }
        //刷新
        savedAdapter.setSelectBarCode(selectBarCode);
        savedAdapter.setNewData(savedBarCode);
    }

    public List<MeterBean> getDeleteMeterList() {
        return deleteMeterList;
    }
    public List<String> getDeleteBarCodeList() {
        return deleteBarCodeList;
    }
}
