package cn.segi.wanandroid.demo.ui.common.net.response

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import cn.segi.wanandroid.demo.application.FrameworkInitializer
import cn.segi.wanandroid.demo.base.network.response.BaseResponse
import cn.segi.wanandroid.demo.ui.common.Constant
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.IOException

class BaseObserver<T : BaseResponse<*>>(
    val liveData: MutableLiveData<T>
) : Observer<T> {

    override fun onComplete() {
        Log.d("Exception", "onComplete")
    }

    override fun onSubscribe(d: Disposable) {
        Log.d("Exception", "onSubscribe")
    }

    override fun onNext(response: T) {
        Log.d("RetrofitManager", "onNext")
        when (response.errorCode) {
            Constant.SUCCESS -> {
                if (response.data is List<*>) {
                    if ((response.data as List<*>).isEmpty()) {
//                        loadState.postValue(State(StateType.EMPTY))
                        return
                    }
                }
//                loadState.postValue(State(StateType.SUCCESS))
                liveData.postValue(response)
            }
            Constant.NOT_LOGIN -> {
//                UserInfo.instance.logoutSuccess()
//                loadState.postValue(State(StateType.ERROR, message = "请重新登录"))
            }
            else -> {
//                loadState.postValue(State(StateType.ERROR, message = response.errorMessage))
            }
        }
    }

    override fun onError(e: Throwable) {
        Log.d("Exception", e.toString())
        if (e is HttpException) {
            when (e.code()) {
                401, 403 -> {
                    Toast.makeText(FrameworkInitializer.getContext(), "鉴权失败", Toast.LENGTH_SHORT)
                        .show()
                }
                in 400..600 -> {
                    Toast.makeText(
                        FrameworkInitializer.getContext(),
                        "服务忙，请稍后再试",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        FrameworkInitializer.getContext(),
                        "网络请求失败，请稍后再试",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else if (e is IOException) {
            Toast.makeText(FrameworkInitializer.getContext(), "io异常", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(FrameworkInitializer.getContext(), "未知异常", Toast.LENGTH_SHORT).show()
        }

    }
}