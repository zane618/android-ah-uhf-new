package com.beiming.uhf_test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.LinearLayout
import com.beiming.uhf_test.R
import com.beiming.uhf_test.bean.MeasBoxBean

/**
 * created by zhangshi on 2022/7/22.
 */
class DoorInfoShowLayout(context: Context, attributeSet: AttributeSet?=null): LinearLayout (
        context, attributeSet){

    private var zsGao: TextView
    private var zsKuan: TextView
    private var zxGao: TextView
    private var zxKuan: TextView

    private var ysGao: TextView
    private var ysKuan: TextView
    private var yxGao: TextView
    private var yxKuan: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_record_data_show_include, this)

        zsGao = findViewById(R.id.tv_zs_gao)
        zsKuan = findViewById(R.id.tv_zs_kuan)
        zxGao = findViewById(R.id.tv_zx_gao)
        zxKuan = findViewById(R.id.tv_zx_kuan)

        ysGao = findViewById(R.id.tv_ys_gao)
        ysKuan = findViewById(R.id.tv_ys_kuan)
        yxGao = findViewById(R.id.tv_yx_gao)
        yxKuan = findViewById(R.id.tv_yx_kuan)
    }

    fun setDatas(measBoxBean: MeasBoxBean) {
        measBoxBean.let {
            zsGao.text = it.zsGao
            zsKuan.text = it.zsKuan
            zxGao.text = it.zxGao
            zxKuan.text = it.zxKuan

            ysGao.text = it.ysGao
            ysKuan.text = it.yxKuan
            yxGao.text = it.yxGao
            yxKuan.text = it.yxKuan
        }
    }

}