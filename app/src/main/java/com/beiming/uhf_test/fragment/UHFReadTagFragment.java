package com.beiming.uhf_test.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.RecordDataActivity;
import com.beiming.uhf_test.activity.UHFMainActivity;
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
import com.beiming.uhf_test.tools.StringUtils;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.utils.ConstantUtil;
import com.beiming.uhf_test.utils.DialogUtils;
import com.beiming.uhf_test.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.uhf.api.cls.Reader;

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
    private int inventoryFlag = 1;
    private List<BarCodeBean> barCodeBeanList;
    private List<MeasBoxBean> measBoxBeanList;
    private MeasBoxBean boxBean;//点击保存时维护的计量箱bean
    private List<MeterBean> meterBeanList;
    BarCodeAdpater adapter;
    Button BtClear;
    TextView tv_count;
    RadioGroup RgInventory;
    RadioButton RbInventorySingle;
    RadioButton RbInventoryLoop;
    Button Btimport;
    Button BtInventory;

    private LinearLayout llContinuous;
    private UHFMainActivity mContext;
    PopupWindow popFilter;
    private ContrastBarCodeDialog contrastBarCodeDialog;
    private HintDialog hintDialog;
    private int MY_PERMISSIONS_REQUEST_FOR_EXCLE = 0x11;

    boolean isReading = false;//是否正在扫描
    private String Tag = "UHFReadTagFragment";
    boolean isSetStop = false;//读取时长后停止

    public String TAG = "UHFReadTagFragment";
    Handler handler = new Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    addEPCToList(msg.obj.toString());
                    break;
                case 3:
                    break;
                default:
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtImport://确定按钮
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

    private void checkImportExcel() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_FOR_EXCLE);
        } else {
            ImportExcel();
        }
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
        mContext = (UHFMainActivity) getActivity();
        barCodeBeanList = new ArrayList<>();
        measBoxBeanList = new ArrayList<>();
        meterBeanList = new ArrayList<>();
        BtClear = (Button) getView().findViewById(R.id.BtClear);
        Btimport = (Button) getView().findViewById(R.id.BtImport);
        tv_count = (TextView) getView().findViewById(R.id.tv_count);
        RgInventory = (RadioGroup) getView().findViewById(R.id.RgInventory);
        String tr = "";
        RbInventorySingle = (RadioButton) getView()
                .findViewById(R.id.RbInventorySingle);
        RbInventoryLoop = (RadioButton) getView()
                .findViewById(R.id.RbInventoryLoop);

        BtInventory = (Button) getView().findViewById(R.id.BtInventory);

        llContinuous = (LinearLayout) getView().findViewById(R.id.llContinuous);

        /*adapter = new SimpleAdapter(mContext, tagList, R.layout.listtag_items,
                new String[]{"tagUii", "tagLen", "tagCount", "tagRssi"},
                new int[]{R.id.TvTagUii, R.id.TvTagLen, R.id.TvTagCount,
                        R.id.TvTagRssi});*/
        recycleview.setLayoutManager(new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false));
        adapter = new BarCodeAdpater(R.layout.listtag_items, barCodeBeanList, measBoxBeanList.size());
        adapter.setContext(mContext);
        recycleview.setAdapter(adapter);

        initAdapterListener();
//        clearData();

        BtClear.setOnClickListener(this);
        Btimport.setOnClickListener(this);
        RgInventory.setOnCheckedChangeListener(new RgInventoryCheckedListener());
        BtInventory.setOnClickListener(this);
    }

    private int currentPosition = 0;

    private void initAdapterListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentPosition = position;
                //条形码类型 0:计量箱 ，1：电能表 -1:不能识别的条形码
                switch (barCodeBeanList.get(position).getBarCodeType()) {
                    case "0":
                        if (hintDialog == null)
                            showBoxHint("是否删除该箱条形码？");
                        else
                            hintDialog.show();
                        break;
                    case "1":
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

    private void showBoxHint(String content) {
        hintDialog = DialogUtils.showHintDialog(mContext, content, "否", "是", new OnHintDialogClicklistener() {
            @Override
            public void onConfirm() {
                barCodeBeanList.remove(currentPosition);
                measBoxBeanList.remove(currentPosition);
                adapter.setBoxNumber(measBoxBeanList.size());
                adapter.setNewData(barCodeBeanList);
                tv_count.setText(measBoxBeanList.size() + "个箱  " + meterBeanList.size() + "个表");
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("MY", "UHFReadTagFragment.onPause");
        super.onPause();
        stopScan();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            System.out.println("当前UHFReadTagFragment不可见");
            stopScan();
        }
    }

    /**
     * 添加EPC到列表中（添加标签至列表中）
     *
     * @param epc
     */
    private void addEPCToList(String epc) {
        if (!TextUtils.isEmpty(epc)) {
            int index = checkIsExist(epc);
            // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");
            //不存在则新增
            if (index == -1) {
                addTag(epc);
            }
            adapter.setBoxNumber(measBoxBeanList.size());
            adapter.setNewData(barCodeBeanList);
        }
    }

    //新增标签对象
    private void addTag(String epc) {
        String barCode = "";
        String assetNo = "";
        String barCodeType = "-1";
        String scanTime = TimeUtils.getTime();

        //去除扫到表的后两位
        if (epc.length() == 24) {
            barCode = epc.substring(0, epc.length() - 2);
            assetNo = barCode.substring(barCode.length() - 15, barCode.length() - 1);
            //todo 判断是表还是箱条形码
            if (assetNo.substring(0, 2).equals("18"))//表
                barCodeType = "1";
            else if (assetNo.substring(0, 2).equals("15"))//箱
                barCodeType = "0";
            else
                barCodeType = "-1";


            BarCodeBean barCodeBean = new BarCodeBean(epc, barCode, barCodeType, 1, "无", scanTime);
            switch (barCodeType) {
                case "-1":

                    break;
                case "0":
                    MeasBoxBean measBoxBean = new MeasBoxBean();
                    measBoxBean.setBarCode(barCode);
                    measBoxBean.setMeasAssetNo(assetNo);
                    measBoxBean.setScanTime(scanTime);
                    List<MeasBoxBean> boxBarCodelist = GreenDaoManager.getInstance().getNewSession().getMeasBoxBeanDao().queryBuilder().where(
                            MeasBoxBeanDao.Properties.BarCode.eq(barCode)).build().list();
                    if (boxBarCodelist != null && boxBarCodelist.size() > 0) {
                        measBoxBean.setIsExsit(true);
                        barCodeBean.setExsit(true);
                    }
                    measBoxBeanList.add(0, measBoxBean);
                    barCodeBeanList.add(0, barCodeBean);
                    break;
                case "1":
                    //todo 注意，此时的表箱条形码还未写入电表中，当用户确认时再次写入
                    MeterBean meterBean = new MeterBean();
                    meterBean.setBarCode(barCode);//电表条形码
                    meterBean.setMeterAssetNo(assetNo);
                    meterBean.setScanTime(scanTime);
                    List<MeterBean> meterBarCodelist = GreenDaoManager.getInstance().getNewSession().getMeterBeanDao().queryBuilder().where(
                            MeterBeanDao.Properties.BarCode.eq(barCode))
                            .build().list();
                    if (meterBarCodelist != null && meterBarCodelist.size() > 0) {
                        meterBean.setIsExsit(true);
                        barCodeBean.setExsit(true);
                    }
                    meterBeanList.add(meterBean);
                    barCodeBeanList.add(barCodeBean);
                    break;
            }
            tv_count.setText(measBoxBeanList.size() + "个箱  " + meterBeanList.size() + "个表");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //导出excel表格
    private void ImportExcel() {
        if (BtInventory.getText().equals(
                mContext.getString(R.string.btInventory))) {
            if (measBoxBeanList.size() == 0) {

                UIHelper.ToastMessage(mContext, "无数据导出");
                return;
            }
            boolean re = FileImport.daochu("", measBoxBeanList);
            if (re) {
                UIHelper.ToastMessage(mContext, "导出成功");
                clearData();
            }
        } else {
            UIHelper.ToastMessage(mContext, "请停止扫描后再导出");
        }
    }

    //清楚数据
    private void clearData() {
        tv_count.setText("0");

        barCodeBeanList.clear();
        measBoxBeanList.clear();
        meterBeanList.clear();

        adapter.setBoxNumber(measBoxBeanList.size());
        adapter.setNewData(barCodeBeanList);
    }

    //单步还是循环
    public class RgInventoryCheckedListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            llContinuous.setVisibility(View.GONE);
            if (checkedId == RbInventorySingle.getId()) {
                // 单步识别
                inventoryFlag = 0;
            } else if (checkedId == RbInventoryLoop.getId()) {
                // 单标签循环识别
                inventoryFlag = 1;
                llContinuous.setVisibility(View.VISIBLE);
            }
        }
    }

    private void readTag() {
        if (BtInventory.getText().equals(mContext.getString(R.string.btInventory))) {// 识别标签
            BtInventory.setText(mContext.getString(R.string.title_stop_Inventory));
            if (mContext.isR2000) {
                startScanToR2000();
            } else {
                startScanTo5300();
            }
        } else {// 停止识别
            stopScan();
        }
    }


    private void startScanTo5300() {
        isReading = true;
        setViewEnabled(false);
        new InventoryRawThread().start();
    }


    public void startScanToR2000() {
        if (!isReading) {
            int[] ants = new int[]{1};
            int option = 0;
            if (!mContext.inventoryEpc) {
                option = 32768;
            }
            Reader.READER_ERR reader_err = mContext.uhfReader.AsyncStartReading(ants, 1, 16);
            if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                isReading = true;
                setViewEnabled(false);
                //获取定时的时间
//                String time = et_stopTime.getText().toString().trim();
                //设置按钮为停止扫描
                BtInventory.setText(mContext
                        .getString(R.string.title_stop_Inventory));
                String time = "10";
                //开启扫描线程
                new InventoryThread().start();
                //是否定时停止扫描
                if (isSetStop) {
                    handler.postDelayed(this::stopScan, Integer.parseInt(time) * 1000);
                }
            } else {
                isReading = false;
                showToast("错误码-->" + reader_err);
            }
        } else {
            stopScan();
        }
    }

    /**
     * 停止识别
     */
    public void stopScan() {
        if (isReading) {
            isReading = false;
            Reader.READER_ERR reader_err = mContext.uhfReader.AsyncStopReading();
            if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                BtInventory.setText(mContext.getString(R.string.btInventory));
                setViewEnabled(true);
            } else {
                UIHelper.ToastMessage(mContext,
                        R.string.uhf_msg_inventory_stop_fail);
            }
        }
    }

    private void setViewEnabled(boolean enabled) {
        RbInventorySingle.setEnabled(enabled);
        RbInventoryLoop.setEnabled(enabled);
        BtClear.setEnabled(enabled);
    }

    /**
     * 判断EPC是否在列表中
     *
     * @param strEPC 索引
     * @return
     */
    public int checkIsExist(String strEPC) {
        int existFlag = -1;
        if (StringUtils.isEmpty(strEPC)) {
            return existFlag;
        }
        BarCodeBean barCodeBean;
        for (int i = 0; i < barCodeBeanList.size(); i++) {
            barCodeBean = barCodeBeanList.get(i);
            if (strEPC.equals(barCodeBean.getTagUii())) {
                barCodeBean.setTagCount(barCodeBean.getTagCount() + 1);
                existFlag = i;
                break;
            }
        }
        return existFlag;
    }

    public class InventoryRawThread extends Thread {
        @Override
        public void run() {
            synchronized (this) {
                while (isReading) {
                    int[] tagcnt = new int[1];
                    tagcnt[0] = 0;
                    int ants[] = {1};
                    Reader.READER_ERR reader_err = mContext.uhfReader.TagInventory_Raw(ants, 1, (short) 100, tagcnt);

                    if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                        Log.e(TAG, "5300 一次标签返回个数: " + tagcnt[0]);
                        for (int i = 0; i < tagcnt[0]; i++) {
                            Reader.TAGINFO taginfo = mContext.uhfReader.new TAGINFO();
                            Reader.READER_ERR reader_err1 = mContext.uhfReader.GetNextTag(taginfo);

                            if (reader_err1 == Reader.READER_ERR.MT_OK_ERR) {
                                String epc = Reader.bytes_Hexstr(taginfo.EpcId);
                                sendMsg(1, epc);
                                mContext.playSound(4);
                            } else {
                                Log.e(TAG, "5300 获取标签失败: " + reader_err1);
                            }
                        }

                    } else {
                        handler.sendEmptyMessage(3);
                        isReading = false;
                        showToast("开启失败 " + reader_err);
                        Log.e(TAG, "5300 开启失败: " + reader_err);
                    }
                }
            }
        }
    }


    private void sendMsg(int what, String epc) {
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = epc;
        handler.sendMessage(msg);
    }


    //开启扫码线程
    public class InventoryThread extends Thread {
        @Override
        public void run() {
            int[] tagcnt = new int[1];
            synchronized (this) {
                while (isReading) {
                    Reader.READER_ERR er;
                    er = mContext.uhfReader.AsyncGetTagCount(tagcnt);
                    if (er == Reader.READER_ERR.MT_OK_ERR) {
                        if (tagcnt[0] > 0) {
                            for (int i = 0; i < tagcnt[0]; i++) {
                                Reader.TAGINFO taginfo = mContext.uhfReader.new TAGINFO();
                                er = mContext.uhfReader.AsyncGetNextTag(taginfo);
                                if (er == Reader.READER_ERR.MT_OK_ERR) {
//                                    if (mContext.inventoryEpc) {
//                                        //盘存模式 EPC
                                    String epc = Reader.bytes_Hexstr(taginfo.EpcId);
                                    sendMsg(1, epc);
//                                    } else {
                                    //盘存TID模式
//                                        if (taginfo.EmbededDatalen > 0) {
//                                            char[] out = new char[taginfo.EmbededDatalen * 2];
//                                            me.uhfReader.Hex2Str(taginfo.EmbededData, taginfo.EmbededDatalen, out);
//                                            String tid = String.valueOf(out);
//                                            addEPCToList(tid);
//                                        }
//                                    }
                                    mContext.playSound(4);
//                                    SystemClock.sleep(30);
                                }
                            }
                        }
                    }
                }
            }

        }
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
}
