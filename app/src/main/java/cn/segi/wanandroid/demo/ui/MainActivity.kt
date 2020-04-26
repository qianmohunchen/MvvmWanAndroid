package cn.segi.wanandroid.demo.ui

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import cn.segi.wanandroid.R
import cn.segi.wanandroid.demo.base.view.BaseActivity
import cn.segi.wanandroid.demo.ui.common.Constant
import cn.segi.wanandroid.demo.ui.home.view.HomeFragment
import cn.segi.wanandroid.demo.ui.system.view.SystemFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseActivity() {

    /**
     * 记录最后的fragment的位置
     */
    private var mLastIndex: Int = -1
    /**
     *
     */
    private val mFragmentSparseArray = SparseArray<Fragment>()

    /**
     * 当前显示的 fragment
     */
    private var mCurrentFragment: Fragment? = null
    /**
     *
     */
    private var mLastFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        // 判断当前是recreate还是新启动
        if (savedInstanceState == null) {
            switchFragment(Constant.HOME)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // recreate时保存当前页面位置
        outState.putInt("index", mLastIndex)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        initToolBar()
        initBottomNavigation()
    }


    override fun initData() {
    }

    private fun initToolBar() {
        // 设置标题
        setToolBarTitle(toolbar, getString(R.string.navigation_home))
        //设置导航图标、按钮有旋转特效
        val toggle = ActionBarDrawerToggle(
            this, drawer_main, toolbar,
            R.string.app_name,
            R.string.app_name
        )
        drawer_main.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    fab_add.visibility = View.VISIBLE
                    setToolBarTitle(toolbar, getString(R.string.navigation_home))
                    switchFragment(Constant.HOME)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_system -> {
                    fab_add.visibility = View.VISIBLE
                    setToolBarTitle(toolbar, getString(R.string.navigation_system))
                    switchFragment(Constant.SYSTEM)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun switchFragment(index: Int) {
        val fragmentManager = supportFragmentManager
        val transaction =
            fragmentManager.beginTransaction()
        // 将当前显示的fragment和上一个需要隐藏的fragment分别加上tag, 并获取出来
        // 给fragment添加tag,这样可以通过findFragmentByTag找到存在的fragment，不会出现重复添加
        mCurrentFragment = fragmentManager.findFragmentByTag(index.toString())
        mLastFragment = fragmentManager.findFragmentByTag(mLastIndex.toString())
        // 如果位置不同
        if (index != mLastIndex) {
            if (mLastFragment != null) {
                transaction.hide(mLastFragment!!)
            }
            if (mCurrentFragment == null) {
                mCurrentFragment = getFragment(index)
                transaction.add(R.id.content, mCurrentFragment!!, index.toString())
            } else {
                transaction.show(mCurrentFragment!!)
            }
        }

        // 如果位置相同或者新启动的应用
        if (index == mLastIndex) {
            if (mCurrentFragment == null) {
                mCurrentFragment = getFragment(index)
                transaction.add(R.id.content, mCurrentFragment!!, index.toString())
            }
        }
        transaction.commit()
        mLastIndex = index
    }

    private fun getFragment(index: Int): Fragment {
        var fragment: Fragment? = mFragmentSparseArray.get(index)
        if (fragment == null) {
            when (index) {
                Constant.HOME -> fragment = HomeFragment.getInstance()
                Constant.SYSTEM -> fragment = SystemFragment.getInstance()
                Constant.NAVIGATION -> fragment = HomeFragment.getInstance()
                Constant.WECHAT -> fragment = HomeFragment.getInstance()
                Constant.PROJECT -> fragment = HomeFragment.getInstance()
            }
            mFragmentSparseArray.put(index, fragment)
        }
        return fragment!!
    }

    fun setToolBarTitle(toolbar: Toolbar, title: String) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }
}
