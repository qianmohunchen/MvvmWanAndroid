package cn.segi.wanandroid.demo.base.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.segi.wanandroid.demo.base.view.dialog.LoadingDialog

abstract class BaseFragment : Fragment() {


    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), null)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initData()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createLoading(context, true, "fragment加载数据中")
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()


    protected fun createLoading(context: Context, cancel: Boolean, loadingTxt: CharSequence) {
        if (context is BaseActivity) {
            context.createLoading(context, cancel, loadingTxt)
            loadingDialog = context.getloading()
        } else {
            loadingDialog = LoadingDialog(
                context,
                cancel,
                loadingTxt
            )
        }
    }


    protected fun showLoading() {
        loadingDialog.show()
    }

    protected fun dissmissLoading() {
        loadingDialog.dismiss()
    }

}
