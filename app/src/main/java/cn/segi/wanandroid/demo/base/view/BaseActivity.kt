package cn.segi.wanandroid.demo.base.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.segi.wanandroid.demo.base.view.dialog.LoadingDialog

abstract class BaseActivity : AppCompatActivity() {

    private var cancelable: Boolean = true

    private lateinit var loadingTxt: CharSequence

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
    }


    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    public fun createLoading(context: Context, cancel: Boolean, loadingTxt: CharSequence) {
        loadingDialog = LoadingDialog(
            context,
            cancel,
            loadingTxt
        )
    }

    public fun getloading(): LoadingDialog {
        return loadingDialog
    }


    protected fun showLoading() {
        loadingDialog.show()
    }

    protected fun dissmissLoading() {
        loadingDialog.dismiss()
    }


}