package com.beiming.uhf_test.tools

import android.content.Context
import android.media.SoundPool

/**
 * created by zhangshi on 2022/8/9.
 */
class KtSingle private constructor(context: Context){


    companion object : SingletonHolder<KtSingle, Context>(::KtSingle) {

    }

    private val soundPool: SoundPool? = null


    init {

    }


}