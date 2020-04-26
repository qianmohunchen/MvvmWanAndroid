package cn.segi.wanandroid.demo.ui.system.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.segi.wanandroid.demo.base.network.response.BaseResponse
import cn.segi.wanandroid.demo.ui.common.net.ApiService
import cn.segi.wanandroid.demo.ui.common.net.NetRequestUtil
import cn.segi.wanandroid.demo.ui.common.net.response.BaseObserver
import com.wjx.android.wanandroidmvvm.ui.system.data.SystemArticleResponse
import com.wjx.android.wanandroidmvvm.ui.system.data.SystemTabNameResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SystemViewModel : ViewModel() {

    private val systemTabLiveData: MutableLiveData<BaseResponse<List<SystemTabNameResponse>>> by lazy {
        MutableLiveData<BaseResponse<List<SystemTabNameResponse>>>()
    }

    private val systemArticleLiveData: MutableLiveData<BaseResponse<SystemArticleResponse>> by lazy {
        MutableLiveData<BaseResponse<SystemArticleResponse>>()
    }

    private val apiService: ApiService by lazy {

        NetRequestUtil.instance.getApiService()
    }


    fun loadSystemTab() {
        apiService.loadSystemTab()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(BaseObserver(systemTabLiveData))
    }

    fun getSystemTab() = systemTabLiveData

    public fun loadSystemArticle(page: Int, cid: Int) {
        apiService.loadSystemArticle(page, cid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(BaseObserver(systemArticleLiveData))
    }

    public fun getSystemArticle() = systemArticleLiveData

}