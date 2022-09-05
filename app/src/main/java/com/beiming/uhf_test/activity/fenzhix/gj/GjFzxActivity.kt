package com.beiming.uhf_test.activity.fenzhix.gj

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import com.beiming.uhf_test.base.BaseActivity
import com.beiming.uhf_test.bean.FenzhiBoxBean
import com.beiming.uhf_test.bean.MeasBoxBean
import com.beiming.uhf_test.bean.pic.AttachmentUpdate
import com.beiming.uhf_test.databinding.ActivityFenzhixGjBinding
import com.beiming.uhf_test.db.GreenDaoManager
import com.beiming.uhf_test.greendao.gen.FenzhiBoxBeanDao
import com.beiming.uhf_test.greendao.gen.MeasBoxBeanDao
import com.beiming.uhf_test.utils.ConstantUtil
import com.beiming.uhf_test.utils.LogPrintUtil
import com.beiming.uhf_test.utils.TimeUtils
import com.beiming.uhf_test.utils.ToastUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * created by zhangshi on 2022/8/9.
 */
class GjFzxActivity : BaseActivity() {
    private lateinit var binding: ActivityFenzhixGjBinding
    private var fzxList = ArrayList<FenzhiBoxBean>()
    private lateinit var fzxAdpater: GjFzxAdpater
    val fzxDao = GreenDaoManager.getInstance().session.fenzhiBoxBeanDao

    val boxDao = GreenDaoManager.getInstance().session.measBoxBeanDao
    private var boxList = ArrayList<MeasBoxBean>()
    private lateinit var boxAdpater: GjBoxAdpater


    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, GjFzxActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun setContentView() {
        binding = ActivityFenzhixGjBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun getData() {
        intent?.let {

        }
    }

    override fun initView() {
        binding.inTitle.tvTitleName.text = "挂接分支箱"
        binding.inTitle.tvRight.let {
            it.visibility = View.VISIBLE
            it.text = "确认挂接"
            it.setOnClickListener {
                guajie()
            }
        }
        binding.inTitle.ivBack.setOnClickListener {
            finish()
        }
        initFzxRv()
        initBoxRv()

    }

    /**
     * 挂接
     */
    fun guajie() {
        if (fzxList.isEmpty() || (!fzxList.any { it.checked })) {
            ToastUtils.showToast("至少选择一个分支箱")
            return
        }
        if (boxList.isEmpty() || (!boxList.any { it.checked })) {
            ToastUtils.showToast("至少选择一个计量箱")
            return
        }

        val fzx = fzxList.first { it.checked }
        for (box in boxList) {
            box.fenzhixCode = fzx.barCode
            box.fenzhixAssetNo = fzx.assetNo
        }
        boxDao.updateInTx(boxList)
        finish()
        //刷新本地数据页面
        val attachmentUpdate = AttachmentUpdate()
        attachmentUpdate.tag = ConstantUtil.CLEAR_READ_TAG_DATA
        EventBus.getDefault().post(attachmentUpdate)

    }

    private fun initFzxRv() {
        fzxAdpater = GjFzxAdpater(fzxList)
        binding.rvFzxBox.layoutManager = LinearLayoutManager(this)
        binding.rvFzxBox.adapter = fzxAdpater

        binding.tvFzxDate.setOnClickListener {
            var calendar = Calendar.getInstance()
            var dialog: DatePickerDialog = DatePickerDialog(
                this,
                object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        val desc: String = java.lang.String.format(
                            "您选择的日期是：%s年%s月%s日",
                            year,
                            month + 1,
                            dayOfMonth
                        )
                        val s = TimeUtils.formatDate(year, month + 1, dayOfMonth)
                        val ts = TimeUtils.toTs(s)
                        LogPrintUtil.zhangshi("ts:" + ts)
                        updateFzxList(s)
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    }

    private fun initBoxRv() {
        boxAdpater = GjBoxAdpater(boxList)
        binding.rvBox.layoutManager = LinearLayoutManager(this)
        binding.rvBox.adapter = boxAdpater

        binding.tvBoxDate.setOnClickListener {
            var calendar = Calendar.getInstance()
            var dialog: DatePickerDialog = DatePickerDialog(
                this,
                object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {
                        val desc: String = java.lang.String.format(
                            "您选择的日期是：%s年%s月%s日",
                            year,
                            month + 1,
                            dayOfMonth
                        )
                        val s = TimeUtils.formatDate(year, month + 1, dayOfMonth)
                        val ts = TimeUtils.toTs(s)
                        LogPrintUtil.zhangshi("ts:" + ts)
                        updateBoxList(s)
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    }

    override fun initListener() {
    }

    override fun initData() {
        val day = TimeUtils.getY_M_D_Time()
        updateFzxList(day)
        updateBoxList(day)

    }

    fun updateFzxList(day: String) {
        binding.tvFzxDate.text = day
        //获取时间戳
        val todayTs = TimeUtils.toTs(day)
        var fzxTmpList = fzxDao.queryBuilder().where(
            FenzhiBoxBeanDao.Properties.Ts.ge(todayTs), FenzhiBoxBeanDao.Properties.Ts.le(
                todayTs +
                        TimeUtils.ssc
            )
        ).build().list()
        fzxList.clear()
        fzxList.addAll(fzxTmpList)
        fzxAdpater.notifyDataSetChanged()
    }

    fun updateBoxList(day: String) {
        binding.tvBoxDate.text = day
        //获取时间戳
        val todayTs = TimeUtils.toTs(day)
        var boxTmpList = boxDao.queryBuilder().where(
            MeasBoxBeanDao.Properties.Ts.ge(todayTs), MeasBoxBeanDao.Properties.Ts.le(
                todayTs +
                        TimeUtils.ssc
            )
        ).build().list()
        boxList.clear()
        boxList.addAll(boxTmpList)
        boxAdpater.notifyDataSetChanged()
    }

}