package cn.segi.wanandroid.demo.ui.common.net

import cn.segi.wanandroid.demo.base.network.response.BaseResponse
import com.wjx.android.wanandroidmvvm.ui.home.data.BannerResponse
import com.wjx.android.wanandroidmvvm.ui.home.data.HomeArticleResponse
import com.wjx.android.wanandroidmvvm.ui.system.data.SystemArticleResponse
import com.wjx.android.wanandroidmvvm.ui.system.data.SystemTabNameResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/article/list/{pageNum}/json")
    fun loadHomeArticle(@Path("pageNum") pageNum: Int): Observable<BaseResponse<HomeArticleResponse>>

    @GET("/banner/json")
    fun loadBanner(): Observable<BaseResponse<List<BannerResponse>>>


    @GET("/tree/json")
    fun loadSystemTab(): Observable<BaseResponse<List<SystemTabNameResponse>>>

    @GET("/article/list/{pageNum}/json")
    fun loadSystemArticle(
        @Path("pageNum") pageNum: Int,
        @Query("cid") id: Int?
    ): Observable<BaseResponse<SystemArticleResponse>>

}