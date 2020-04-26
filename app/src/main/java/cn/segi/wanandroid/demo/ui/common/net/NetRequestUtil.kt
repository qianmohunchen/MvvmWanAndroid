package cn.segi.wanandroid.demo.ui.common.net

import cn.segi.wanandroid.demo.base.network.RetrofitManager
import cn.segi.wanandroid.demo.ui.common.Constant

class NetRequestUtil {

    private var apiService: ApiService? = null

    companion object {
        val instance: NetRequestUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetRequestUtil()
        }
    }

    fun <T> getApi(clz: Class<T>): T {
        return RetrofitManager.Builder()
            .setBaseUrl(Constant.BASE_URL)
            .build()
            .create(clz)
    }

    fun getApiService(): ApiService {
        return apiService ?: RetrofitManager.Builder()
            .setBaseUrl(Constant.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }



}