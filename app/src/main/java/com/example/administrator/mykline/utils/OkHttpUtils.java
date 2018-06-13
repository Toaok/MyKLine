package com.example.administrator.mykline.utils;

import android.content.Context;

import com.example.administrator.mykline.OkHttpBase;

import java.util.Locale;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {
    private static final int MAX_CACHE_SIZE = 200 * 1024 * 1024;
    private static OkHttpClient sOkHttpClient;

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();

        sOkHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(FileUtils.getHttpCacheDir(applicationContext), MAX_CACHE_SIZE))
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }

    //返回request对象 一个上下文对象 参数对象body 请求接口
    public static Request HttpLocale(RequestBody mRequestBodysumit, String url) {
        Request mRequestsumit = null;
        if (mRequestBodysumit != null) {
            mRequestsumit = new Request.Builder().post(mRequestBodysumit).url(OkHttpBase.UrlZs + "" + url).header("token", OkHttpBase.token).header("lang", OkHttpBase.lang_zh_cn).build();
        } else {
            mRequestsumit = new Request.Builder().url(OkHttpBase.UrlZs + "" + url).header("token", OkHttpBase.token).header("lang", OkHttpBase.lang_zh_cn).build();
        }
        return mRequestsumit;
    }
}
