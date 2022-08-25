package com.beiming.uhf_test.activity.fenzhix

import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import com.beiming.uhf_test.base.BaseActivity
import com.beiming.uhf_test.bean.FenzhiBoxBean
import com.beiming.uhf_test.bean.LocationBean
import com.beiming.uhf_test.bean.MeasBoxBean
import com.beiming.uhf_test.databinding.ActivityFenzhixGjBinding
import com.beiming.uhf_test.databinding.ActivityFenzhixReadBinding
import com.beiming.uhf_test.db.GreenDaoManager
import com.beiming.uhf_test.greendao.gen.FenzhiBoxBeanDao
import com.beiming.uhf_test.helper.map.LocationHelper
import com.beiming.uhf_test.tools.rfid.RfidHelper
import com.beiming.uhf_test.utils.ConstantUtil
import com.beiming.uhf_test.utils.FastJson
import com.beiming.uhf_test.utils.LogPrintUtil
import com.beiming.uhf_test.utils.SharedPreferencesUtil
import kotlin.collections.ArrayList

/**
 * created by zhangshi on 2022/8/9.
 */
class GjFzxActivity : BaseActivity() {
    private lateinit var binding: ActivityFenzhixGjBinding
    private var fenzhiboxList = ArrayList<FenzhiBoxBean>()
    private lateinit var fzxAdapter: FzxBarCodeAdpater
    val dao = GreenDaoManager.getInstance().session.fenzhiBoxBeanDao


    companion object {
        fun startActivity(context: Context, boxBean: MeasBoxBean) {
            val intent = Intent(context, GjFzxActivity::class.java)
            intent.putExtra("boxBean", boxBean)
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
        fzxAdapter = FzxBarCodeAdpater(fenzhiboxList)
        binding.rvBox.layoutManager = LinearLayoutManager(this)
        binding.rvBox.adapter = fzxAdapter

        binding.inTitle.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun onPause() {
        super.onPause()
        RfidHelper.getInstance().stopScan()
    }

}