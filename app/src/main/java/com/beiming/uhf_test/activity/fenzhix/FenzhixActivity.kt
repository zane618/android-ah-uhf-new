package com.beiming.uhf_test.activity.fenzhix

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.beiming.uhf_test.base.BaseActivity
import com.beiming.uhf_test.bean.BarCodeBean
import com.beiming.uhf_test.bean.MeasBoxBean
import com.beiming.uhf_test.bean.pic.AttachmentUpdate
import com.beiming.uhf_test.databinding.ActivityFenzhixBinding
import com.beiming.uhf_test.db.GreenDaoManager
import com.beiming.uhf_test.tools.rfid.IRfidListener
import com.beiming.uhf_test.tools.rfid.RfidHelper
import com.beiming.uhf_test.utils.ConstantUtil
import com.beiming.uhf_test.utils.ToastUtils
import org.greenrobot.eventbus.EventBus

/**
 * created by zhangshi on 2022/8/9.
 */
class FenzhixActivity : BaseActivity() {
    private lateinit var binding: ActivityFenzhixBinding
    private lateinit var boxBean: MeasBoxBean
    private var barCodeBeanList = ArrayList<BarCodeBean>()
    private lateinit var fzxAdapter: FzxBarCodeAdpater

    companion object {
        fun startActivity(context: Context, boxBean: MeasBoxBean) {
            val intent = Intent(context, FenzhixActivity::class.java)
            intent.putExtra("boxBean", boxBean)
            context.startActivity(intent)
        }
    }

    override fun setContentView() {
        binding = ActivityFenzhixBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun getData() {
        intent?.let {
            boxBean = intent.getSerializableExtra("boxBean") as MeasBoxBean
        }
    }

    override fun initView() {
        binding.inTitle.tvTitleName.text = "关联分支箱"
        boxBean.let {
            binding.tvXiang.text = "箱：${boxBean.barCode}"
        }
        fzxAdapter = FzxBarCodeAdpater(barCodeBeanList)
        binding.rvBox.layoutManager = LinearLayoutManager(this)
        binding.rvBox.adapter = fzxAdapter

    }

    override fun initListener() {
        binding.btnScan.setOnClickListener {
            if (!"停止扫描".equals(binding.btnScan.text)) {
                binding.btnScan.text = "停止扫描"
                RfidHelper.getInstance().startScan(object : IRfidListener {
                    override fun onRfidResult(s: String?) {
                        s?.let {
                            var exist = false
                            for (item in barCodeBeanList) {
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
                                    if (barCode.startsWith("05", 5)) {
                                        var barCodeBean = BarCodeBean()
                                        barCodeBean.barCode = barCode
                                        barCodeBeanList.add(barCodeBean)
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
                RfidHelper.getInstance().stopScan()
            }
        }
        binding.BtClear.setOnClickListener {
            barCodeBeanList.clear()
            fzxAdapter.notifyDataSetChanged()
        }
        binding.sure.setOnClickListener { //确认关联
            var fzxCode = ""
            for (item in barCodeBeanList) {
                if (item.isSelect) {
                    fzxCode = item.barCode
                    break
                }
            }
            if (fzxCode.equals("")) {
                ToastUtils.showToast("请选择一个分支箱")
            } else {
                val measBoxBeanDao = GreenDaoManager.getInstance().newSession.measBoxBeanDao
                boxBean.fenzhixCode = fzxCode
                measBoxBeanDao.update(boxBean)
                val attachmentUpdate = AttachmentUpdate()
                attachmentUpdate.tag = ConstantUtil.CLEAR_READ_TAG_DATA
                attachmentUpdate.data = boxBean
                EventBus.getDefault().post(attachmentUpdate)
                finish()
                ToastUtils.showToast("关联分支箱成功")
            }
        }
    }

    override fun initData() {
    }

    override fun onPause() {
        super.onPause()
        RfidHelper.getInstance().stopScan()
    }
}