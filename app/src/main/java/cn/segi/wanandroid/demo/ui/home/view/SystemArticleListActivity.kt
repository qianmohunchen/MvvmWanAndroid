package cn.segi.wanandroid.demo.ui.home.view

import android.view.View
import cn.segi.wanandroid.R
import cn.segi.wanandroid.demo.base.utils.ColorUtil
import cn.segi.wanandroid.demo.base.view.BaseActivity
import kotlinx.android.synthetic.main.custom_bar.view.*

class SystemArticleListActivity : BaseActivity() {
    private var mCurrentPageNum: Int = 0

    private lateinit var headView: View

    private val mTitle: String? by lazy { intent?.getStringExtra("title") }
    private val mCid: Int? by lazy { intent?.getIntExtra("id", 0) }
    override fun getLayoutId(): Int {
        return R.layout.fragment_article_list
    }


    override fun initView() {
        initHeaderView()
    }

    private fun initHeaderView() {
        headView = View.inflate(this, R.layout.custom_bar, null)
        headView.detail_title.text = mTitle
        headView.detail_back.visibility = View.VISIBLE
        headView.detail_back.setOnClickListener { finish() }
        headView.detail_search.visibility = View.GONE
        headView.setBackgroundColor(ColorUtil.getColor(this))
//        mAdapter.addHeaderView(headView)
    }

    override fun initData() {
        mCurrentPageNum = 0
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}