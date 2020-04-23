package cn.segi.wanandroid.demo.base.network.Interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ExceptionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.body() != null) {
            if (401 == response.code() || 403 == response.code()) {
                // this.throwable = new HttpAuthorizationFailException("授权失败");
            } else if (400 <= response.code() && response.code() < 600) {
                // this.throwable = new HttpBusyServiceException("服务忙，请稍后再试");
            } else {
                //  this.throwable = new HttpRequestFailException("网络请求失败，请稍后再试");
            }
        } else {
            // this.throwable = new HttpNoResponseException("服务器无数据返回");
        }
        return response
    }
}