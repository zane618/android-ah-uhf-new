package com.beiming.uhf_test.data

import com.beiming.uhf_test.library.bean.LibSpnnerBean

/**
 * created by zhangshi on 2022/8/18.
 */
object ConstData {

    val NUM_0X = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    val BOX_KIND = arrayOf("单相", "三相")

    val DAN_BIAO_WEI = arrayOf(
        "未选择",
        "2表位",
        "3表位",
        "4表位单排1",
        "4表位单排2",
        "4表位单2排",
        "6表位2排",
        "8表位2排",
        "10表位2排",
        "6表位3排",
        "9表位3排",
        "12表位3排",
        "15表位3排"
    )

    val DAN_CHI_CUN = mapOf(
        "2表位" to "648×450×150",
        "3表位" to "790×450×150",
        "4表位单排1" to "1026×450×150",
        "4表位单排2" to "1026×450×150",
        "4表位单2排" to "728×800×180",
        "6表位2排" to "870×800×180",
        "8表位2排" to "1106×800×180",
        "10表位2排" to "1248×800×180",
        "6表位3排" to "758×1000×180",
        "9表位3排" to "910×1000×180,890×1000×180",
        "12表位3排" to "1166×1000×180,1146×1000×180",
        "15表位3排" to "1318×1000×180,1288×1000×180"
    )

    val SAN_CHI_CUN = mapOf(
        "2表位单排" to "860×550×150",
        "3表位单排" to "1160×550×150",
        "2表位2排" to "780×1000×180",
        "4表位2排" to "890×1000×180",
        "6表位2排" to "1220×1000×180",
        "1表位" to "700×1000×180",
        "2表位" to "1000×1000×180",
        "互感器接入式1表位" to "700×1000×180",
        "互感器接入式2表位" to "910×1000×180"
    )


    val SAN_BIAO_WEI = arrayOf(
        "未选择",
        "2表位单排",
        "3表位单排",
        "2表位2排",
        "4表位2排",
        "6表位2排",
        "1表位",
        "2表位",
        "互感器接入式1表位",
        "互感器接入式2表位"
    )


    /**
     * 资产类型
     */
//    val LIB_ASSETS = mapOf(
//        "电能表" to "01",
//        "互感器" to "02",
//        "计量箱" to "05",
//        "电能信息采集终端" to "09",
//        "测试装置" to "13",
//        "其他仪器仪表" to "14",
//        "周转箱" to "19",
//        "现场手持终端" to "20",
//        "电能表外置断路器" to "27",
//        "回路状态巡检仪" to "50",
//        "反窃电装置" to "51",
//        "流水线设备" to "52",
//        "仓储设备" to "53",
//        "通信模块" to "54"
//    )

    val LIB_ASSETS = mutableListOf(
        LibSpnnerBean("全部资产", "00"),
        LibSpnnerBean("电能表", "01"),
        LibSpnnerBean("互感器", "02"),
        LibSpnnerBean("计量箱", "05"),
        LibSpnnerBean("电能信息采集终端", "09"),
        LibSpnnerBean("测试装置", "13"),
        LibSpnnerBean("其他仪器仪表" , "14"),
        LibSpnnerBean("周转箱", "19"),
        LibSpnnerBean("现场手持终端", "20"),
        LibSpnnerBean("电能表外置断路器", "27"),
        LibSpnnerBean("回路状态巡检仪", "50"),
        LibSpnnerBean("反窃电装置", "51"),
        LibSpnnerBean("流水线设备", "52"),
        LibSpnnerBean("仓储设备", "53"),
        LibSpnnerBean("通信模块", "54")
    )


}