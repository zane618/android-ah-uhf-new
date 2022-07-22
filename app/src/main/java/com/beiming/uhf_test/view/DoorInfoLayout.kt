package com.beiming.uhf_test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.beiming.uhf_test.R

/**
 * created by zhangshi on 2022/7/22.
 */
class DoorInfoLayout(context: Context, attributeSet: AttributeSet?=null): LinearLayout (
        context, attributeSet){

    private var zsGao: EditText
    private var zsKuan: EditText
    private var zxGao: EditText
    private var zxKuan: EditText

    private var ysGao: EditText
    private var ysKuan: EditText
    private var yxGao: EditText
    private var yxKuan: EditText

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_record_data_include, this)

        zsGao = findViewById(R.id.tv_zs_gao)
        zsKuan = findViewById(R.id.tv_zs_kuan)
        zxGao = findViewById(R.id.tv_zx_gao)
        zxKuan = findViewById(R.id.tv_zx_kuan)

        ysGao = findViewById(R.id.tv_ys_gao)
        ysKuan = findViewById(R.id.tv_ys_kuan)
        yxGao = findViewById(R.id.tv_yx_gao)
        yxKuan = findViewById(R.id.tv_yx_kuan)
    }

    fun getZsGao(): String {
        return zsGao.text.toString()
    }

    fun getZsKuan(): String {
        return zsKuan.text.toString()
    }

    fun getZxGao(): String {
        return zxGao.text.toString()
    }

    fun getZxKuan(): String {
        return zxKuan.text.toString()
    }

    fun getYsGao(): String {
        return ysGao.text.toString()
    }

    fun getYsKuan(): String {
        return ysKuan.text.toString()
    }

    fun getYxGao(): String {
        return yxGao.text.toString()
    }

    fun getYxKuan(): String {
        return yxKuan.text.toString()
    }
}