package cn.segi.wanandroid.demo.base.network.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookie管理器
 *
 * @author liangzx
 * @version 1.0
 * @time 2018-04-20 14:59
 **/
public class DefaultCookiesManager implements CookieJar {

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        PersistentCookieStore.getInstance().addAll(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return PersistentCookieStore.getInstance().getAllCookies();
    }
}