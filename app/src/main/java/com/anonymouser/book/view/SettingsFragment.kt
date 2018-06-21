package com.anonymouser.book.view

import android.os.Bundle
import butterknife.ButterKnife
import com.anonymouser.book.R
import com.anonymouser.book.view.lazyFragment.LazyFragment


/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 06-20-2018
 */
class SettingsFragment : LazyFragment() {
    override fun onCreateViewLazy(savedInstanceState: Bundle?) {
        super.onCreateViewLazy(savedInstanceState)
        setContentView(R.layout.fragment_settings)
        ButterKnife.bind(this, contentView)
    }

    override fun onDestroyViewLazy() {
        super.onDestroyViewLazy()
    }
}