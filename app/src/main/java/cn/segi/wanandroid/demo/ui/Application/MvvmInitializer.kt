package cn.segi.wanandroid.demo.ui.Application

import cn.segi.wanandroid.demo.application.FrameworkInitializer

class MvvmInitializer :FrameworkInitializer() {

    companion object{
        val instance:MvvmInitializer by lazy {
            MvvmInitializer()
        }
    }
}