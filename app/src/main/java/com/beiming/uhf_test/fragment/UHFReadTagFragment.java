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
    private MeasBoxBean boxBean;//?????????????????????????????????bean
    private List<MeterBean> meterBeanList;
    BarCodeAdpater adapter;
    Button BtClear;
    TextView tv_count;
    Button btnSave;
    /**
     * ????????????
     **/
    Button btnShibie;

    private ContrastBarCodeDialog contrastBarCodeDialog;
    private int MY_PERMISSIONS_REQUEST_FOR_EXCLE = 0x11;

    private String Tag = "UHFReadTagFragment";

    public final static int BROADCAST_INPUT_MODE = 0;//????????????
//    public final static int INPUTTEXT_INPUT_MODE = 1;//???????????????
    public final static int NON_EXTRA = 0;//???
//    public final static int ENTER_EXTRA = 1;//????????????
    private ScansManager mScansManager = null;
//    private int inputmode = INPUTTEXT_INPUT_MODE;
//    private int addExtra = NON_EXTRA;
    private MyReceiver broadcastReceiver = null;
    private IntentFilter filter = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save://??????
//                checkImportExcel();
                //1.??????????????????????????????????????????0?????????1?????????????????????
                if (!checkBoxBarCodeNumber()) {
                    return;
                }
                //2.????????????????????????????????????????????????????????????????????????????????????
                if (!checkBarCodeIsExsit()) {
                    return;
                }
                //3.??????1???2????????????????????????????????????????????????
                Intent intent = new Intent(mContext, RecordDataActivity.class);
                setData();
                intent.putExtra("box", boxBean);
                startActivity(intent);
                break;
            case R.id.BtClear://????????????
                clearData();
                break;
            case R.id.BtInventory://????????????
                readTag();
                break;
        }
    }

    //??????????????????????????????????????????????????????
    private void setData() {
        boxBean = measBoxBeanList.get(0);
        boxBean.setMeters(meterBeanList);
    }

    //???????????????????????????
    private boolean checkBarCodeIsExsit() {
        if (meterBeanList.size() == 0) {
            showToast("????????????????????????");
            return false;
        }
        //???????????????
        if (isExsitBarCodeSaved()) {
            showToast("???????????????????????????????????????");
            return false;
        }
        return true;
    }

    //???????????????????????????????????????
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

    //???????????????????????????
    private boolean checkBoxBarCodeNumber() {
        if (measBoxBeanList.size() == 0) {
            showToast("???????????????????????????");
            return false;
        }
        if (measBoxBeanList.size() > 1) {
            showToast("?????????????????????????????????");
            return false;
        }
        if (measBoxBeanList.get(0).getIsExsit()) {
            showToast("????????????????????????????????????????????????????????????");
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
//        mContext = (MainActivity) getActivity();
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
                //??????????????? 0:????????? ???1???????????? -1:????????????????????????
                switch (barCodeBeanList.get(position).getBarCodeType()) {
                    case "0": //???
                        showBoxHint(true, "??????????????????????????????");
                        break;
                    case "1"://???
                        if (view instanceof TextView) {
                            if (((TextView) view).getText().equals("??????")) {
                                showBoxHint(false, "????????????????????????????????????");
                                return;
                            }
                        }
//                        if (contrastBarCodeDialog == null) {
//                            showContrastBarCodeDialog();
//                        } else {
//                            contrastBarCodeDialog.updateDialogData(getExsitBarCode(), barCodeBeanList.get(position).getBarCode());
//                            contrastBarCodeDialog.show();
//                        }
                        break;
                    default:
                        showToast("???????????????");
                        break;
                }
            }
        });
    }

    //????????????????????????????????????
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
                //????????????bean
                meterBeanList.removeAll(contrastBarCodeDialog.getDeleteMeterList());
                //?????????????????????????????????????????????
                List<String> deleteBarCodeList = contrastBarCodeDialog.getDeleteBarCodeList();
                Iterator<BarCodeBean> iter = barCodeBeanList.iterator();
                while (iter.hasNext()) {
                    BarCodeBean s = iter.next();
                    if (deleteBarCodeList.contains(s.getBarCode())) {
                        iter.remove();
                    }
                }
                adapter.setNewData(barCodeBeanList);
                tv_count.setText(measBoxBeanList.size() + "??????  " + meterBeanList.size() + "??????");
            }
        });
    }

    private void showBoxHint(boolean isBox, String content) {
        DialogUtils.showHintDialog(mContext, content, "???", "???", new OnHintDialogClicklistener() {
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
                tv_count.setText(measBoxBeanList.size() + "??????  " + meterBeanList.size() + "??????");
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * ??????EPC??????????????????????????????????????????
     *
     * @param epc
     */
    private void addEPCToList(String epc) {
        if (!TextUtils.isEmpty(epc)) {
            boolean exist = checkIsExist(epc);
            //??????????????????
            if (!exist) {
                addTag(epc);
                adapter.setNewData(barCodeBeanList);
            }
        }
    }

    //??????????????????
    private void addTag(String epc) {
        String barCode = "";
        String assetNo = "";
        String barCodeType = "-1";
        long ts = System.currentTimeMillis();
        String scanTime = TimeUtils.getTime(ts);


        //??????????????????????????????22??????rfid????????????2???0??????24??????????????????????????????????????????
        //???????????????????????????
        if (epc.length() == 24) {
            barCode = epc.substring(0, epc.length() - 2);
        } else if (epc.length() == 22) {
            barCode = epc;
        } else {
            return;
        }
        assetNo = barCode.substring(barCode.length() - 15, barCode.length() - 1); //????????????7???????????????14???

        // TODO: 2022/9/21 ????????????????????? 
        //???????????????6???7??? ??????????????????
        if (barCode.startsWith("01", 5))//???
            barCodeType = "1";
        else if (barCode.startsWith("05", 5))//???
            barCodeType = "0";
        else {
//            barCodeType = "-1"; //?????????????????????10???????????????22?????????-1??????1 ??????
            barCodeType = "1"; //?????????????????????10???????????????22?????????-1??????1 ??????
            assetNo = barCode.substring(barCode.length() - 11, barCode.length() - 1); //????????????7???????????????14???
        }

        BarCodeBean barCodeBean = new BarCodeBean(epc, barCode, barCodeType, 1, "???", scanTime);
        switch (barCodeType) {
            case "-1":

                break;
            case "0":
                MeasBoxBean measBoxBean = new MeasBoxBean();
                measBoxBean.setBarCode(barCode);
                measBoxBean.setMeasAssetNo(assetNo);
                measBoxBean.setScanTime(scanTime);
                measBoxBean.setTs(ts);
                //??????????????????????????????
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
                //??????????????????????????????????????????????????????????????????????????????
                MeterBean meterBean = new MeterBean();
                meterBean.setBarCode(barCode);//???????????????
                meterBean.setMeterAssetNo(assetNo);
                meterBean.setScanTime(scanTime);
                meterBean.setTs(ts);
                //????????????????????????????????????
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
        tv_count.setText(measBoxBeanList.size() + "??????  " + meterBeanList.size() + "??????");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //??????excel??????
    private void ImportExcel() {
    }

    //????????????
    private void clearData() {
        tv_count.setText("0");

        barCodeBeanList.clear();
        measBoxBeanList.clear();
        meterBeanList.clear();

        adapter.setNewData(barCodeBeanList);
    }

    private void readTag() {
        if (btnShibie.getText().equals(mContext.getString(R.string.btInventory))) {// ????????????
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
        } else {// ????????????
            RfidHelper.getInstance().stopScan();
        }
    }


    private void setViewEnabled(boolean enabled) {
        BtClear.setEnabled(enabled);
    }

    /**
     * ??????EPC??????????????????
     *
     * @param strEPC ??????
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

    //????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_FOR_EXCLE) {//?????????????????????????????????word
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
        mScansManager = (ScansManager) mContext.getSystemService("scans");
        mScansManager.setInputMode(BROADCAST_INPUT_MODE);
        mScansManager.setExtras(NON_EXTRA);
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
            LogPrintUtil.zhangshi("fragment:??????UHFReadTagFragment?????????");

            RfidHelper.getInstance().stopScan();
            destroyBroadcase();
        } else {
            LogPrintUtil.zhangshi("fragment:??????UHFReadTagFragment   ??????");
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
