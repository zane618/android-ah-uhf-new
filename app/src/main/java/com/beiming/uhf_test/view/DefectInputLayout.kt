package com.beiming.uhf_test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.beiming.uhf_test.R
import java.lang.StringBuilder
import java.util.*

/**
 * created by zhangshi on 2022/8/8.
 */
class DefectInputLayout(context: Context, attributeSet: AttributeSet? = null) :
    LinearLayout(context, attributeSet) {

    private lateinit var radioGroup_qx: RadioGroup
    private lateinit var qx_dj_0: RadioButton
    private lateinit var qx_dj_1: RadioButton
    private lateinit var qx_dj_2: RadioButton
    private lateinit var qx_dj_3: RadioButton
    private lateinit var tv_detail: TextView
    private lateinit var ll_checkbox: LinearLayout

    private val QX_ARR_1 = arrayOf("(1)计量箱具、印缺头;","(2)计量箱内存在杂物;","(3)计量箱视窗发黄、不清晰;","(4) 计量箱视窗破损、缺失;","(5)计量箱锁具损杯;","(6)计查相外观存在污垢。")
    private val QX_ARR_2 = arrayOf("(1)计量箱无法加锁;","(2)计量箱变形;","(3)计量箱锁具损坏;","(4)计量箱轻微破损或者局部锈蚀，可修复后，正常使用。")
    private val QX_ARR_3 = arrayOf("(1)计量装置饺链断裂;","(2)计量安装不牢固;","(3)计量箱门无法关闭;","(4)金属表箱严重锈性，无法修复;","(5)金属计量箱未地;","(6)计量箱门被焊技，无法打开;"
        ,"(7)计置门、底缺失;","(8)计量箱进水","(9)塑料表箱严重老化，部分位置已开始资他风化;","(10)计量箱严重破损，无法修等或无修复价信")



    init {
            LayoutInflater.from(context).inflate(R.layout.layout_defect_input, this)
            radioGroup_qx = findViewById(R.id.radioGroup_qx)
            qx_dj_0 = findViewById(R.id.qx_dj_0)
            qx_dj_1 = findViewById(R.id.qx_dj_1)
            qx_dj_2 = findViewById(R.id.qx_dj_2)
            qx_dj_3 = findViewById(R.id.qx_dj_3)
            tv_detail = findViewById(R.id.tv_detail)
            ll_checkbox = findViewById(R.id.ll_checkbox)

            radioGroup_qx.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
                override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                    var s = ""
                    var TMP_ARR = null
                    when (checkedId) {
                        R.id.qx_dj_1 -> {
                            s = resources.getString(R.string.qx_first)

                            createCb(QX_ARR_1)
                        }


                        R.id.qx_dj_2 -> {
                            s = resources.getString(R.string.qx_second)
                            createCb(QX_ARR_2)
                        }

                        R.id.qx_dj_3 -> {
                            s = resources.getString(R.string.qx_third)
                            createCb(QX_ARR_3)
                        }

                        else -> {
                            //                    tv_detail.text = s
                            ll_checkbox.removeAllViews()
                        }
                    }

                }
            })
        }


    fun createCb(array: Array<String>) {
        ll_checkbox.removeAllViews()
        for (i in 0..array.size-1) {
            val cb = CheckBox(context)
            val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            cb.text = array[i]
            ll_checkbox.addView(cb, layoutParams)
        }
    }

    /**
     * 获取缺陷等级 无 I级 II级 III级
     */
    fun getDj() :String{
        var dj = "无"
        for (i in 0..radioGroup_qx.childCount-1) {
            if ((radioGroup_qx.getChildAt(i) as RadioButton).isChecked) {
                dj = (radioGroup_qx.getChildAt(i) as RadioButton).text as String
                break
            }
        }
        return dj
    }

    /**
     * 逗号分割，存储缺陷具体详情
     */
    fun getKindsDetail(): String {
        val count = ll_checkbox.childCount
        val sb = StringBuilder()
        if (count > 0) {
            for (i in 0..count-1) {
                val item = ll_checkbox.getChildAt(i) as CheckBox
                if(item.isChecked) {
                    sb.append("${item.text}\n")
                }
            }
        }
        return sb.toString()
    }


}