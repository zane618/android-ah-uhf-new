package com.beiming.uhf_test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import com.beiming.uhf_test.R
import com.tencent.mmkv.MMKV
import java.lang.Exception

/**
 * created by zhangshi on 2022/7/22.
 */
class DoorInfoInputLayout(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(
    context, attributeSet
) {

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
        zsGao.setText(MMKV.defaultMMKV().decodeString("zsGao", ""))

        zsKuan = findViewById(R.id.tv_zs_kuan)
        zsKuan.setText(MMKV.defaultMMKV().decodeString("zsKuan", ""))

        zxGao = findViewById(R.id.tv_zx_gao)
        zxGao.setText(MMKV.defaultMMKV().decodeString("zxGao", ""))

        zxKuan = findViewById(R.id.tv_zx_kuan)
        zxKuan.setText(MMKV.defaultMMKV().decodeString("zxKuan", ""))


        ysGao = findViewById(R.id.tv_ys_gao)
        ysGao.setText(MMKV.defaultMMKV().decodeString("ysGao", ""))

        ysKuan = findViewById(R.id.tv_ys_kuan)
        ysKuan.setText(MMKV.defaultMMKV().decodeString("ysKuan", ""))

        yxGao = findViewById(R.id.tv_yx_gao)
        yxGao.setText(MMKV.defaultMMKV().decodeString("yxGao", ""))

        yxKuan = findViewById(R.id.tv_yx_kuan)
        yxKuan.setText(MMKV.defaultMMKV().decodeString("yxKuan", ""))
    }

    fun getZsGao(): String {
        MMKV.defaultMMKV().encode("zsGao", zsGao.text.toString())
        return zsGao.text.toString()
    }

    fun getZsKuan(): String {
        MMKV.defaultMMKV().encode("zsKuan", zsKuan.text.toString())
        return zsKuan.text.toString()
    }

    fun getZxGao(): String {
        MMKV.defaultMMKV().encode("zxGao", zxGao.text.toString())
        return zxGao.text.toString()
    }

    fun getZxKuan(): String {
        MMKV.defaultMMKV().encode("zxKuan", zxKuan.text.toString())
        return zxKuan.text.toString()
    }

    fun getYsGao(): String {
        MMKV.defaultMMKV().encode("ysGao", ysGao.text.toString())
        return ysGao.text.toString()
    }

    fun getYsKuan(): String {
        MMKV.defaultMMKV().encode("ysKuan", ysKuan.text.toString())
        return ysKuan.text.toString()
    }

    fun getYxGao(): String {
        MMKV.defaultMMKV().encode("yxGao", yxGao.text.toString())
        return yxGao.text.toString()
    }

    fun getYxKuan(): String {
        MMKV.defaultMMKV().encode("yxKuan", yxKuan.text.toString())
        return yxKuan.text.toString()
    }

    /**
     * 带出的数据是否有编辑过
     */
    fun theSame(): Boolean {
        if (MMKV.defaultMMKV().decodeString("zsGao", "").equals(zsGao.text.toString())
            && MMKV.defaultMMKV().decodeString("zsKuan", "").equals(zsKuan.text.toString())
            && MMKV.defaultMMKV().decodeString("zxGao", "").equals(zxGao.text.toString())
            && MMKV.defaultMMKV().decodeString("zxKuan", "").equals(zxKuan.text.toString())
            && MMKV.defaultMMKV().decodeString("ysGao", "").equals(ysGao.text.toString())
            && MMKV.defaultMMKV().decodeString("ysKuan", "").equals(ysKuan.text.toString())
            && MMKV.defaultMMKV().decodeString("yxGao", "").equals(yxGao.text.toString())
            && MMKV.defaultMMKV().decodeString("yxKuan", "").equals(yxKuan.text.toString())) {
            return true
        }
        return false
    }
}