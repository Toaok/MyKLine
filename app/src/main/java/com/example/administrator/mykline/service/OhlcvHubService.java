package com.example.administrator.mykline.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.mykline.MainActivity;
import com.example.administrator.mykline.OkHttpBase;
import com.example.administrator.mykline.utils.ThreadPoolManager;
import com.example.administrator.mykline.utils.Util;
import com.google.gson.JsonElement;

import java.util.Timer;
import java.util.TimerTask;

import donky.microsoft.aspnet.signalr.client.Action;
import donky.microsoft.aspnet.signalr.client.Platform;
import donky.microsoft.aspnet.signalr.client.SignalRFuture;
import donky.microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import donky.microsoft.aspnet.signalr.client.hubs.HubConnection;
import donky.microsoft.aspnet.signalr.client.hubs.HubProxy;
import donky.microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;


//import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Administrator on 2018/1/30.
 * K线Service
 */

public class OhlcvHubService extends Service {
    //192.168.1.242
    //测试 exchange.ico17.com
    public static final String serverUrl = OkHttpBase.UrlZs;//服务器地址
    public static final String SERVER_HUB_CHAT = "OhlcvHub";//与服务器那边的名称一致
    public static final String SERVER_INIT = "AddToGroup";
    public static final String SERVER_METHOD_SEND = "GetOhlcvByPeriod";//服务器上面的方法
    public static final String CLIENT_METHOD_RECEIVE = "UpdateOhlcvChange";//客户端这边的方法，服务器调用时需要根据这个来进行对应的调用
    //连接
    private HubConnection mHubConnection = null;
    private HubProxy mHubProxy;
    //时间
    private Timer Meitimer;
    private int time;
    //交易对
    private String PairName;

    private final IBinder mBinder = (IBinder) new LocalBinder();

    // private Handler  mHandler = new Handler();
    //每次更新的数据
    private String Updatecount;

    private String Lastcode = "";

    private boolean isbool = false;

    @Nullable
    //创建服务
    @Override
    public void onCreate() {


    }


    //service绑定服务
    @Override
    public IBinder onBind(Intent intent) {
        PairName = intent.getStringExtra("pairName");
       /* if(!OkHttpBase.isConnected)
        {*/
        OhlcvHubonStartCommand();

        return mBinder;
    }


    //service开始服务
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        PairName = intent.getStringExtra("pairName");


        return result;
    }

    //销毁服务
    @Override
    public void onDestroy() {
//        RefWatcher refWatcher = MyApplication.getRefWatcher(this);//1
//        refWatcher.watch(this);
        if (mHubConnection != null && isbool) {
            mHubConnection.stop();
            mHubConnection = null;
        } else {
            isbool = false;
        }

        // mHandler.removeCallbacks(UpdateTherad);
        //mHandler.removeCallbacks(SignalTask);
        //ThreadPoolManager.getInstance().remove(UpdateTherad);
        //mHandler.removeCallbacksAndMessages(null);
    }

    Runnable SignalTask = new Runnable() {
        @Override
        public void run() {
            Platform.loadPlatformComponent(new AndroidPlatformComponent());
            //创建
            mHubConnection = new HubConnection(serverUrl, true);//初始化连接
            mHubProxy = mHubConnection.createHubProxy(SERVER_HUB_CHAT);
            time = 0;
            connectTimeout();
            if (mHubConnection != null) {

                Log.i("hub", "初始化服务k线");
                ServerSentEventsTransport sentEventsTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
//                sentEventsTransport.start(mHubConnection,,);
                SignalRFuture<Void> awaitConnection = mHubConnection.start(sentEventsTransport);

                Log.i("hub", "启动服务k线");
                if (mHubProxy != null && awaitConnection != null) {
                    try {

                        awaitConnection.get();
//                        String code = PairName + "" + Util.T1;
                        UpdateLineData(PairName, Util.T1);//调用服务器的AddToGroup方法，进行第一次数据连接
                        serviceOpatorClient();
                        String token = mHubConnection.getConnectionToken();
                        if (token != null) {
                            Log.i("hub", "连接·成功k线");
                        } else {
                            Log.i("hub", "连接失败");
                        }
                        Log.i("hub", "连接到服务器k线");
                        isbool = true;
                        //ThreadPoolManager.getInstance().remove(SignalTask);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        mHubConnection.stop();
                        ThreadPoolManager.getInstance().remove(SignalTask);
                        OhlcvHubonStartCommand();
                    }
                } else {
                    Log.i("CONNECET_FAIL", "HUB = NULL");
                }

            } else {
                Log.i("CONNECET_FAIL", "CONNETION = NULL");

            }
        }
    };

    //开始连接服务方法
    public void OhlcvHubonStartCommand() {
        ThreadPoolManager.getInstance().execute(SignalTask);
    }

    /**
     * 服务器连接超时
     */
    public void connectTimeout() {
        Meitimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mHubConnection != null) {
                        String token = mHubConnection.getConnectionToken();
                        if (token != null) {
                            Meitimer.cancel();
                            time = 0;
                            return;
                        }
                    }
                    if (time > 0) {
                        mHubConnection.stop();
                        ThreadPoolManager.getInstance().remove(SignalTask);
                        OhlcvHubonStartCommand();
                        Log.i("+++connectTimeout", "连接超时");
                        time = 0;
                        Meitimer.cancel();
                        return;
                    }

                    time = time + 1;
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        Meitimer.scheduleAtFixedRate(task, 1000 * 20, 1000 * 20);
    }

    /**
     * 服务器通知客户端调用方法
     */
    private void serviceOpatorClient() {


        mHubProxy.subscribe(CLIENT_METHOD_RECEIVE).addReceivedHandler(
                new Action<JsonElement[]>() {

                    @Override
                    public void run(JsonElement[] obj)
                            throws Exception {
                        Updatecount = obj[0].toString();
                        if (Updatecount != null) {
                            Log.i("hub", "k线更新数据" + Updatecount + "长度：" + obj.length);
                            ThreadPoolManager.getInstance().execute(UpdateTherad);
                        }

                    }
                });

    }


    //绑定方法
    public class LocalBinder extends Binder {
        public OhlcvHubService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return OhlcvHubService.this;
        }
    }

    //更新数据开启线程更新到UI
    Runnable UpdateTherad = new Runnable() {
        @Override
        public void run() {
            Message mssg = Message.obtain();
            if (mssg != null) {
                mssg.what = 1;
                mssg.obj = Updatecount;
                if (MainActivity.mHandler != null) {
                    MainActivity.mHandler.sendMessage(mssg);
                    //ThreadPoolManager.getInstance().remove(UpdateTherad);
                }

            }
        }
    };

    //变更K线分组数据
    public void UpdateLineData(final String code, String type) {
        mHubProxy.invoke(SERVER_INIT, code + type, Lastcode);
        Lastcode = code;
    }
    /*interface UpdateLineDates
    {

        public abstract void UpdateLineData();
    }*/

}
