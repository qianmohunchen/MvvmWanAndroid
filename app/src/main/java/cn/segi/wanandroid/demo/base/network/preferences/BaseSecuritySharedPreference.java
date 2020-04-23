package cn.segi.wanandroid.demo.base.network.preferences;

import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * 加密SharedPreferences封装<br>
 * 对所有数据进行加密，相对BaseSharedPreferences性能较低，用于敏感信息加密
 *
 * @author liangzx
 * @version 1.0
 * @time 2020-03-03 16:47
 **/
public abstract class BaseSecuritySharedPreference extends BaseSharedPreferences {
    /**
     * 是否已升级为加密数据
     */
    private static final String HAS_UPDATE_SECURITY_DATA = "has_update_security_data";

    public BaseSecuritySharedPreference() {
        super();
        updateToSecurityData();
    }

    /**
     * 升级为加密数据
     */
    private void updateToSecurityData() {
        boolean hasUpdateData = get(HAS_UPDATE_SECURITY_DATA, false);
        if (!hasUpdateData) {
            Map<String, ?> all = super.getAll();
            if (null == all || all.isEmpty()) {
                put(HAS_UPDATE_SECURITY_DATA, true);
                return;
            }
            if (!contains(HAS_UPDATE_SECURITY_DATA)) {
                clear();
                Iterator<? extends Map.Entry<String, ?>> it = all.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, ?> next = it.next();
                    put(next.getKey(), next.getValue());
                }
                put(HAS_UPDATE_SECURITY_DATA, true);
            }
        }
    }

    @Override
    public BaseSharedPreferences put(String key, Object value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
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
            return this;
        }
        if (isApply()) {
            editor.apply();
        } else {
            editor.commit();
        }
        return this;
    }

    @Override
    public String get(String key, String defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = getSharedPreferences().getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return AESForSpUtils.decrypt(result);
    }

    @Override
    public String getStringDefault(String key) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = getSharedPreferences().getString(sKey, null);
        return AESForSpUtils.decrypt(result);
    }

    @Override
    public boolean get(String key, boolean defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = getSharedPreferences().getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return Boolean.valueOf(AESForSpUtils.decrypt(result));
    }

    @Override
    public int get(String key, int defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = getSharedPreferences().getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return Integer.valueOf(AESForSpUtils.decrypt(result));
    }

    @Override
    public long get(String key, long defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = getSharedPreferences().getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return Long.valueOf(AESForSpUtils.decrypt(result));
    }

    @Override
    public float get(String key, float defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        String result = getSharedPreferences().getString(sKey, null);
        if (TextUtils.isEmpty(result)) {
            return defaultValue;
        }
        return Float.valueOf(AESForSpUtils.decrypt(result));
    }

    @Override
    public Set<String> get(String key, Set<String> defaultValue) {
        final String sKey = AESForSpUtils.encrypt(key);
        Set<String> set = getSharedPreferences().getStringSet(sKey, null);
        if (null == set || set.isEmpty()) {
            return defaultValue;
        }
        Set<String> tmp = new LinkedHashSet<>(set.size());
        for (String item : set) {
            tmp.add(AESForSpUtils.decrypt(item));
        }
        return tmp;
    }

    @Override
    public Set<String> getSetDefault(String key) {
        return get(key, new HashSet<String>(0));
    }

    @Override
    public Map<String, Object> getAll() {
        final Map<String, ?> all = getSharedPreferences().getAll();
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

    /**
     * 是否存在该key
     *
     * @param key key
     * @return
     */
    public boolean contains(@NonNull String key) {
        final String sKey = AESForSpUtils.encrypt(key);
        return getSharedPreferences().contains(sKey);
    }

    /**
     * 移除该key对应的内容<br>
     * 默认同步提交，如需异步提交需提前调用useApply方法
     *
     * @param key key
     */
    public BaseSharedPreferences remove(@NonNull String key) {
        final String sKey = AESForSpUtils.encrypt(key);
        SharedPreferences.Editor editor = getSharedPreferences().edit().remove(sKey);
        if (isApply()) {
            editor.apply();
        } else {
            editor.commit();
        }
        return this;
    }
}
