package cn.segi.wanandroid.demo.ui.home.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.segi.wanandroid.R
import cn.segi.wanandroid.demo.base.network.response.BaseResponse
import cn.segi.wanandroid.demo.base.selfview.SpeedLayoutManager
import cn.segi.wanandroid.demo.base.utils.ColorUtil
import cn.segi.wanandroid.demo.base.view.BaseFragment
import cn.segi.wanandroid.demo.ui.ArticleDetailActivity
import cn.segi.wanandroid.demo.ui.common.glide.GlideImageLoader
import cn.segi.wanandroid.demo.ui.home.adapter.ArticleAdapter
import cn.segi.wanandroid.demo.ui.home.viewmodel.HomeViewModel
import com.wjx.android.wanandroidmvvm.ui.home.data.BannerResponse
import com.wjx.android.wanandroidmvvm.ui.home.data.HomeArticleResponse
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_article_list.*
import kotlinx.android.synthetic.main.home_banner.view.*
import java.util.*

class HomeFragment : BaseFragment() {

    private lateinit var homeAdapter: ArticleAdapter

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mBanner: Banner

    private val urls by lazy {
        arrayListOf<String>()
    }

    private val titles by lazy {
        arrayListOf<String>()
    }
    /**
     * 当前页面
     */
    private var mCurrentPage: Int = 0

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        createLoading(context, true, "稍等一会")
//    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }


    override fun initView() {
        // 设置下拉刷新的loading颜色
        mSrlRefresh.setProgressBackgroundColorSchemeColor(ColorUtil.getColor(activity!!))
        mSrlRefresh.setColorSchemeColors(Color.WHITE)
        mSrlRefresh.setOnRefreshListener { onRefreshData() }

        mRvArticle?.layoutManager = SpeedLayoutManager(context, 10f)
        homeAdapter = ArticleAdapter(R.layout.article_item, null)
        mRvArticle.adapter = homeAdapter

        homeAdapter.setEnableLoadMore(true)
        homeAdapter.setOnLoadMoreListener({ onLoadMoreData() }, mRvArticle)
        //设置头部
        val headView = View.inflate(activity, R.layout.home_banner, null)
        mBanner = headView.mBanner
        mBanner.setOnBannerListener { position ->
            val intent: Intent = Intent(activity, ArticleDetailActivity::class.java)
            intent.putExtra("url", urls[position])
            intent.putExtra("title", titles[position])
            startActivity(intent)
        }
        mBanner.setImageLoader(GlideImageLoader())
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        mBanner.setDelayTime(5000)
        mBanner.setBannerAnimation(Transformer.DepthPage)
        homeAdapter.addHeaderView(headView)

        homeAdapter.setOnItemClickListener { adapter, view, position ->
            val article = homeAdapter.getItem(position)
            article?.let {
                val intent: Intent = Intent(activity, ArticleDetailActivity::class.java)
                intent.putExtra("url", it.link)
                intent.putExtra("title", it.title)
                startActivity(intent)
            }
        }
        createLoading(activity!!, true, "稍等一会")
    }


    override fun initData() {
        showLoading()
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.loadHomeArticleData(mCurrentPage)
        homeViewModel.getHomeData().observe(
            this,
            Observer<BaseResponse<HomeArticleResponse>> { homeArticleResponse ->
                dissmissLoading()
                homeAdapter.setNewData(homeArticleResponse.data.datas)
                homeAdapter.notifyDataSetChanged()
                mSrlRefresh.setRefreshing(false)
                homeAdapter.loadMoreComplete()
            })

        homeViewModel.loadBannerDatas()
        homeViewModel.getBannerDatas()
            .observe(this, Observer<BaseResponse<List<BannerResponse>>> { bannerResponses ->
                dissmissLoading()
                setBannerData(bannerResponses.data)
            })
    }

    fun onRefreshData() {
        mCurrentPage = 0
        homeViewModel.loadHomeArticleData(mCurrentPage)
    }

    fun onLoadMoreData() {
        Log.d("home","onLoadMoreData")
        homeViewModel.loadHomeArticleData(++mCurrentPage)
    }

    private fun setBannerData(bannerList: List<BannerResponse>) {
        val images = ArrayList<String>()
        urls.clear()
        titles.clear()
        for (bannerItem in bannerList) {
            images.add(bannerItem.imagePath)
            titles.add(bannerItem.title)
            urls.add(bannerItem.url)
        }
        mBanner.setImages(images)
        mBanner.setBannerTitles(titles)
        mBanner.start()
    }

}