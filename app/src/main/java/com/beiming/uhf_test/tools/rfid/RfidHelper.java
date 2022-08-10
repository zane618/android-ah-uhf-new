package com.beiming.uhf_test.tools.rfid;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.beiming.uhf_test.App;
import com.beiming.uhf_test.R;
import com.beiming.uhf_test.tools.AppManager;
import com.beiming.uhf_test.tools.UIHelper;
import com.beiming.uhf_test.utils.LogPrintUtil;
import com.hc.pda.HcPowerCtrl;
import com.kongzue.baseframework.util.SettingsUtil;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.ReadListener;
import com.uhf.api.cls.Reader;

import java.util.HashMap;

/**
 * created by zhangshi on 2022/8/9.
 */
public class RfidHelper implements ReadListener {
    private static RfidHelper instance = null;
    private Context context;
    private IRfidListener listener;

    public void setListener(IRfidListener listener) {
        this.listener = listener;
    }

    public static RfidHelper getInstance() {
        if (instance == null) {
            synchronized (RfidHelper.class) {
                if (instance == null) {
                    instance = new RfidHelper();
                }
            }
        }
        return instance;
    }

    private RfidHelper() {
        context = App.getContext();
        initUHF();
        initSound();
    }


    public Reader uhfReader;
    RfidPower power;
    HcPowerCtrl ctrl;
    public SettingsUtil settingsUtil;
    public boolean inventoryEpc = false;//盘存模式，EPC 或 TID

    boolean isReading = false;//是否正在扫描
    String TAG = "RfidHelper";
    boolean isSetStop = false;//读取时长后停止
    public boolean isR2000 = true;


    /**
     * 初始化
     */
    private void initUHF() {
        //初始化uhf
        settingsUtil = new SettingsUtil("uhf");
        inventoryEpc = settingsUtil.getBoolean("inventory", false);
        ctrl = new HcPowerCtrl();
        ctrl.identityPower(1);
        if (uhfReader == null) {
            uhfReader = new Reader();
            new InitUHFTask().execute();
        }
    }

    @Override
    public void tagRead(Reader reader, Reader.TAGINFO[] taginfos) {

    }

    public class InitUHFTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            power = new RfidPower(RfidPower.PDATYPE.NONE, context);
            if (power.PowerUp()) {
//                Reader.READER_ERR reader_err = uhfReader.InitReader_Notype("/dev/ttyS1", 1);
                Reader.READER_ERR reader_err = uhfReader.InitReader_Notype("/dev/ttysWK1", 1);
                Log.e(TAG, "doInBackground: "+reader_err );
                if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                    uhfReader.addReadListener(RfidHelper.this);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            mypDialog.cancel();
            if (result) {
                Reader.AntPowerConf apcf = uhfReader.new AntPowerConf();
                apcf.antcnt = 1;
                Reader.AntPowerConf apcf2 = uhfReader.new AntPowerConf();

                //获取功率
                uhfReader.ParamGet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf2);
                int session = settingsUtil.getInt("session", 1);
                //设置session 值
                uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION, new int[]{session});
                //设置附加数据内容,如果纯盘EPC 就传null
//                if (inventoryEpc) {
                uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, null);
//                } else {
//                    Reader.EmbededData_ST subjoinSet = uhfReader.new EmbededData_ST();//如果不用附加数据时 需将此参数设null ,否则会影响读取效率
//                    subjoinSet.bank = 2;//附加数据的块区，值位 0,1,2,3 对应Gen2 标签的4个区
//                    subjoinSet.startaddr = 0;//附加数据的起始读地址，字节为单位
//                    subjoinSet.bytecnt = 12;//附近数据的读取长度地址，字节为单位
//                    subjoinSet.accesspwd = null;//不用密码的时候置空
//                    uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, subjoinSet);
//                }
                Reader.HardwareDetails val = uhfReader.new HardwareDetails();
                uhfReader.GetHardwareDetails(val);
                if (val.module.toString().equals("MODOULE_SLR5300")) {
                    isR2000 = false;
                } else {
                    isR2000 = true;
                }
                Log.e(TAG, "模块: " + val.module.toString() );
                Toast.makeText(context,   " 初始化成功,功率 " + apcf2.Powers[0].readPower / 100 + "db", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "初始化失败", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mypDialog = new ProgressDialog(AppManager.getAppManager().getTopActivity());
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("初始化...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();

        }
    }


    public void startScan(IRfidListener listener) {
        this.listener = listener;
        if (isR2000) {
            startScanToR2000();
        } else {
            startScanTo5300();
        }
    }


    public void startScanTo5300() {
        isReading = true;
//        bt_stopScan.setEnabled(true);
//        bt_startScan.setEnabled(false);
        new InventoryRawThread().start();
    }

    public void startScanToR2000() {
        if (!isReading) {
            int[] ants = new int[]{1};
            int option = 0;
            if (!inventoryEpc) {
                option = 32768;
            }
            Reader.READER_ERR reader_err = uhfReader.AsyncStartReading(ants, 1, 16);
            new InventoryThread().start();
            if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                isReading = true;
//                bt_stopScan.setEnabled(true);
//                bt_startScan.setEnabled(false);
                String time = "10";
                //是否停止扫描
                if (isSetStop) {
                    handler.postDelayed(this::stopScan, Integer.parseInt(time)*1000);
                }
            } else {
                isReading = false;
//                toast("错误码-->" + reader_err);
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
            Reader.READER_ERR reader_err = uhfReader.AsyncStopReading();
            if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                //回调出去
                if (listener != null) {
                    listener.onStop();
                }
            } else {
                UIHelper.ToastMessage(AppManager.getAppManager().getTopActivity(),
                        R.string.uhf_msg_inventory_stop_fail);
            }
        }

    }

    public class InventoryRawThread extends Thread {
        @Override
        public void run() {
            synchronized (this) {
                while (isReading) {
                    int[] tagcnt = new int[1];
                    tagcnt[0] = 0;
                    int ants[] = {1};
                    Reader.READER_ERR reader_err = uhfReader.TagInventory_Raw(ants, 1, (short) 100, tagcnt);

                    if (reader_err == Reader.READER_ERR.MT_OK_ERR) {
                        Log.e(TAG, "5300 一次标签返回个数: " +tagcnt[0] );
                        for (int i = 0; i < tagcnt[0]; i++) {
                            Reader.TAGINFO taginfo = uhfReader.new TAGINFO();
                            Reader.READER_ERR reader_err1 = uhfReader.GetNextTag(taginfo);

                            if (reader_err1 == Reader.READER_ERR.MT_OK_ERR) {
                                String epc = Reader.bytes_Hexstr(taginfo.EpcId);
                                sendMsg(1, epc);
                                playSound(4);
                            } else {
                                Log.e(TAG, "5300 获取标签失败: " +reader_err1 );
                            }
                        }

                    } else {
                        handler.sendEmptyMessage(3);
                        isReading = false;
//                        toast("开启失败 " + reader_err);
                        Log.e(TAG, "5300 开启失败: " +reader_err );
                    }
                }
            }
        }
    }


    public class InventoryThread extends Thread {
        @Override
        public void run() {
            int[] tagcnt = new int[1];
            synchronized (this) {
                while (isReading) {
                    Reader.READER_ERR er;
                    er = uhfReader.AsyncGetTagCount(tagcnt);
                    if (er == Reader.READER_ERR.MT_OK_ERR) {
                        if (tagcnt[0] > 0) {
                            for (int i = 0; i < tagcnt[0]; i++) {
                                Reader.TAGINFO taginfo = uhfReader.new TAGINFO();
                                er = uhfReader.AsyncGetNextTag(taginfo);
                                if (er == Reader.READER_ERR.MT_OK_ERR) {
//                                    if (inventoryEpc) {
//                                        //盘存模式 EPC
                                    String epc = Reader.bytes_Hexstr(taginfo.EpcId);
                                    sendMsg(1, epc);
//                                    } else {
                                    //盘存TID模式
//                                        if (taginfo.EmbededDatalen > 0) {
//                                            char[] out = new char[taginfo.EmbededDatalen * 2];
//                                            uhfReader.Hex2Str(taginfo.EmbededData, taginfo.EmbededDatalen, out);
//                                            String tid = String.valueOf(out);
//                                            addEPCToList(tid);
//                                        }
//                                    }
                                    playSound(4);
//                                    SystemClock.sleep(30);
                                }
                            }
                        }
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

    Handler handler = new Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (listener != null) {
                        listener.onRfidResult(msg.obj.toString());
                    }
                    LogPrintUtil.zhangshi(TAG + msg.obj);
                    break;
                case 3:
                    break;
                default:
            }
            return false;
        }
    });

    HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
    private SoundPool soundPool;

    private void initSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        soundMap.put(1, soundPool.load(context, R.raw.beeper_short, 1));
        soundMap.put(2, soundPool.load(context, R.raw.beeper, 1));
        soundMap.put(3, soundPool.load(context, R.raw.scan_buzzer, 1));
        soundMap.put(4, soundPool.load(context, R.raw.beep330, 1));
        soundMap.put(5, soundPool.load(context, R.raw.scan_new, 1));
        soundMap.put(6, soundPool.load(context, R.raw.beep333, 1));
    }

    /**
     * 播放提示音
     *
     * @param id 成功1，失败2
     */
    public void playSound(int id) {
        soundPool.play(soundMap.get(id), 1, // 左声道音量
                1, // 右声道音量
                1, // 优先级，0为最低
                0, // 循环次数，0无不循环，-1无永远循环
                1 // 回放速度 ，该值在0.5-2.0之间，1为正常速度
        );
    }

    public void exitUHF() {
        if (power != null) {
            power.PowerDown();
        }
        if (ctrl != null) {
            ctrl.identityPower(0);
        }
        if (uhfReader != null) {
            uhfReader.CloseReader();
            uhfReader = null;
        }
    }
}
