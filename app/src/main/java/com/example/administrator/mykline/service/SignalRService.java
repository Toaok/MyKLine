package com.example.administrator.mykline.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.administrator.mykline.MainActivity;
import com.example.administrator.mykline.OkHttpBase;
import com.example.administrator.mykline.utils.ThreadPoolManager;
import com.google.gson.JsonElement;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import donky.microsoft.aspnet.signalr.client.Action;
import donky.microsoft.aspnet.signalr.client.ErrorCallback;
import donky.microsoft.aspnet.signalr.client.Platform;
import donky.microsoft.aspnet.signalr.client.SignalRFuture;
import donky.microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import donky.microsoft.aspnet.signalr.client.hubs.HubConnection;
import donky.microsoft.aspnet.signalr.client.hubs.HubProxy;
import donky.microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import donky.microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;
import donky.microsoft.aspnet.signalr.client.transport.WebsocketTransport;


//import com.squareup.leakcanary.RefWatcher;


public class SignalRService extends Service {

    public static final String serverUrl = OkHttpBase.UrlZs;//服务器地址

    //K线推送
    public static final String SERVER_HUB_CHAT_OHLCV = "OhlcvHub";//与服务器那边的名称一致
    public static final String SERVER_INIT_OHLCV = "AddToGroup";
    public static final String SERVER_METHOD_SEND_OHLCV = "GetOhlcvByPeriod";//服务器上面的方法
    public static final String CLIENT_METHOD_RECEIVE_OHLCV = "UpdateOhlcvChange";//客户端这边的方法，服务器调用时需要根据这个来进行对应的调用


    private static final String TAG = "SignalRService";
    private HubConnection mHubConnection;

    private HubProxy mHubProxyOhlcv;
    private final IBinder mBinder = (IBinder) new LocalBinder();

    //定时
    private Timer timer;
    private int countTime;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        return result;
    }

    @Override
    public IBinder onBind(Intent intent) {
//        oderBookPairName = intent.getStringExtra("pairName");
        ohlcvLastPairName = intent.getStringExtra("pairName");
        ohlcvLastType = intent.getStringExtra("type");
        startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            return SignalRService.this;
        }
    }


    public void startSignalR() {

        ThreadPoolManager.getInstance().execute(SignalTask);
    }

    Runnable SignalTask = new Runnable() {
        @Override
        public void run() {
            Platform.loadPlatformComponent(new AndroidPlatformComponent());
            mHubConnection = new HubConnection(serverUrl);//初始化连接
            mHubProxyOhlcv = mHubConnection.createHubProxy(SERVER_HUB_CHAT_OHLCV);
            countTime = 0;
            connectTimeout();
            if (mHubConnection != null) {
                Log.i(TAG, "初始化服务");

                /*
                 * .done(function(){ console.log('Now connected, connection ID=' + connection.id); })
                 * .fail(function(){ console.log('Could not connect'); });
                 * */

                SignalRFuture signalRFuture = mHubConnection.start(new WebsocketTransport(mHubConnection.getLogger())).done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        Log.i(TAG, "Now connected, connection ID=" + mHubConnection.getConnectionId());
                        Log.i(TAG, "启动服务");
                    }
                }).onError(new ErrorCallback() {
                    @Override
                    public void onError(Throwable throwable) {
                        Log.i(TAG, "Could not connect");
                        ThreadPoolManager.getInstance().remove(SignalTask);
                        startSignalR();
                    }
                });

                try {
                    Log.i(TAG, "signalRFuture will do get");
                    signalRFuture.get(2000, TimeUnit.MILLISECONDS);
                    Log.i(TAG, "signalRFuture had do get");
                } catch (Exception e) {
                    e.printStackTrace();
                    ThreadPoolManager.getInstance().remove(SignalTask);
                    startSignalR();
                }
                if (mHubProxyOhlcv != null) {
                    ohlcvSendMessage(ohlcvLastPairName, ohlcvLastType);

                    GetOhlcvByPeriod();
                } else {
                    Log.i("CONNECET_FAIL", "HUB = NULL");
                }
            } else {
                Log.i("CONNECET_FAIL", "CONNETION = NULL");
            }
        }
    };


    private String data;

    /**
     * OhlcvByPeriod
     */
    public void GetOhlcvByPeriod() {

        mHubProxyOhlcv.on(CLIENT_METHOD_RECEIVE_OHLCV, new SubscriptionHandler1<JsonElement>() {
            @Override
            public void run(JsonElement json) {
                Log.i(TAG, "k线" + json);
                data = json.toString();
                if (data.indexOf(ohlcvLastPairName) > 0 && data.indexOf(ohlcvLastType) > 0) {
                    Log.i(TAG, "k线数据正确" + json);
                    ThreadPoolManager.getInstance().execute(updataData);
                } else {
                    Log.i(TAG, "k线数据错误" + json);
                }
            }
        }, JsonElement.class);

    }

    Runnable updataData = new Runnable() {
        @Override
        public void run() {
            Message mssg = Message.obtain();
            if (mssg != null) {
                mssg.what = 1;
                mssg.obj = data;
                if (MainActivity.mHandler != null) {
                    MainActivity.mHandler.sendMessage(mssg);
                    ThreadPoolManager.getInstance().remove(updataData);
                }

            }
        }
    };


    @Override
    public void onDestroy() {
        if (mHubConnection != null) {
            mHubConnection.stop();
        }
    }


    //ohlcv 切换交易对
    private String ohlcvLastPairName;
    private String ohlcvLastType;

    public void ohlcvSendMessage(final String pairName, final String type) {
        /*
        * .done(function () {
        console.log ('Invocation of NewContosoChatMessage succeeded');
    }).fail(function (error) {
        console.log('Invocation of NewContosoChatMessage failed. Error: ' + error);
    });
        * */
        mHubProxyOhlcv.invoke(SERVER_INIT_OHLCV, pairName + type, ohlcvLastPairName).done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                Log.i(TAG, "Invocation of NewContosoChatMessage succeeded");
                Log.i(TAG, "GroupsToken:" + mHubConnection.getGroupsToken());
                Log.i(TAG, "change pairName:" + pairName);
                Log.i(TAG, "change type:" + type);
                /*if (mHubConnection.getGroupsToken() == null) {
                    startSignalR();
                }*/

            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "Invocation of NewContosoChatMessage failed. Error:" + throwable.getMessage());
                Log.i(TAG, "GroupsToken:" + mHubConnection.getGroupsToken());
                Log.i(TAG, "change pairName:" + pairName);
                Log.i(TAG, "change type:" + type);
                ThreadPoolManager.getInstance().remove(SignalTask);
                startSignalR();
            }
        });
        ohlcvLastPairName = pairName;
        ohlcvLastType = type;
    }


    /**
     * 连接服务器超时
     */
    public void connectTimeout() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (mHubConnection != null) {
                        String token = mHubConnection.getConnectionToken();
                        if (token != null) {
                            timer.cancel();
                            countTime = 0;
                            return;
                        }
                    }
                    if (countTime > 0) {
                        ThreadPoolManager.getInstance().remove(SignalTask);
                        startSignalR();
                        Log.i(TAG, "连接失败");
                        Log.i("+++connectTimeout", "连接超时");
                        countTime = 0;
                        timer.cancel();
                        return;
                    }
                    countTime = countTime + 1;
                } catch (Exception e) {
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000 * 10, 1000 * 10);
    }
}