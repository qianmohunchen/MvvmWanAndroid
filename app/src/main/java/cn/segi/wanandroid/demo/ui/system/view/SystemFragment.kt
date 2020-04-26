package cn.segi.wanandroid.demo.ui.system.view

import android.graphics.Color
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.segi.wanandroid.R
import cn.segi.wanandroid.demo.base.network.response.BaseResponse
import cn.segi.wanandroid.demo.base.utils.ColorUtil
import cn.segi.wanandroid.demo.base.view.BaseFragment
import cn.segi.wanandroid.demo.ui.system.adapter.SystemAdapter
import cn.segi.wanandroid.demo.ui.system.viewmodel.SystemViewModel
import com.wjx.android.wanandroidmvvm.ui.system.data.SystemLabelResponse
import com.wjx.android.wanandroidmvvm.ui.system.data.SystemTabNameResponse
import kotlinx.android.synthetic.main.system_fragment.*

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/27
 * Time: 17:01
 */
class SystemFragment : BaseFragment() {

    private lateinit var mAdapter: SystemAdapter

    private lateinit var systemViewModel: SystemViewModel

    companion object {
        fun getInstance(): SystemFragment? {
            return SystemFragment()
        }
    }


    override fun getLayoutId(): Int = R.layout.system_fragment

    override fun initView() {
        initRefresh()
        recycler_view?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = SystemAdapter(
            R.layout.system_item,
            null
        )
        recycler_view.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { _, _, position ->
            val item = mAdapter.getItem(position)
            item?.let {
            }
        }
        createLoading(activity!!, true, "加载system数据")
    }

    override fun initData() {
        systemViewModel = ViewModelProvider(this).get(SystemViewModel::class.java)
        showLoading()
        systemViewModel.loadSystemTab()
        systemViewModel.getSystemTab()
            .observe(this, Observer<BaseResponse<List<SystemTabNameResponse>>> { tabs ->
                mAdapter.setNewData(tabs.data)
                mAdapter.notifyDataSetChanged()
                dissmissLoading()
            })
    }

    private fun initRefresh() {
        // 设置下拉刷新的loading颜色
        system_refresh.setProgressBackgroundColorSchemeColor(ColorUtil.getColor(activity!!))
        system_refresh.setColorSchemeColors(Color.WHITE)
        system_refresh.setOnRefreshListener { onRefreshData() }
    }

    private fun onRefreshData() {
        systemViewModel.loadSystemTab()
    }

    private fun setSystemTabData(systemListName: List<SystemTabNameResponse>) {
        val chileItems = arrayListOf<SystemLabelResponse>()
        // 返回列表为空显示加载完毕
        if (systemListName.isEmpty()) {
            mAdapter.loadMoreEnd()
            return
        }

        // 如果是下拉刷新状态，直接设置数据
        if (system_refresh.isRefreshing) {
            system_refresh.isRefreshing = false
            mAdapter.setNewData(systemListName)
            mAdapter.loadMoreComplete()
            return
        }

        // 初始化状态直接加载数据
        mAdapter.addData(systemListName)
        mAdapter.loadMoreComplete()
    }

}