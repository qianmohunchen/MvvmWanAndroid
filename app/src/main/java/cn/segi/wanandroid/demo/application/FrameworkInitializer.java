package cn.segi.wanandroid.demo.application;

import android.app.Application;
import android.content.Context;

public abstract class FrameworkInitializer {

    /**
     * 全局上下文
     */
    private static Application mContext;

    /**
     * 初始化方法<br>
     * 需要在Application中调用
     *
     * @param context 上下文
     */
    public void init(Application context) {
        mContext = context;
        startApplication();
    }

    protected void startApplication(){

    }

    /**
     * 返回上下文
     *
     * @return Context
     */
    public static Context getContext() {
        return mContext;
    }

}
