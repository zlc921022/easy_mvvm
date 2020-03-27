package com.xiaochen.easy_mvvm

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xiaochen.base.base.AbsBaseActivity
import com.xiaochen.base.utils.LogUtil
import com.xiaochen.easy_mvvm.viewmodel.TestViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbsBaseActivity<TestViewModel>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun createViewModel(): TestViewModel {
        return ViewModelProviders.of(this).get(TestViewModel::class.java)
    }

    override fun initData() {
        super.initData()
        mViewModel?.let {
            it.mHomeLiveData.observe(this, Observer {
                LogUtil.e("MainActivity", "调用成功")
            })
        }
    }

    override fun initListener() {
        tv.setOnClickListener {
            mViewModel?.getArticleInfo(1)
        }
    }
}
