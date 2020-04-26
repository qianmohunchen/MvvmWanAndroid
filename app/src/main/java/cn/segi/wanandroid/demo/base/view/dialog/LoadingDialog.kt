package cn.segi.wanandroid.demo.base.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import cn.segi.wanandroid.R
import kotlinx.android.synthetic.main.v_alert_progress_dialog.*

class LoadingDialog : Dialog, LifecycleObserver {

    /**
     * 提示内容缓存
     */
    private var mMessageStr: CharSequence? = null

    constructor(context: Context, cancelable: Boolean, messageStr: CharSequence) : this(
        context,
        0
    ) {
        this.mMessageStr = messageStr
        setCancelable(cancelable)
    }

    constructor(context: Context, themeResId: Int) : super(context, R.style.CustomProgressDialog) {
//        window.griv(Gravity.CENTER)
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v_alert_progress_dialog)
        progress_tv.text = mMessageStr
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d("life", "myobserver onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory() {
        Log.d("life", "myobserver onDestory")
    }


}