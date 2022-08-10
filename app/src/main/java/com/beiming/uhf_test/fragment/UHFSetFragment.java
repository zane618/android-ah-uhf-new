package com.beiming.uhf_test.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.activity.MainActivity;
import com.kongzue.baseframework.interfaces.BindView;
import com.lidroid.xutils.ViewUtils;
import com.uhf.api.cls.Reader;


public class UHFSetFragment extends KeyDwonFragment implements View.OnClickListener {
    @BindView(R.id.sp_setWorkBand)
    Spinner sp_frequency_band;
    @BindView(R.id.sp_setPower)
    Spinner sp_set_power;
    @BindView(R.id.bt_setPower)
    Button bt_setPower;
    @BindView(R.id.bt_getPower)
    Button bt_getPower;
    @BindView(R.id.rg_module)
    RadioGroup rg_module;

    @BindView(R.id.bt_getFrequency)
    Button bt_getFrequency;
    @BindView(R.id.bt_getT)
    Button bt_getT;
    @BindView(R.id.et_temperature)
    EditText et_T;
    @BindView(R.id.bt_setFrequency)
    Button bt_setFrequency;
    int power = 3000;
    private MainActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater
                .inflate(R.layout.fragment_setting, container, false);
        ViewUtils.inject(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();
        sp_frequency_band = getView().findViewById(R.id.sp_setWorkBand);
        sp_set_power = getView().findViewById(R.id.sp_setPower);
        bt_setPower = getView().findViewById(R.id.bt_setPower);
        bt_getPower = getView().findViewById(R.id.bt_getPower);
        rg_module = getView().findViewById(R.id.rg_module);
        bt_getFrequency = getView().findViewById(R.id.bt_getFrequency);
        bt_getT = getView().findViewById(R.id.bt_getT);
        et_T = getView().findViewById(R.id.et_temperature);
        bt_setFrequency = getView().findViewById(R.id.bt_setFrequency);

        sp_frequency_band.setDropDownVerticalOffset(50);
        sp_frequency_band.setDropDownHorizontalOffset(10);
        sp_frequency_band.setSelection(1);
        sp_set_power.setDropDownVerticalOffset(50);
        sp_set_power.setDropDownHorizontalOffset(10);
        sp_frequency_band.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置频段
        bt_setFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrequency();
            }
        });
        //获取频段
        bt_getFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFrequency();
            }
        });

        //获取模块当前温度
        bt_getT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_T.setText(mContext.getModuleTemperature()+"");
            }
        });


        //设置功率
        bt_setPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power = sp_set_power.getSelectedItemPosition() * 100 + 500;
                setPower(power);
            }
        });

        rg_module.check(mContext.inventoryEpc ? R.id.rb_epc : R.id.rb_tid);
        rg_module.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_epc:
                        mContext.inventoryEpc = true;
                        break;
                    case R.id.rb_tid:
                        mContext.inventoryEpc = false;
                        break;
                }

                //设置附加数据内容,如果纯盘EPC 就传null
                if (mContext.inventoryEpc) {
                    Reader.READER_ERR subjoinErr = mContext.uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, null);
                } else {
                    Reader.EmbededData_ST subjoinSet = mContext.uhfReader.new EmbededData_ST();//如果不用附加数据时 需将此参数设null ,否则会影响读取效率
                    subjoinSet.bank = 2;//附加数据的块区，值位 0,1,2,3 对应Gen2 标签的4个区
                    subjoinSet.startaddr = 0;//附加数据的起始读地址，字节为单位
                    subjoinSet.bytecnt = 12;//附近数据的读取长度地址，字节为单位
                    subjoinSet.accesspwd = null;//不用密码的时候置空
                    Reader.READER_ERR subjoinErr = mContext.uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, subjoinSet);
                }
                mContext.settingsUtil.commit("inventory", mContext.inventoryEpc);
                showToast("设置成功");
            }
        });
        //获取功率
        bt_getPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPower();
            }
        });
//        getPower();
    }

    @Override
    protected void initData() {}

    private void getFrequency() {
        Reader.Region_Conf[] rcf2 = new Reader.Region_Conf[1];
        Reader.READER_ERR er = mContext.uhfReader.ParamGet(
                Reader.Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rcf2);
        if (er == Reader.READER_ERR.MT_OK_ERR) {
            switch (rcf2[0]) {
                case RG_EU:
                    sp_frequency_band.setSelection(4);
                    break;
                case RG_EU2:
                    sp_frequency_band.setSelection(5);
                    break;
                case RG_EU3:
                    sp_frequency_band.setSelection(6);
                    break;
                case RG_KR:
                    sp_frequency_band.setSelection(3);
                    break;
                case RG_NA:
                    sp_frequency_band.setSelection(1);
                    break;
                case RG_OPEN:
                    sp_frequency_band.setSelection(9);
                    break;
                case RG_PRC2:
                    sp_frequency_band.setSelection(10);
                    break;
                default:
                    sp_frequency_band.setSelection(0);
                    break;
            }
//            toast("获取成功");
        } else {
//            toast("获取失败" + er);
        }
    }

    private void setFrequency() {
        Reader.Region_Conf rre;
        switch (sp_frequency_band.getSelectedItemPosition()) {
            case 0:
                rre = Reader.Region_Conf.RG_PRC;
                break;
            case 1:
                rre = Reader.Region_Conf.RG_NA;
                break;
            case 2:
                rre = Reader.Region_Conf.RG_NONE;
                break;
            case 3:
                rre = Reader.Region_Conf.RG_KR;
                break;
            case 4:
                rre = Reader.Region_Conf.RG_EU;
                break;
            case 5:
                rre = Reader.Region_Conf.RG_EU2;
                break;
            case 6:
                rre = Reader.Region_Conf.RG_EU3;
                break;
            case 7:
            case 8:
                rre = Reader.Region_Conf.RG_NONE;
                break;
            case 9:
                rre = Reader.Region_Conf.RG_OPEN;
                break;
            case 10:
                rre = Reader.Region_Conf.RG_PRC2;
                break;
            default:
                rre = Reader.Region_Conf.RG_NONE;
                break;
        }

        Reader.READER_ERR er = mContext.uhfReader.ParamSet(
                Reader.Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rre);
        if (er == Reader.READER_ERR.MT_OK_ERR) {
            showToast("设置成功");
        } else {
            showToast("设置失败 " + er);
        }
    }

    //获取功率
    public void getPower() {
        Reader.AntPowerConf apcf2 = mContext.uhfReader.new AntPowerConf();
        Reader.READER_ERR er = mContext.uhfReader.ParamGet(
                Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf2);
        if (er == Reader.READER_ERR.MT_OK_ERR) {
            int select = (apcf2.Powers[0].readPower - 500) / 100;
            if (select == 28) {
                select = 25;
            }
            sp_set_power.setSelection(select);
//            toast("获取成功");
        } else {
//            toast("获取失败 " + er);
        }

    }

    //设置功率
    public void setPower(int setPower) {
        Reader.AntPowerConf apcf = mContext.uhfReader.new AntPowerConf();
        apcf.antcnt = 1;
        Reader.AntPower jaap = mContext.uhfReader.new AntPower();
        jaap.antid = 1;
        jaap.readPower = (short) setPower;
        jaap.writePower = (short) setPower;
        apcf.Powers[0] = jaap;
        Reader.READER_ERR powerSet = mContext.uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);
        if (powerSet == Reader.READER_ERR.MT_OK_ERR) {
            showToast("设置成功");
        } else {
            showToast("设置失败 " + powerSet);
        }
    }

    @Override
    public void onClick(View view) {

    }

}
