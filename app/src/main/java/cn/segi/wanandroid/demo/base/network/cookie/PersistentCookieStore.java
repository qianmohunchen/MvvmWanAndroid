/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    https://loopj.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        https://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package cn.segi.wanandroid.demo.base.network.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.segi.wanandroid.demo.application.FrameworkInitializer;
import cn.segi.wanandroid.demo.base.network.preferences.AESForSpUtils;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * 参考于开源库android-async-http的实现<br>
 * 开源库地址：https://github.com/loopj/android-async-http
 */
public class PersistentCookieStore {
    /**
     * 是否已升级为加密数据
     */
    private static final String HAS_UPDATE_SECURITY_DATA = "has_update_security_data";
    /**
     * Cookie的Key名
     */
    public static final String COOKIES_KEY = "Cookie";
    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS_NAME = "cookies_info";
    private final Map<String, ConcurrentHashMap<String, Cookie>> mAllCookies;
    private final SharedPreferences mCookiePrefs;
    private static PersistentCookieStore mPersistentCookieStore;

    private PersistentCookieStore() {
        mCookiePrefs = FrameworkInitializer.getContext().getSharedPreferences(COOKIE_PREFS_NAME, Context.MODE_PRIVATE);
        mAllCookies = new HashMap<>();
        updateToSecurityData();
        getAllCookiesFromXml();
    }

    /**
     * 升级为加密数据
     */
    private void updateToSecurityData() {
        boolean hasUpdateData = getSecurity(HAS_UPDATE_SECURITY_DATA, false);
        if (!hasUpdateData) {
            Map<String, ?> all = mCookiePrefs.getAll();
            if (null == all || all.isEmpty()) {
                putSecurity(HAS_UPDATE_SECURITY_DATA, true);
                return;
            }
            if (!containsSecurity(HAS_UPDATE_SECURITY_DATA)) {
                removeAll();
                Iterator<? extends Map.Entry<String, ?>> it = all.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, ?> next = it.next();
                    putSecurity(next.getKey(), next.getValue());
                }
                putSecurity(HAS_UPDATE_SECURITY_DATA, true);
            }
        }
    }

    /**
     * 从xml中获取所有cookie数据
     */
    private void getAllCookiesFromXml() {
        //将持久化的cookies缓存到内存中 即map cookies
        Map<String, ?> prefsMap = getAllSecurity();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
            for (String name : cookieNames) {
                String encodedCookie = getSecurity(name, null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        if (!mAllCookies.containsKey(entry.getKey())) {
                            mAllCookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                        }
                        mAllCookies.get(entry.getKey()).put(name, decodedCookie);
                    }
                }
            }
        }
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static PersistentCookieStore getInstance() {
        if (null == mPersistentCookieStore) {
            mPersistentCookieStore = new PersistentCookieStore();
        }
        return mPersistentCookieStore;
    }

    /**
     * 获取cookie的token
     *
     * @param cookie
     * @return
     */
    protected String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * 批量添加Cookies信息
     *
     * @param url
     * @param cookies
     */
    public void addAll(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie c : cookies) {
                String name = getCookieToken(c);
                //将cookies缓存到内存中 如果缓存过期 就重置此cookie
//                if (!c.persistent()) {
                if (!mAllCookies.containsKey(url.host())) {
                    mAllCookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
                }
                mAllCookies.get(url.host()).put(name, c);
//                } else {
//                    if (mAllCookies.containsKey(url.host())) {
//                        mAllCookies.get(url.host()).remove(name);
//                    }
//                }
                putSecurity(url.host(), TextUtils.join(",", mAllCookies.get(url.host()).keySet()));
                putSecurity(name, encodeCookie(new SerializableOkHttpCookies(c)));
            }
        }
    }

    /**
     * 根据域名获取Cookies
     *
     * @param url
     * @return
     */
    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (mAllCookies.containsKey(url.host())) {
            ret.addAll(mAllCookies.get(url.host()).values());
        }
        return ret;
    }

    /**
     * 移除所有cookie信息e cast to com.framework.lib.net.cookie.SerializableOkHtt
     *
     * @return
     */
    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = mCookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.commit();
        putSecurity(HAS_UPDATE_SECURITY_DATA, true);
        mAllCookies.clear();
        return true;
    }

    /**
     * 移除某个域名的Cookies信息
     *
     * @param url
     * @param cookie
     * @return
     */
    public boolean remove(HttpUrl url, Cookie cookie) {
        String name = getCookieToken(cookie);
        if (mAllCookies.containsKey(url.host()) && mAllCookies.get(url.host()).containsKey(name)) {
            mAllCookies.get(url.host()).remove(name);
            if (containsSecurity(name)) {
                removeSecurity(name);
            }
            putSecurity(url.host(), TextUtils.join(",", mAllCookies.get(url.host()).keySet()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取所有Cookie信息
     *
     * @return
     */
    public List<Cookie> getAllCookies() {
        ArrayList<Cookie> ret = new ArrayList<>();
        if (null == mAllCookies) {
            getAllCookiesFromXml();
        }
        for (String key : mAllCookies.keySet()) {
            ret.addAll(mAllCookies.get(key).values());
        }
        return ret;
    }

    /**
     * 获取所有Cookie信息
     *
     * @return
     */
    public String getAllCookiesStr() {
        StringBuilder cookieSb = new StringBuilder();
        if (null == mAllCookies) {
            getAllCookiesFromXml();
        }
        for (String domain : mAllCookies.keySet()) {
            ConcurrentHashMap<String, Cookie> cookieHm = mAllCookies.get(domain);
            Iterator<String> it = cookieHm.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                cookieSb.append(cookieHm.get(key).name())
                        .append("=")
                        .append(cookieHm.get(key).value())
                        .append(";");

            }
        }
        return cookieSb.toString();
    }

    /**
     * cookies 序列化成 string
     *
     * @param cookie 要序列化的cookie
     * @return 序列化之后的string
     */
    protected String encodeCookie(SerializableOkHttpCookies cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (IOException e) {
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * 将字符串反序列化成cookies
     *
     * @param cookieString cookies string
     * @return cookie object
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableOkHttpCookies) objectInputStream.readObject()).getCookies();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }

        return cookie;
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    //====================安全加密操作====================
    private void putSecurity(String key, Object value) {
        SharedPreferences.Editor editor = mCookiePrefs.edit();
        final String sKey = AESForSpUtils.encrypt(key);
        if (null == value) {
            editor.remove(sKey);
        } else if (value instanceof String) {
            editor.putString(sKey, AESForSpUtils.encrypt((String) value));
        } else if (value instanceof Boolean) {
            editor.putString(sKey, AESForSpUtils.encrypt(Boolean.toString((Boolean) value)));
        } else if (value instanceof Integer) {
            editor.putString(sKey, AESForSpUtils.encrypt(value.toString()));
        } else if (value instanceof Long) {
            editor.putString(sKey, AESForSpUtils.encrypt(value.toString()));
        } else if (value instanceof Float) {
            editor.putString(sKey, AESForSpUtils.encrypt(value.toString()));
        } else if (value instanceof Set) {
            Set<String> tmp = new LinkedHashSet<>();
            for (String item : (Set<String>) value) {
                tmp.add(AESForSpUtils.encrypt(item));
            }
            editor.putStringSet(sKey, tmp);
        } else {
            return;
        }
        editor.commit();
    }

    private boolean containsSecurity(@NonNull String key) {
        final String sKey = AESForSpUtils.encrypt(key);
        return mCookiePrefs.contains(sKey);
    }

    private String getSecurity(String key, String defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = mCookiePrefs.getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return AESForSpUtils.decrypt(result);
    }

    public boolean getSecurity(String key, boolean defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = mCookiePrefs.getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return Boolean.valueOf(AESForSpUtils.decrypt(result));
    }

    private Map<String, Object> getAllSecurity() {
        final Map<String, ?> all = mCookiePrefs.getAll();
        if (null == all || all.isEmpty()) {
            return null;
        }
        Map<String, Object> result = new HashMap<>(all.size());
        Iterator<? extends Map.Entry<String, ?>> it = all.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ?> next = it.next();
            final String key = AESForSpUtils.decrypt(next.getKey());
            Object value = next.getValue();
            if (null != value && value instanceof Set) {
                Set<String> setValue = (Set<String>) value;
                Set<String> newValue = new LinkedHashSet<>(setValue.size());
                for (String item : setValue) {
                    newValue.add(AESForSpUtils.decrypt(item));
                }
                value = newValue;
            } else {
                value = AESForSpUtils.decrypt(String.valueOf(value));
            }
            result.put(key, value);
        }
        return result;
    }

    private void removeSecurity(@NonNull String key) {
        final String sKey = AESForSpUtils.encrypt(key);
        SharedPreferences.Editor editor = mCookiePrefs.edit().remove(sKey);
        editor.commit();
    }
}