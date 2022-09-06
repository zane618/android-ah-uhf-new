package com.beiming.uhf_test.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.beiming.uhf_test.R
import com.beiming.uhf_test.data.ConstData
import com.beiming.uhf_test.utils.LogPrintUtil
import java.util.*

/**
 * created by zhangshi on 2022/8/18.
 */
class BoxRowColLayout(context: Context, attributeSet: AttributeSet? = null) :
    LinearLayout(context, attributeSet) {

    private var spinnerRow: Spinner
    private var spinnerCol: Spinner
    private val coLData: MutableList<String>


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_box_row_col_input, this)

        spinnerRow = findViewById(R.id.spinnerRow)
        val a = Arrays.asList(*ConstData.NUM_0X)
        val kindAdapter: ArrayAdapter<String> = ArrayAdapter(
            context, R.layout.spinner_box_kind,
            a
        )
        spinnerRow.setDropDownVerticalOffset(50)
        spinnerRow.setDropDownHorizontalOffset(10)
        spinnerRow.adapter = kindAdapter


        spinnerCol = findViewById(R.id.spinnerCol)
        coLData = mutableListOf()
        coLData.addAll(Arrays.asList(*ConstData.NUM_0X))
        val biaoweiAdapter: ArrayAdapter<String> = ArrayAdapter(
            context, R.layout.spinner_box_kind,
            coLData
        )
        spinnerCol.setDropDownVerticalOffset(50)
        spinnerCol.setDropDownHorizontalOffset(10)
        spinnerCol.adapter = biaoweiAdapter


        initListener()


    }

    private fun initListener() {
        spinnerRow.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    LogPrintUtil.zhangshi("getRow:" + spinnerRow.adapter.getItem(position))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        spinnerCol.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    LogPrintUtil.zhangshi("getCol:" + spinnerCol.adapter.getItem(position))

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
    }

    fun getRow(): String {
        return spinnerRow.selectedItem.toString()
    }

    fun getCol(): String {
        return spinnerCol.selectedItem.toString()
    }


}