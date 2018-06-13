package com.example.administrator.mykline;

import android.app.Application;

import com.example.administrator.mykline.utils.OkHttpUtils;

/**
 * @author Administrator
 * @version 1.0  2018/6/1.
 */
public class App extends Application {
//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化OKhttp对象
        OkHttpUtils.init(getApplicationContext());
    }
}