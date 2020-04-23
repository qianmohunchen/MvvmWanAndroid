package cn.segi.wanandroid.demo.base.network

import android.util.Log
import cn.segi.wanandroid.demo.base.network.Interceptor.ExceptionInterceptor
import cn.segi.wanandroid.demo.base.network.convert.ConvertFactory
import cn.segi.wanandroid.demo.base.network.cookie.DefaultCookiesManager
import cn.segi.wanandroid.demo.ui.common.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager {

    private val TAG: String = "RetrofitManager"
    private var retrofit: Retrofit? = null

    private var baseUrl: String? = null

    //    companion object {
//        val instance: RetrofitManager by lazy {
//            Log.d("RetrofitManager", "RetrofitManager()")
//            RetrofitManager()
//        }
//    }
    init {
//        这里比构造函数先执行
        Log.d(TAG, "init")
    }

    constructor(baseUrl: String) {
        this.baseUrl = baseUrl
        Log.d(TAG, "RetrofitManager()")
        initRetrofit()

    }

    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(this.baseUrl)
            .addConverterFactory(ConvertFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(initOkHttpClient())
            .build()
    }

    private fun initOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .cookieJar(DefaultCookiesManager())
            .addInterceptor(ExceptionInterceptor())
            .build()
//        return OkHttpClient.Builder()
//            .addInterceptor(initCookieIntercept())
//            .addInterceptor(initLoginIntercept())
//            .addInterceptor(initCommonInterceptor())
//            .build()
    }

    /***
     *
     * @param clz api的接口类型
     * @return T 返回类操作里面定义的方法
     */
    fun <T> create(clz: Class<T>): T {
        return retrofit!!.create(clz)
    }


    public class Builder {
        private var baseUrl: String = Constant.BASE_URL

        fun setBaseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun build(): RetrofitManager {

            return RetrofitManager(this.baseUrl)
        }

    }
}