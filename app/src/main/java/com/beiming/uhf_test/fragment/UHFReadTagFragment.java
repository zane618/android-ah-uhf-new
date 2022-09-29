package com.beiming.uhf_test.fragment;


import android.app.ScansManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.RecordDataActivity;
import com.beiming.uhf_test.activity.MainActivity;
import com.beiming.uhf_test.adapter.BarCodeAdpater;
import com.beiming.uhf_test.bean.BarCodeBean;
import com.beiming.uhf_test.bean.MeasBoxBean;
import com.beiming.uhf_test.bean.MeterBean;
import com.beiming.uhf_test.bean.pic.AttachmentUpdate;
import com.beiming.uhf_test.db.GreenDaoManager;
import com.beiming.uhf_test.dialog.ContrastBarCodeDialog;
import com.beiming.uhf_test.dialog.HintDialog;
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao;
import com.beiming.uhf_test.greendao.gen.MeterBeanDao;
import com.beiming.uhf_test.listener.OnHintDialogClicklistener;
import com.beiming.uhf_test.tools.rfid.IRfidListener;
import com.beiming.uhf_test.tools.rfid.RfidHelper;
import com.beiming.uhf_test.tools.StringUtils;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.DialogUtils;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.beiming.uhf_test.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UHFReadTagFragment extends KeyDwonFragment implements View.OnClickListener {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    Unbinder unbinder;
    private List<BarCodeBean> barCodeBeanList;
    private List<MeasBoxBean> measBoxBeanList;
    private MeasBoxBean boxBean;//点击保存时维护的计量箱bean
    private List<MeterBean> meterBeanList;
    BarCodeAdpater adapter;
    Button BtClear;
    TextView tv_count;
    Button btnSave;
    /**
     * 开始识别
     **/
    Button btnShibie;

    private MainActivity mContext;
    private ContrastBarCodeDialog contrastBarCodeDialog;
    private int MY_PERMISSIONS_REQUEST_FOR_EXCLE = 0x11;

    private String Tag = "UHFReadTagFragment";

    /*public final static int BROADCAST_INPUT_MODE = 0;//广播模式
    public final static int INPUTTEXT_INPUT_MODE = 1;//输入框模式
    public final static int NON_EXTRA = 0;//无
    public final static int ENTER_EXTRA = 1;//添加回车
    private ScansManager mScansManager = null;
    private int inputmode = INPUTTEXT_INPUT_MODE;
    private int addExtra = NON_EXTRA;*/
    private MyReceiver broadcastReceiver = null;
    private IntentFilter filter = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save://保存
//                checkImportExcel();
                //1.判断当前扫描的箱集合大小，为0或大于1时做出相应提示
                if (!checkBoxBarCodeNumber()) {
                    return;
                }
                //2.判断当前扫描的箱和表在本地保存中是否存在，存在则提示用户
                if (!checkBarCodeIsExsit()) {
                    return;
                }
                //3.满足1、2条件后跳转拍照记录页面，点击保存
                Intent intent = new Intent(mContext, RecordDataActivity.class);
                setData();
                intent.putExtra("box", boxBean);
                startActivity(intent);
                break;
            case R.id.BtClear://清空数据
                clearData();
                break;
            case R.id.BtInventory://开始识别
                readTag();
                break;
        }
    }

    //跳转拍照页面时，将箱表关系记录并传递
    private void setData() {
        boxBean = measBoxBeanList.get(0);
        boxBean.setMeters(meterBeanList);
    }

    //判断条形码是否存在
    private boolean checkBarCodeIsExsit() {
        if (meterBeanList.size() == 0) {
            showToast("电表条码不能为空");
            return false;
        }
        //电能表编号
        if (isExsitBarCodeSaved()) {
            showToast("请先处理已存在的条形码编号");
            return false;
        }
        return true;
    }

    //判断电表是否有已保存的存在
    private boolean isExsitBarCodeSaved() {
        boolean isSaved = false;
        for (int i = 0; i < barCodeBeanList.size(); i++) {
            if (barCodeBeanList.get(i).isExsit()) {
                isSaved = true;
                break;
            }
        }
        return isSaved;
    }

    //验证箱数量及表数量
    private boolean checkBoxBarCodeNumber() {
        if (measBoxBeanList.size() == 0) {
            showToast("计量箱条码不能为空");
            return false;
        }
        if (measBoxBeanList.size() > 1) {
            showToast("计量箱条码必须是唯一的");
            return false;
        }
        if (measBoxBeanList.get(0).getIsExsit()) {
            showToast("计量箱条码本地已存在，无法进行下一步操作");
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("MY", "UHFReadTagFragment.onCreateView");
        Log.i("CMCC", Tag + ":" + System.currentTimeMillis());
        View view = inflater
                .inflate(R.layout.uhf_readtag_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("MY", "UHFReadTagFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        barCodeBeanList = new ArrayList<>();
        measBoxBeanList = new ArrayList<>();
        meterBeanList = new ArrayList<>();
        BtClear = getView().findViewById(R.id.BtClear);
        btnSave = getView().findViewById(R.id.btn_save);
        tv_count = getView().findViewById(R.id.tv_count);

        btnShibie = getView().findViewById(R.id.BtInventory);

        recycleview.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false));
        adapter = new BarCodeAdpater(barCodeBeanList, measBoxBeanList);
        adapter.setContext(mContext);
        recycleview.setAdapter(adapter);

        initAdapterListener();

        BtClear.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnShibie.setOnClickListener(this);
    }

    private int currentPosition = 0;

    private void initAdapterListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentPosition = position;
                //条形码类型 0:计量箱 ，1：电能表 -1:不能识别的条形码
                switch (barCodeBeanList.get(position).getBarCodeType()) {
                    case "0": //箱
                        showBoxHint(true, "是否删除该箱条形码？");
                        break;
                    case "1"://表
                        if (view instanceof TextView) {
                            if (((TextView) view).getText().equals("删除")) {
                                showBoxHint(false, "是否删除该电能表条形码？");
                                return;
                            }
                        }
                        if (contrastBarCodeDialog == null) {
                            showContrastBarCodeDialog();
                        } else {
                            contrastBarCodeDialog.updateDialogData(getExsitBarCode(), barCodeBeanList.get(position).getBarCode());
                            contrastBarCodeDialog.show();
                        }
                        break;
                    default:
                        showToast("异常条形码");
                        break;
                }
            }
        });
    }

    //获取已存在电表条形码集合
    private List<MeterBean> getExsitBarCode() {
        List<MeterBean> exsitList = new ArrayList<>();
        for (int i = 0; i < meterBeanList.size(); i++) {
            if (meterBeanList.get(i).getIsExsit()) {
                exsitList.add(meterBeanList.get(i));
            }
        }
        return exsitList;
    }

    private void showContrastBarCodeDialog() {
        contrastBarCodeDialog = DialogUtils.showContrastBarCodeDialog(mContext, getExsitBarCode(), barCodeBeanList.get(currentPosition).getBarCode());
        contrastBarCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //去除电表bean
                meterBeanList.removeAll(contrastBarCodeDialog.getDeleteMeterList());
                //删除所有条码集合中的对应的数据
                List<String> deleteBarCodeList = contrastBarCodeDialog.getDeleteBarCodeList();
                Iterator<BarCodeBean> iter = barCodeBeanList.iterator();
                while (iter.hasNext()) {
                    BarCodeBean s = iter.next();
                    if (deleteBarCodeList.contains(s.getBarCode())) {
                        iter.remove();
                    }
                }
                adapter.setNewData(barCodeBeanList);
                tv_count.setText(measBoxBeanList.size() + "个箱  " + meterBeanList.size() + "个表");
            }
        });
    }

    private void showBoxHint(boolean isBox, String content) {
        DialogUtils.showHintDialog(mContext, content, "否", "是", new OnHintDialogClicklistener() {
            @Override
            public void onConfirm() {
//                barCodeBeanList.remove(currentPosition);
                if (isBox) {
                    measBoxBeanList.remove(currentPosition);
                } else {
                    String delBarCode = barCodeBeanList.get(currentPosition).getBarCode();
                    for (int i = meterBeanList.size() - 1; i >= 0; i--) {
                        if (meterBeanList.get(i).getBarCode().equals(delBarCode)) {
                            meterBeanList.remove(i);
                            break;
                        }
                    }
                }
                adapter.remove(currentPosition);
//                adapter.setNewData(barCodeBeanList);
                tv_count.setText(measBoxBeanList.size() + "个箱  " + meterBeanList.size() + "个表");
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 添加EPC到列表中（添加标签至列表中）
     *
     * @param epc
     */
    private void addEPCToList(String epc) {
        if (!TextUtils.isEmpty(epc)) {
            boolean exist = checkIsExist(epc);
            //不存在则新增
            if (!exist) {
                addTag(epc);
                adapter.setNewData(barCodeBeanList);
            }
        }
    }

    //新增标签对象
    private void addTag(String epc) {
        String barCode = "";
        String assetNo = "";
        String barCodeType = "-1";
        long ts = System.currentTimeMillis();
        String scanTime = TimeUtils.getTime(ts);


        //新表老表条码标号都是22位，rfid识别多了2位0，是24位，怎么区分资产编号和类型呢
        //去除扫到表的后两位
        if (epc.length() == 24) {
            barCode = epc.substring(0, epc.length() - 2);
        } else if (epc.length() == 22) {
            barCode = epc;
        } else {
            return;
        }
        assetNo = barCode.substring(barCode.length() - 15, barCode.length() - 1); //新表，第7位开始，共14位

        // TODO: 2022/9/21 老表的资产类型 
        //正确的是第6、7位 代表资产类型
        if (barCode.startsWith("01", 5))//表
            barCodeType = "1";
        else if (barCode.startsWith("05", 5))//箱
            barCodeType = "0";
        else {
//            barCodeType = "-1"; //老表资产编号是10位，编码是22位，从-1改成1 临时
            barCodeType = "1"; //老表资产编号是10位，编码是22位，从-1改成1 临时
            assetNo = barCode.substring(barCode.length() - 11, barCode.length() - 1); //新表，第7位开始，共14位
        }

        BarCodeBean barCodeBean = new BarCodeBean(epc, barCode, barCodeType, 1, "无", scanTime);
        switch (barCodeType) {
            case "-1":

                break;
            case "0":
                MeasBoxBean measBoxBean = new MeasBoxBean();
                measBoxBean.setBarCode(barCode);
                measBoxBean.setMeasAssetNo(assetNo);
                measBoxBean.setScanTime(scanTime);
                measBoxBean.setTs(ts);
                //当前是否存储过这个箱
                List<MeasBoxBean> boxBarCodelist = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao().queryBuilder().where(
                        MeasBoxBeanDao.Properties.BarCode.eq(barCode), MeasBoxBeanDao.Properties.Ts.ge(TimeUtils.toTs(TimeUtils.getY_M_D_Time()))).build().list();
                if (boxBarCodelist.size() > 0) {
                    measBoxBean.setIsExsit(true);
                    barCodeBean.setExsit(true);
                }
                measBoxBeanList.add(0, measBoxBean);
                barCodeBeanList.add(0, barCodeBean);
                break;
            case "1":
                //此时的表箱条形码还未写入电表中，当用户确认时再次写入
                MeterBean meterBean = new MeterBean();
                meterBean.setBarCode(barCode);//电表条形码
                meterBean.setMeterAssetNo(assetNo);
                meterBean.setScanTime(scanTime);
                meterBean.setTs(ts);
                //当前是否存储过这个电能表
                List<MeterBean> meterBarCodelist = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao().queryBuilder().where(
                                MeterBeanDao.Properties.BarCode.eq(barCode), MeterBeanDao.Properties.Ts.ge(TimeUtils.toTs(TimeUtils.getY_M_D_Time())))
                        .build().list();
                if (meterBarCodelist.size() > 0) {
                    meterBean.setIsExsit(true);
                    barCodeBean.setExsit(true);
                }
                meterBeanList.add(meterBean);
                barCodeBeanList.add(barCodeBean);
                break;
        }
        tv_count.setText(measBoxBeanList.size() + "个箱  " + meterBeanList.size() + "个表");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //导出excel表格
    private void ImportExcel() {
    }

    //清楚数据
    private void clearData() {
        tv_count.setText("0");

        barCodeBeanList.clear();
        measBoxBeanList.clear();
        meterBeanList.clear();

        adapter.setNewData(barCodeBeanList);
    }

    private void readTag() {
        if (btnShibie.getText().equals(mContext.getString(R.string.btInventory))) {// 开始识别
            btnShibie.setText(mContext.getString(R.string.title_stop_Inventory));
            RfidHelper.getInstance().startScan(new IRfidListener() {
                @Override
                public void onRfidResult(String s) {
                    LogPrintUtil.zhangshi("thread: " + Thread.currentThread().getName()
                            + "  s: " + s);
                    addEPCToList(s);
                }

                @Override
                public void onStop() {
                    btnShibie.setText(mContext.getString(R.string.btInventory));
                    setViewEnabled(true);
                }
            });
        } else {// 停止识别
            RfidHelper.getInstance().stopScan();
        }
    }


    private void setViewEnabled(boolean enabled) {
        BtClear.setEnabled(enabled);
    }

    /**
     * 判断EPC是否在列表中
     *
     * @param strEPC 索引
     * @return
     */
    public boolean checkIsExist(String strEPC) {
        if (StringUtils.isEmpty(strEPC)) {
            return false;
        }
        BarCodeBean barCodeBean;
        for (int i = 0; i < barCodeBeanList.size(); i++) {
            barCodeBean = barCodeBeanList.get(i);
            if (barCodeBean.getTagUii().startsWith(strEPC) ||
                    strEPC.startsWith(barCodeBean.getTagUii())) {
                barCodeBean.setTagCount(barCodeBean.getTagCount() + 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public void myOnKeyDwon() {
        readTag();
    }

    @Override
    protected void initData() {

    }

    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_FOR_EXCLE) {//权限请求成功导，出所有word
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImportExcel();
            } else {
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
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
            clearData();
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            LogPrintUtil.zhangshi("fragment:onReceive");
            if (intent.getAction().equals("com.safeuem.doublebird.appReadyToInstall")) {
//                et.append("com.safeuem.doublebird.appReadyToInstall");
            } else {
                addEPCToList(intent.getStringExtra("barcode"));
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogPrintUtil.zhangshi("fragment:" + hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogPrintUtil.zhangshi("fragment:onResume");
        if (isVisibleToUser) {
            initBroadcast();
        }
    }

    @Override
    public void onPause() {
        LogPrintUtil.zhangshi("fragment:onPause");
        super.onPause();
        RfidHelper.getInstance().stopScan();
        destroyBroadcase();
    }

    private boolean isVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser) {
            LogPrintUtil.zhangshi("fragment:当前UHFReadTagFragment不可见");

            RfidHelper.getInstance().stopScan();
            destroyBroadcase();
        } else {
            LogPrintUtil.zhangshi("fragment:当前UHFReadTagFragment   可见");
            initBroadcast();
        }
    }

    private void destroyBroadcase() {
        if (broadcastReceiver != null) {
            try {
                mContext.unregisterReceiver(broadcastReceiver);
            } catch (Exception e) {
            }
        }
    }

    private void initBroadcast() {
        if (broadcastReceiver == null) {
            filter = new IntentFilter();
            filter.addAction("android.scanservice.action.UPLOAD_BARCODE_DATA");
            filter.addAction("com.safeuem.doublebird.appReadyToInstall");
            broadcastReceiver = new MyReceiver();
        }
        try {
            mContext.registerReceiver(broadcastReceiver, filter);
        } catch (Exception e) {
        }
    }
}
