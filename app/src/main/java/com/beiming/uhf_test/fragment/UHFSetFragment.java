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
import com.beiming.uhf_test.tools.rfid.RfidHelper;
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

        //????????????
        bt_setFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrequency();
            }
        });
        //????????????
        bt_getFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFrequency();
            }
        });

        //????????????????????????
        bt_getT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_T.setText(RfidHelper.getInstance().getModuleTemperature()+"");
            }
        });


        //????????????
        bt_setPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power = sp_set_power.getSelectedItemPosition() * 100 + 500;
                setPower(power);
            }
        });

        rg_module.check(RfidHelper.getInstance().inventoryEpc ? R.id.rb_epc : R.id.rb_tid);
        rg_module.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_epc:
                        RfidHelper.getInstance().inventoryEpc = true;
                        break;
                    case R.id.rb_tid:
                        RfidHelper.getInstance().inventoryEpc = false;
                        break;
                }

                //????????????????????????,????????????EPC ??????null
                if (RfidHelper.getInstance().inventoryEpc) {
                    Reader.READER_ERR subjoinErr = RfidHelper.getInstance().uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, null);
                } else {
                    Reader.EmbededData_ST subjoinSet = RfidHelper.getInstance().uhfReader.new EmbededData_ST();//??????????????????????????? ??????????????????null ,???????????????????????????
                    subjoinSet.bank = 2;//?????????????????????????????? 0,1,2,3 ??????Gen2 ?????????4??????
                    subjoinSet.startaddr = 0;//????????????????????????????????????????????????
                    subjoinSet.bytecnt = 12;//???????????????????????????????????????????????????
                    subjoinSet.accesspwd = null;//???????????????????????????
                    Reader.READER_ERR subjoinErr = RfidHelper.getInstance().uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, subjoinSet);
                }
                RfidHelper.getInstance().settingsUtil.commit("inventory", RfidHelper.getInstance().inventoryEpc);
                showToast("????????????");
            }
        });
        //????????????
        bt_getPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPower();
            }
        });
        getPower();
    }

    @Override
    protected void initData() {}

    private void getFrequency() {
        Reader.Region_Conf[] rcf2 = new Reader.Region_Conf[1];
        Reader.READER_ERR er = RfidHelper.getInstance().uhfReader.ParamGet(
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
//            toast("????????????");
        } else {
//            toast("????????????" + er);
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

        Reader.READER_ERR er = RfidHelper.getInstance().uhfReader.ParamSet(
                Reader.Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rre);
        if (er == Reader.READER_ERR.MT_OK_ERR) {
            showToast("????????????");
        } else {
            showToast("???????????? " + er);
        }
    }

    //????????????
    public void getPower() {
        Reader.AntPowerConf apcf2 = RfidHelper.getInstance().uhfReader.new AntPowerConf();
        Reader.READER_ERR er = RfidHelper.getInstance().uhfReader.ParamGet(
                Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf2);
        if (er == Reader.READER_ERR.MT_OK_ERR) {
            int select = (apcf2.Powers[0].readPower - 500) / 100;
            if (select == 28) {
                select = 25;
            }
            sp_set_power.setSelection(select);
//            toast("????????????");
        } else {
//            toast("???????????? " + er);
        }

    }

    //????????????
    public void setPower(int setPower) {
        Reader.AntPowerConf apcf = RfidHelper.getInstance().uhfReader.new AntPowerConf();
        apcf.antcnt = 1;
        Reader.AntPower jaap = RfidHelper.getInstance().uhfReader.new AntPower();
        jaap.antid = 1;
        jaap.readPower = (short) setPower;
        jaap.writePower = (short) setPower;
        apcf.Powers[0] = jaap;
        Reader.READER_ERR powerSet = RfidHelper.getInstance().uhfReader.ParamSet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);
        if (powerSet == Reader.READER_ERR.MT_OK_ERR) {
            showToast("????????????");
        } else {
            showToast("???????????? " + powerSet);
        }
    }

    @Override
    public void onClick(View view) {

    }

}
