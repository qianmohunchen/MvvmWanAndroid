package cn.segi.wanandroid.demo.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.segi.wanandroid.demo.base.network.response.BaseResponse
import cn.segi.wanandroid.demo.ui.common.net.ApiService
import cn.segi.wanandroid.demo.ui.common.net.NetRequestUtil
import cn.segi.wanandroid.demo.ui.common.net.response.BaseObserver
import com.wjx.android.wanandroidmvvm.ui.home.data.BannerResponse
import com.wjx.android.wanandroidmvvm.ui.home.data.HomeArticleResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/26
 * Time: 10:49
 */
class HomeViewModel : ViewModel() {

    //    val mHomeArticleData: MutableLiveData<BaseData<HomeArticleResponse>> = MutableLiveData()
    private val homeArticleData: MutableLiveData<BaseResponse<HomeArticleResponse>> by lazy {
        MutableLiveData<BaseResponse<HomeArticleResponse>>()
    }
    private val bannerResonpseDatas: MutableLiveData<BaseResponse<List<BannerResponse>>> by lazy {
        MutableLiveData<BaseResponse<List<BannerResponse>>>()
    }

    private val apiService: ApiService by lazy {
        NetRequestUtil.instance.getApiService()
    }

    fun getHomeData(): MutableLiveData<BaseResponse<HomeArticleResponse>> {
        return homeArticleData
    }

    fun getBannerDatas(): MutableLiveData<BaseResponse<List<BannerResponse>>> {
        return bannerResonpseDatas
    }

    fun loadHomeArticleData(pageNum: Int) {
        apiService.loadHomeArticle(pageNum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(BaseObserver(homeArticleData))
    }

    fun loadBannerDatas() {
        apiService.loadBanner()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(BaseObserver(bannerResonpseDatas))
    }
}