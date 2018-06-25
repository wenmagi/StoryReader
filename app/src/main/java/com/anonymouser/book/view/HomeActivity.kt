package com.anonymouser.book.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import com.anonymouser.book.R
import com.anonymouser.book.bean.DownloadBookEvent
import com.anonymouser.book.event.CacheProgressEvent
import com.anonymouser.book.event.CheckUpdateEvent
import com.anonymouser.book.event.SaveIsShowAdInfo
import com.anonymouser.book.presenter.HomePresenter
import com.anonymouser.book.service.DownloadService
import com.anonymouser.book.utlis.AppUpdate
import com.anonymouser.book.widget.Display
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.include_bottom_navigation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by YandZD on 2017/7/13.
 */
class HomeActivity : StatusBarActivity(), NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        navigation.menu.getItem(position).isChecked = true
        invalidateStatusBar()
        navigation?.itemTextColor = createColorList(viewPager_th.currentItem)
        navigation?.itemIconTintList = createColorList(viewPager_th.currentItem)
    }

    private fun tabColor(position: Int): Int {
        when (position) {
            0 -> {
                return resources.getColor(R.color.pink_800)
            }
            1 -> {
                return resources.getColor(R.color.amber_700)
            }
            2 -> {
                return resources.getColor(R.color.blue_800)
            }
            3 -> {
                return resources.getColor(R.color.teal_800)
            }
        }
        return return resources.getColor(R.color.pink_800)
    }

    val HIDE_FIRST_LOGO = 0x0001
    var mPresenter = HomePresenter()
    var mDownloadService = DownloadService()
    var mCheckUpdateEvent: CheckUpdateEvent? = null


    var mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            if (msg?.what == HIDE_FIRST_LOGO) {
//                firstLogo.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_home)
        mHandler.sendEmptyMessageDelayed(HIDE_FIRST_LOGO, 2000)

        mPresenter.initJar()

        //删除临时数据
        mPresenter.removeTempDataBase()

        EventBus.getDefault().register(this)

        initComponent()
    }

    fun initComponent() {
        val creator = FragmentPagerItems.with(this)
        creator.add(resources.getString(R.string.book_case), BookcaseFragment::class.java)
        creator.add(resources.getString(R.string.category), CategoryFragment::class.java)
        creator.add("排行榜", HotListFragment::class.java)
        creator.add("设置", HotListFragment::class.java)
        val adapter = FragmentPagerItemAdapter(
                supportFragmentManager, creator.create())
        var params: ViewGroup.LayoutParams = viewPager_th.layoutParams
//        params.setMargins(0, Display.getActionBarHeightPixels(this) + Display.dpToPixel(this, 10F), 0, 0);
        viewPager_th.offscreenPageLimit = 4
        viewPager_th.adapter = adapter as PagerAdapter?
        viewPager_th.setOnPageChangeListener(this)
        navigation?.itemTextColor = createColorList(0)
        navigation?.itemIconTintList = createColorList(0)
        navigation.setOnNavigationItemSelectedListener { item ->

            navigation?.itemTextColor = createColorList(viewPager_th.currentItem)
            navigation?.itemIconTintList = createColorList(viewPager_th.currentItem)
//            navigation?.setBackgroundColor(tabColor(viewPager_th.currentItem))

            when (item.itemId) {
                R.id.navigation_movie -> {
                    if (viewPager_th.currentItem != 0) {
                        viewPager_th.currentItem = 0
                    }
                    true
                }
                R.id.navigation_music -> {
                    if (viewPager_th.currentItem != 1) {
                        viewPager_th.currentItem = 1
                    }
                    true
                }
                R.id.navigation_books -> {
                    if (viewPager_th.currentItem != 2) {
                        viewPager_th.currentItem = 2
                    }

                    true
                }
                R.id.navigation_settings -> {
                    if (viewPager_th.currentItem != 3) {
                        viewPager_th.currentItem = 3
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun createColorList(position: Int): ColorStateList {
        val colors = intArrayOf(resources.getColor(R.color.grey_400), tabColor(position))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(-android.R.attr.state_checked)
        states[1] = intArrayOf(android.R.attr.state_checked)
        return ColorStateList(states, colors)

    }

    override fun onResume() {
        super.onResume()
        mPresenter.notfiyBookCase()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun onSearch(view: View) {
        var intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun cacheEvent(event: CacheProgressEvent) {
        if (event.isFinish)
            tvCacheProgress.visibility = View.GONE
        else
            tvCacheProgress.visibility = View.VISIBLE
        tvCacheProgress.text = "缓存中：${event.msg}"
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onDownloadEvent(downloadBookEvent: DownloadBookEvent) {
        mDownloadService.onDownloadEvent(downloadBookEvent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCheckUpdateEvent(event: CheckUpdateEvent): Boolean {
        mCheckUpdateEvent = event
        var update = AppUpdate(this)
        return update.getVersionInfo(mCheckUpdateEvent?.mBean)
    }

    @Subscribe
    fun onSaveIsShowAd(isShowAdInfo: SaveIsShowAdInfo) {
        getSharedPreferences("info", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isShowAd", isShowAdInfo.isShowAd)
                .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /* 抽屉菜单功能实现 */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_check_app) {
            //版本升级
            if (mCheckUpdateEvent != null) {
                if (onCheckUpdateEvent(mCheckUpdateEvent!!)) {
                    Toast.makeText(this@HomeActivity, "已经是最新版了", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@HomeActivity, "已经是最新版了", Toast.LENGTH_LONG).show()
            }
        } else if (id == R.id.nav_about) {
            var intent = Intent(this@HomeActivity, AboutActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_feedback) {
            var intent = Intent(this@HomeActivity, FeedbackActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_teach_use) {
            var intent = Intent(this@HomeActivity, UseTeachActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_sharing) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享")
            intent.putExtra(Intent.EXTRA_TEXT, "http://yourbuffslonnol.com")
            intent.putExtra(Intent.EXTRA_TITLE, resources.getString(R.string.app_name))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent.createChooser(intent, "请选择"))
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun isSystemUiFullscreen(): Boolean {
        return true
    }

    override fun provideStatusBarColor(): Int {
        if (viewPager_th != null)
            return tabColor(viewPager_th.currentItem)
        return 0
    }

}