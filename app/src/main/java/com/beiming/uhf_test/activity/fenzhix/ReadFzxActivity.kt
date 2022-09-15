package com.beiming.uhf_test.activity.fenzhix

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.beiming.uhf_test.base.BaseActivity
import com.beiming.uhf_test.bean.FenzhiBoxBean
import com.beiming.uhf_test.bean.LocationBean
import com.beiming.uhf_test.bean.MeasBoxBean
import com.beiming.uhf_test.databinding.ActivityFenzhixReadBinding
import com.beiming.uhf_test.db.GreenDaoManager
import com.beiming.uhf_test.helper.map.LocationHelper
import com.beiming.uhf_test.tools.rfid.IRfidListener
import com.beiming.uhf_test.tools.rfid.RfidHelper
import com.beiming.uhf_test.utils.*

/**
 * created by zhangshi on 2022/8/9.
 */
class ReadFzxActivity : BaseActivity() {
    private lateinit var binding: ActivityFenzhixReadBinding
    private var fenzhiboxList = ArrayList<FenzhiBoxBean>()
    private lateinit var fzxAdapter: FzxBarCodeAdpater
    private var mLocationBean: LocationBean? = FastJson.parseObject(
        SharedPreferencesUtil.getInstance()
            .getStringValue(ConstantUtil.LAST_LOCATION), LocationBean::class.java
    )

    companion object {
        fun startActivity(context: Context, boxBean: MeasBoxBean) {
            val intent = Intent(context, ReadFzxActivity::class.java)
            intent.putExtra("boxBean", boxBean)
            context.startActivity(intent)
        }
    }

    override fun setContentView() {
        binding = ActivityFenzhixReadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun getData() {
        intent?.let {

        }
    }

    override fun initView() {
        binding.inTitle.tvTitleName.text = "扫描分支箱"
        fzxAdapter = FzxBarCodeAdpater(fenzhiboxList)
        binding.rvBox.layoutManager = LinearLayoutManager(this)
        binding.rvBox.adapter = fzxAdapter

        binding.btnSave.setOnClickListener {
            val dao = GreenDaoManager.getInstance().session.fenzhiBoxBeanDao
            dao.insertInTx(fenzhiboxList)
            ToastUtils.showToast("保存成功")
            finish()
        }
        binding.inTitle.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun initListener() {
        binding.btnScan.setOnClickListener {
            if (!"停止扫描".equals(binding.btnScan.text)) {
                binding.btnScan.text = "停止扫描"
//                handler.sendEmptyMessage(1)

                RfidHelper.getInstance().startScan(object : IRfidListener {
                    override fun onRfidResult(s: String?) {
                        s?.let {
                            var exist = false
                            for (item in fenzhiboxList) {
                                if (s.startsWith(item.barCode)) {
                                    exist = true
                                    break
                                }
                            }

                            if (!exist) {
                                //不存在
                                if (s.length == 24) { //标准电子标签
                                    val barCode = s.substring(0, s.length - 2)
                                    val assetNo = barCode.substring(
                                        barCode.length - 15,
                                        barCode.length - 1
                                    )
                                    if (barCode.startsWith("88", 5)) {
                                        val fenzhiBoxBean = FenzhiBoxBean()
                                        fenzhiBoxBean.barCode = barCode
                                        fenzhiBoxBean.assetNo = assetNo
                                        fenzhiBoxBean.ts = System.currentTimeMillis()
                                        mLocationBean?.let {
                                            fenzhiBoxBean.instAddr = it.address
                                            fenzhiBoxBean.setGps_X(it.longitude)
                                            fenzhiBoxBean.setGps_Y(it.latitude)
                                            fenzhiBoxBean.setGps_Z(it.altitude)
                                        }
                                        fenzhiboxList.add(fenzhiBoxBean)
                                        fzxAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }

                    override fun onStop() {
                        binding.btnScan.text = "扫描分支箱"
                    }

                })
            } else {
                binding.btnScan.text = "扫描分支箱"
                RfidHelper.getInstance().stopScan()
            }
        }
        binding.BtClear.setOnClickListener {
            fenzhiboxList.clear()
            fzxAdapter.notifyDataSetChanged()
        }
    }

    override fun initData() {
    }

    override fun onPause() {
        super.onPause()
        RfidHelper.getInstance().stopScan()
    }

    override fun onResume() {
        super.onResume()
        //展示定位数据

        //展示定位数据
        LocationHelper().setListener { locationBean: LocationBean? ->
            if (null != locationBean) {
                LogPrintUtil.zhangshi("定位成功 getNewAppLocation:" + FastJson.toJSONString(locationBean))
                SharedPreferencesUtil.getInstance()
                    .setObjectToShare(ConstantUtil.LAST_LOCATION, locationBean)
                if (locationBean != null && locationBean.address != null) {
                    mLocationBean = locationBean
//                    tvAddr.setText(locationBean.address)
//                    boxBean.setInstAddr(locationBean.address)
//                    boxBean.setGps_X(locationBean.longitude)
//                    boxBean.setGps_Y(locationBean.latitude)
//                    boxBean.setGps_Z(locationBean.altitude)
                } else {
//                    tvAddr.setText("暂未获取到定位信息")
                }
            } else {
                //定位失败
//                tvAddr.setText("暂未获取到定位信息")
                LogPrintUtil.zhangshi("定位失败 getNewAppLocation:" + FastJson.toJSONString(locationBean))
            }
        }.startLocation()
    }
}