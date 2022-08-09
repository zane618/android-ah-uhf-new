package com.beiming.uhf_test.activity

import com.beiming.uhf_test.base.BaseActivity
import com.beiming.uhf_test.databinding.ActivityFenzhixBinding

/**
 * created by zhangshi on 2022/8/9.
 */
class FenzhixActivity : BaseActivity() {
    private lateinit var binding: ActivityFenzhixBinding

    override fun setContentView() {
        binding = ActivityFenzhixBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView() {
        binding.inTitle.tvTitleName.text = "关联分支箱"
    }

    override fun initListener() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }
}