package cn.segi.wanandroid.demo.base.network.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.segi.wanandroid.demo.application.FrameworkInitializer;

/**
 * SharedPreferences使用封装
 *
 * @author liangzx
 * @version 1.0
 * @time 2018-05-05 15:48
 **/
public abstract class BaseSharedPreferences {

    /**
     * 登记式单例列表
     */
    private static volatile Map<String, BaseSharedPreferences> registryMap = new HashMap<>();
    /**
     * SharedPreferences实体
     */
    private SharedPreferences mSharedPreferences;
    /**
     * 是否异步提交<br>
     * 默认为false，当调用方法getInstance后也会变为false，如需异步提交需在put之前调用方法useApply
     */
    private boolean mIsApply;

    @SuppressLint("CommitPrefEdits")
    public BaseSharedPreferences() {
        mIsApply = false;
        mSharedPreferences = FrameworkInitializer.getContext().getSharedPreferences(getFileName(), Context.MODE_PRIVATE);
        String clazzName = this.getClass().getName();
        synchronized (registryMap) {
            registryMap.put(clazzName, this);
        }
    }

    /**
     * 单例实现
     *
     * @param clazz 子类
     * @return 返回逻辑子类实例
     */
    public static BaseSharedPreferences getInstance(Class<? extends BaseSharedPreferences> clazz) {
        String clazzName = clazz.getName();
        if (!registryMap.containsKey(clazzName)) {
            synchronized (registryMap) {
                if (!registryMap.containsKey(clazzName)) {
                    try {
                        return clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        BaseSharedPreferences bsp = registryMap.get(clazzName);
        bsp.mIsApply = false;
        return bsp;
    }

    /**
     * 获取文件名称
     *
     * @return
     */
    protected abstract String getFileName();

    /**
     * 使用异步提交
     *
     * @return
     */
    public BaseSharedPreferences useApply() {
        mIsApply = true;
        return this;
    }

    /**
     * 设置值<br>
     * 默认同步提交，如需异步提交需提前调用useApply方法
     *
     * @param key   key
     * @param value 值
     * @return
     */
    public BaseSharedPreferences put(String key, Object value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (null == value) {
            editor.remove(key);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        } else {
            return this;
        }
        if (mIsApply) {
            editor.apply();
        } else {
            editor.commit();
        }
        return this;
    }

    /**
     * 返回值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    /**
     * 返回值
     *
     * @param key key
     * @return
     */
    public String getStringDefault(String key) {
        return mSharedPreferences.getString(key, null);
    }

    /**
     * 返回值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 返回值
     *
     * @param key key
     * @return
     */
    public boolean getBooleanDefault(String key) {
        return get(key, false);
    }

    /**
     * 返回值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public int get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    /**
     * 返回值
     *
     * @param key key
     * @return
     */
    public int getIntDefault(String key) {
        return get(key, 0);
    }

    /**
     * 返回值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public long get(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 返回值
     *
     * @param key key
     * @return
     */
    public long getLongDefault(String key) {
        return get(key, 0L);
    }

    /**
     * 返回值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * 返回值
     *
     * @param key key
     * @return
     */
    public float getFloatDefault(String key) {
        return get(key, 0F);
    }

    /**
     * 返回值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return
     */
    public Set<String> get(String key, Set<String> defaultValue) {
        return mSharedPreferences.getStringSet(key, defaultValue);
    }

    /**
     * 返回值
     *
     * @param key key
     * @return
     */
    public Set<String> getSetDefault(String key) {
        return mSharedPreferences.getStringSet(key, null);
    }

    /**
     * 获取所有键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    /**
     * 是否存在该key
     *
     * @param key key
     * @return
     */
    public boolean contains(@NonNull final String key) {
        return mSharedPreferences.contains(key);
    }

    /**
     * 移除该key对应的内容<br>
     * 默认同步提交，如需异步提交需提前调用useApply方法
     *
     * @param key key
     */
    public BaseSharedPreferences remove(@NonNull final String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit().remove(key);
        if (mIsApply) {
            editor.apply();
        } else {
            editor.commit();
        }
        return this;
    }

    /**
     * 清除所有数据<br>
     * 默认同步提交，如需异步提交需提前调用useApply方法
     */
    public BaseSharedPreferences clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit().clear();
        if (mIsApply) {
            editor.apply();
        } else {
            editor.commit();
        }
        return this;
    }

    /**
     * 注销所有实例
     */
    public static void destory() {
        if (null == registryMap) {
            return;
        }
        registryMap.clear();
    }

    /**
     * 获取SharedPreferences实例
     *
     * @return
     */
    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    /**
     * 是否异步提交
     *
     * @return
     */
    public boolean isApply() {
        return mIsApply;
    }
}
