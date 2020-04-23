package cn.segi.wanandroid.demo.ui.Application

import android.app.Application

class MvvmApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MvvmInitializer.instance.init(this)
    }
}