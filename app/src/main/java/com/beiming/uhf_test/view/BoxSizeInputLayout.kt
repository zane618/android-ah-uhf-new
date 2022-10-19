package com.beiming.uhf_test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.beiming.uhf_test.R
import com.beiming.uhf_test.data.ConstData
import com.beiming.uhf_test.utils.LogPrintUtil
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * created by zhangshi on 2022/8/18.
 */
class BoxSizeInputLayout(context: Context, attributeSet: AttributeSet? = null) :
    LinearLayout(context, attributeSet) {

    private var et_gao: EditText
    private var et_kuan: EditText
    private var et_shen: EditText
    private var spinnerBoxKind: Spinner
    private var spinnerBiaowei: Spinner
    private val biaoweiData: MutableList<String>


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_box_size_input, this)
        et_gao = findViewById(R.id.et_xiang_gao)
        et_gao.setText(MMKV.defaultMMKV().decodeString("et_gao", ""))
        findViewById<View>(R.id.iv_xiang_gao).setOnClickListener {
            et_gao.setText("")
        }

        et_kuan = findViewById(R.id.et_xiang_kuan)
        et_kuan.setText(MMKV.defaultMMKV().decodeString("et_kuan", ""))
        findViewById<View>(R.id.iv_xiang_kuan).setOnClickListener {
            et_kuan.setText("")
        }

        et_shen = findViewById(R.id.et_xiang_shen)
        et_shen.setText(MMKV.defaultMMKV().decodeString("et_shen", ""))
        findViewById<View>(R.id.iv_xiang_shen).setOnClickListener {
            et_shen.setText("")
        }

        spinnerBoxKind = findViewById(R.id.spinnerBoxKind)
        val a = Arrays.asList(*ConstData.BOX_KIND)
        val kindAdapter: ArrayAdapter<String> = ArrayAdapter(
            context, R.layout.spinner_box_kind,
            a
        )
        spinnerBoxKind.setDropDownVerticalOffset(50)
        spinnerBoxKind.setDropDownHorizontalOffset(10)
        spinnerBoxKind.adapter = kindAdapter

        //表位 adapter
        spinnerBiaowei = findViewById(R.id.spinnerBiaowei)
//        biaoweiData =
        biaoweiData = mutableListOf()
        biaoweiData.addAll(Arrays.asList(*ConstData.DAN_BIAO_WEI))
        val biaoweiAdapter: ArrayAdapter<String> = ArrayAdapter(
            context, R.layout.spinner_box_kind,
            biaoweiData
        )
        spinnerBiaowei.setDropDownVerticalOffset(50)
        spinnerBiaowei.setDropDownHorizontalOffset(10)
        spinnerBiaowei.adapter = biaoweiAdapter


        initListener()


    }

    private fun initListener() {
        spinnerBoxKind.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    refreshBiaoweiSpinner("单相".equals(spinnerBoxKind.adapter.getItem(position)))
                    LogPrintUtil.zhangshi("上spinner:" + spinnerBoxKind.adapter.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        spinnerBiaowei.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    LogPrintUtil.zhangshi("xia   xia  spinner:" + spinnerBiaowei.adapter.getItem(position))

                    if ("单相".equals(spinnerBoxKind.selectedItem.toString())) {
                        val res = ConstData.DAN_CHI_CUN.get(spinnerBiaowei.selectedItem.toString())
                        res?.let {
                            val arr = res.split(",")
                            val s = arr[0]
                            val chicunArr = s.split("×")
                            et_gao.setText(chicunArr[0])
                            et_kuan.setText(chicunArr[1])
                            et_shen.setText(chicunArr[2])
                        }
                    } else{
                        val res = ConstData.SAN_CHI_CUN.get(spinnerBiaowei.selectedItem.toString())
                        res?.let {
                            val arr = res.split(",")
                            val s = arr[0]
                            val chicunArr = s.split("×")
                            et_gao.setText(chicunArr[0])
                            et_kuan.setText(chicunArr[1])
                            et_shen.setText(chicunArr[2])
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

    }

    fun setEtData() {

    }

    fun refreshBiaoweiSpinner(single: Boolean) {
        if (single) {
            biaoweiData.clear()
            biaoweiData.addAll(Arrays.asList(*ConstData.DAN_BIAO_WEI))
        } else {
            biaoweiData.clear()
            biaoweiData.addAll(Arrays.asList(*ConstData.SAN_BIAO_WEI))
        }
        (spinnerBiaowei.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        spinnerBiaowei.setSelection(0)
        et_gao.setText("")
        et_kuan.setText("")
        et_shen.setText("")
    }


    fun getGao(): String {
        MMKV.defaultMMKV().encode("et_gao", et_gao.text.toString())
        return et_gao.text.toString()
    }

    fun getKuan(): String {
        MMKV.defaultMMKV().encode("et_kuan", et_kuan.text.toString())
        return et_kuan.text.toString()
    }

    fun getShen(): String {
        MMKV.defaultMMKV().encode("et_shen", et_shen.text.toString())
        return et_shen.text.toString()
    }

    fun theSame(): Boolean {
        if (MMKV.defaultMMKV().decodeString("et_gao", "").equals(et_gao.text.toString())
            && MMKV.defaultMMKV().decodeString("et_kuan", "").equals(et_kuan.text.toString())
            && MMKV.defaultMMKV().decodeString("et_shen", "").equals(et_shen.text.toString())
        ) {
            return true
        }
        return false
    }

}