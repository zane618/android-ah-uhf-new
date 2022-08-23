package com.beiming.uhf_test

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.beiming.uhf_test.databinding.ActivityTestBinding
import com.beiming.uhf_test.utils.LogPrintUtil
import com.beiming.uhf_test.utils.TimeUtils
import java.util.*


class TestActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.b1111.setOnClickListener {
            view ->
            var calendar = Calendar.getInstance()
            var dialog: DatePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    val desc: String = java.lang.String.format(
                        "您选择的日期是：%s年%s月%s日",
                        year,
                        month + 1,
                        dayOfMonth
                    )

                    val s = TimeUtils.formatDate(year, month + 1, dayOfMonth)
                    val ts = TimeUtils.toTs(s)

                    LogPrintUtil.zhangshi("ts:" + ts)

                    Toast.makeText(this@TestActivity, s, Toast.LENGTH_SHORT).show()
                }

            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            dialog.show()

            LogPrintUtil.zhangshi("1111")

        }
        binding.b2.setOnClickListener {
            LogPrintUtil.zhangshi("2222")
        }
    }


}