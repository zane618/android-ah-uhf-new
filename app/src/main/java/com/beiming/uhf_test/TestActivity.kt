package com.beiming.uhf_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.beiming.uhf_test.utils.LogPrintUtil
import com.beiming.uhf_test.view.DefectInputLayout

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        val xx = findViewById<DefectInputLayout>(R.id.xx)


        val b1 = findViewById<Button>(R.id.b1)
        b1.setOnClickListener {
            LogPrintUtil.zhangshi("b1:" + xx.getDj())
        }
        val b2 = findViewById<Button>(R.id.b2)
        b2.setOnClickListener {

        LogPrintUtil.zhangshi("b2:" + xx.getKindsDetail())
        }
    }
}