package com.beiming.uhf_test.tools.rfid

/**
 * created by zhangshi on 2022/8/10.
 */
interface IRfidListener {

    fun onRfidResult(s: String?)

    fun onStop()

}