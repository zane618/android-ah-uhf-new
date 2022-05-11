package com.beiming.uhf_test.fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.RecordDataActivity;
import com.beiming.uhf_test.activity.UHFMainActivity;
import com.beiming.uhf_test.activity.login.LoginActivity;
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
import com.rscja.deviceapi.RFIDWithUHF;

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
    private boolean loopFlag = false;
    private int inventoryFlag = 1;
    Handler handler;
    private List<BarCodeBean> barCodeBeanList;
    private List<MeasBoxBean> measBoxBeanList;
    private MeasBoxBean boxBean;//点击保存时维护的计量箱bean
    private List<MeterBean> meterBeanList;
    private List<BarCodeBean> errorCodeList;
    BarCodeAdpater adapter;
    Button BtClear;
    TextView tv_count;
    RadioGroup RgInventory;
    RadioButton RbInventorySingle;
    RadioButton RbInventoryLoop;
    Button Btimport;
    Button BtInventory;

    private Button btnFilter;//过滤
    private LinearLayout llContinuous;
    private UHFMainActivity mContext;
    PopupWindow popFilter;
    private ContrastBarCodeDialog contrastBarCodeDialog;
    private HintDialog hintDialog;
    private int MY_PERMISSIONS_REQUEST_FOR_EXCLE = 0x11;

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

    private String Tag = LoginActivity.class.getName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("MY", "UHFReadTagFragment.onCreateView");
        Log.i("CMCC", Tag+":"+System.currentTimeMillis());
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
        errorCodeList = new ArrayList<>();
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
        btnFilter = (Button) getView().findViewById(R.id.btnFilter);

        //过滤按钮的监听事件
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popFilter == null) {
                    View viewPop = LayoutInflater.from(mContext).inflate(R.layout.popwindow_filter, null);

                    popFilter = new PopupWindow(viewPop, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

                    popFilter.setTouchable(true);
                    popFilter.setOutsideTouchable(true);
                    popFilter.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    popFilter.setBackgroundDrawable(new BitmapDrawable());

                    final EditText etLen = (EditText) viewPop.findViewById(R.id.etLen);
                    final EditText etPtr = (EditText) viewPop.findViewById(R.id.etPtr);
                    final EditText etData = (EditText) viewPop.findViewById(R.id.etData);
                    final RadioButton rbEPC = (RadioButton) viewPop.findViewById(R.id.rbEPC);
                    final RadioButton rbTID = (RadioButton) viewPop.findViewById(R.id.rbTID);
                    final RadioButton rbUser = (RadioButton) viewPop.findViewById(R.id.rbUser);
                    final Button btSet = (Button) viewPop.findViewById(R.id.btSet);


                    btSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String filterBank = "UII";
                            if (rbEPC.isChecked()) {
                                filterBank = "UII";
                            } else if (rbTID.isChecked()) {
                                filterBank = "TID";
                            } else if (rbUser.isChecked()) {
                                filterBank = "USER";
                            }
                            if (etLen.getText().toString() == null || etLen.getText().toString().isEmpty()) {
                                UIHelper.ToastMessage(mContext, "数据长度不能为空");
                                return;
                            }
                            if (etPtr.getText().toString() == null || etPtr.getText().toString().isEmpty()) {
                                UIHelper.ToastMessage(mContext, "起始地址不能为空");
                                return;
                            }
                            int ptr = StringUtils.toInt(etPtr.getText().toString(), 0);
                            int len = StringUtils.toInt(etLen.getText().toString(), 0);
                            String data = etData.getText().toString().trim();
                            if (len > 0) {
                                String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                                if (data == null || data.isEmpty() || !data.matches(rex)) {
                                    UIHelper.ToastMessage(mContext, "过滤的数据必须是十六进制数据");
//									mContext.playSound(2);
                                    return;
                                }

                                if (mContext.mReader.setFilter(RFIDWithUHF.BankEnum.valueOf(filterBank), ptr, len, data, false)) {
                                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_filter_succ);
                                } else {
                                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_filter_fail);
//									mContext.playSound(2);
                                }
                            } else {
                                //禁用过滤
                                String dataStr = "";
                                if (mContext.mReader.setFilter(RFIDWithUHF.BankEnum.valueOf("UII"), 0, 0, dataStr, false)
                                        && mContext.mReader.setFilter(RFIDWithUHF.BankEnum.valueOf("TID"), 0, 0, dataStr, false)
                                        && mContext.mReader.setFilter(RFIDWithUHF.BankEnum.valueOf("USER"), 0, 0, dataStr, false)) {
                                    UIHelper.ToastMessage(mContext, R.string.msg_disable_succ);
                                } else {
                                    UIHelper.ToastMessage(mContext, R.string.msg_disable_fail);
                                }
                            }


                        }
                    });
                    CheckBox cb_filter = (CheckBox) viewPop.findViewById(R.id.cb_filter);
                    rbEPC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (rbEPC.isChecked()) {
                                etPtr.setText("32");
                            }
                        }
                    });
                    rbTID.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (rbTID.isChecked()) {
                                etPtr.setText("0");
                            }
                        }
                    });
                    rbUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (rbUser.isChecked()) {
                                etPtr.setText("0");
                            }
                        }
                    });

                    cb_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) { //启用过滤

                            } else { //禁用过滤

                            }
                            popFilter.dismiss();
                        }
                    });
                }
                if (popFilter.isShowing()) {
                    popFilter.dismiss();
                    popFilter = null;
                } else {
                    popFilter.showAsDropDown(view);
                }
            }
        });

        Log.i("MY", "UHFReadTagFragment.EtCountOfTags=" + tv_count.getText());
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj + "";
                String[] strs = result.split("@");
                addEPCToList(strs[0], strs[1]);
                mContext.playSound(1);
            }
        };
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
    public void onPause() {
        Log.i("MY", "UHFReadTagFragment.onPause");
        super.onPause();

        // 停止识别
        stopInventory();
    }

    /**
     * 添加EPC到列表中（添加标签至列表中）
     *
     * @param epc
     */
    private void addEPCToList(String epc, String rssi) {
        if (!TextUtils.isEmpty(epc)) {
            int index = checkIsExist(epc);
            // mContext.getAppContext().uhfQueue.offer(epc + "\t 1");
            //不存在则新增
            if (index == -1) {
                addTag(epc, rssi);
            }
            adapter.setBoxNumber(measBoxBeanList.size());
            adapter.setNewData(barCodeBeanList);
        }
    }

    //新增标签对象
    private void addTag(String epc, String rssi) {
        String barCode = "";
        String assetNo = "";
        String barCodeType = "-1";
        String scanTime = TimeUtils.getTime();

        //去除扫到表的后两位
        if (epc.length() == 28) {
            barCode = epc.substring(4, epc.length() - 2);
            assetNo = barCode.substring(barCode.length() - 15, barCode.length() - 1);
            //todo 判断是表还是箱条形码
            if (assetNo.substring(0, 2).equals("18"))//表
                barCodeType = "1";
            else if (assetNo.substring(0, 2).equals("15"))//箱
                barCodeType = "0";
            else
                barCodeType = "-1";


            BarCodeBean barCodeBean = new BarCodeBean(epc, barCode, barCodeType, 1, rssi, scanTime);
            switch (barCodeType) {
                case "-1":
//                    errorCodeList.add(barCodeBean);
//                    barCodeBeanList.add(0, barCodeBean);
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
        errorCodeList.clear();

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
        if (BtInventory.getText().equals(
                mContext.getString(R.string.btInventory)))// 识别标签
        {
            switch (inventoryFlag) {
                case 0:// 单步
                {
                    String strUII = mContext.mReader.inventorySingleTag();
                    if (!TextUtils.isEmpty(strUII)) {
                        String strEPC = mContext.mReader.convertUiiToEPC(strUII);
                        addEPCToList(strEPC, "N/A");
                        tv_count.setText(measBoxBeanList.size() + "个箱  " + meterBeanList.size() + "个表");
                    } else {
                        UIHelper.ToastMessage(mContext, R.string.uhf_msg_inventory_fail);
//					mContext.playSound(2);
                    }
                }
                break;
                case 1:// 单标签循环  .startInventoryTag((byte) 0, (byte) 0))
                {
                    //  mContext.mReader.setEPCTIDMode(true);
                    if (mContext.mReader.startInventoryTag(0, 0)) {
                        BtInventory.setText(mContext
                                .getString(R.string.title_stop_Inventory));
                        loopFlag = true;
                        setViewEnabled(false);
                        new TagThread().start();
                    } else {
                        mContext.mReader.stopInventory();
                        UIHelper.ToastMessage(mContext,
                                R.string.uhf_msg_inventory_open_fail);
//					mContext.playSound(2);
                    }
                }
                break;
                default:
                    break;
            }
        } else {// 停止识别
            stopInventory();
        }
    }

    private void setViewEnabled(boolean enabled) {
        RbInventorySingle.setEnabled(enabled);
        RbInventoryLoop.setEnabled(enabled);
        btnFilter.setEnabled(enabled);
        BtClear.setEnabled(enabled);
    }

    /**
     * 停止识别
     */
    private void stopInventory() {
        if (loopFlag) {
            loopFlag = false;
            setViewEnabled(true);
            if (mContext.mReader.stopInventory()) {
                BtInventory.setText(mContext.getString(R.string.btInventory));
            } else {
                UIHelper.ToastMessage(mContext,
                        R.string.uhf_msg_inventory_stop_fail);
            }
        }
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

    class TagThread extends Thread {
        public void run() {
            String strTid;
            String strResult;
            String[] res = null;
            while (loopFlag) {
                res = mContext.mReader.readTagFromBuffer();
                if (res != null) {
                    strTid = res[0];
                    if (strTid.length() != 0 && !strTid.equals("0000000" +
                            "000000000") && !strTid.equals("000000000000000000000000")) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Log.i("data", "EPC:" + res[1] + "|" + strResult);
                    Message msg = handler.obtainMessage();
                    msg.obj = strResult + "EPC:" + mContext.mReader.convertUiiToEPC(res[1]) + "@" + res[2];

                    handler.sendMessage(msg);
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
