package com.example.administrator.mykline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.mykline.model.OhlcvInfoBean;
import com.example.administrator.mykline.service.SignalRService;
import com.example.administrator.mykline.utils.OkHttpUtils;
import com.example.administrator.mykline.utils.Util;
import com.example.klinelib.chart.KLineView;
import com.example.klinelib.chart.TimeLineView;
import com.example.klinelib.model.HisData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private static DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

    private static final String ETH_BTC = "ETH_BTC";
    private static final String SNT_BTC = "SNT_BTC";
    private static final String EOS_BTC = "EOS_BTC";
    private static final String OMG_BTC = "OMG_BTC";
    private static final String EOS_ETH = "EOS_ETH";
    private static final String OMG_ETH = "OMG_ETH";
    private static final String SNT_ETH = "SNT_ETH";


    private String pairName;
    private String type;

    public static Handler mHandler;

    private KLineView mKLineView;

    private TimeLineView mTimeLineView;

    private Spinner mSpinner;

    private TextView fenshi_tv, one_min, day_line, klin_view, depthmap_view;

    private SignalRService mService;

    //K线集合
    private List<OhlcvInfoBean> ohlcvInfolst = null;
    private List<HisData> hisData = null;
    private List<HisData> fenshiHisData = null;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((SignalRService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mHandler = new KLineHandler(MainActivity.this);

        pairName=ETH_BTC;
        type = Util.T1;

        Intent intent = new Intent(MainActivity.this, SignalRService.class);
        intent.putExtra("pairName", pairName);
        intent.putExtra("type", type);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        initData();
    }

    public void showVolume() {

        mKLineView.post(new Runnable() {
            @Override
            public void run() {
                mKLineView.showVolume();
            }
        });
    }

    public void showMacd() {
        mKLineView.post(new Runnable() {
            @Override
            public void run() {
                mKLineView.showMacd();
            }
        });
    }

    public void showKdj() {
        mKLineView.post(new Runnable() {
            @Override
            public void run() {
                mKLineView.showKdj();
            }
        });
    }

    protected void initData() {



        mSpinner = findViewById(R.id.spinner);
        // 建立数据源
        List<String> pairNames = new ArrayList<>();
        pairNames.add(ETH_BTC);
        pairNames.add(SNT_BTC);
        pairNames.add(EOS_BTC);
        pairNames.add(OMG_BTC);
        pairNames.add(EOS_ETH);
        pairNames.add(OMG_ETH);
        pairNames.add(SNT_ETH);
        //  建立Adapter绑定数据源
        ArrayAdapter mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pairNames);
        //绑定Adapter
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                pairName= parent.getItemAtPosition(position).toString();
                InitKline();
                mService.ohlcvSendMessage(pairName, type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fenshi_tv = findViewById(R.id.fenshi_tv);
        one_min = findViewById(R.id.one_min);
        day_line = findViewById(R.id.day_line);
        klin_view = findViewById(R.id.klin_view);
        depthmap_view = findViewById(R.id.depthmap_view);


        fenshi_tv.setOnClickListener(this);
        one_min.setOnClickListener(this);
        day_line.setOnClickListener(this);
        klin_view.setOnClickListener(this);
        depthmap_view.setOnClickListener(this);

        //kline
        mKLineView = findViewById(R.id.kline);
        mKLineView.setLimitLine();

        //分时
        mTimeLineView=findViewById(R.id.fenshi_line);


        showVolume();
        selectTab(fenshi_tv);
    }


    private void selectTab(View v) {
        switch (v.getId()) {
            case R.id.fenshi_tv:
                fenshi_tv.setBackgroundResource(R.color.text_background);
                day_line.setBackgroundResource(R.color.chart_background);
                one_min.setBackgroundResource(R.color.chart_background);
                mTimeLineView.setDateFormat("HH:mm");
                mTimeLineView.setVisibility(View.VISIBLE);

                mKLineView.setVisibility(View.GONE);
                InitFenshiLine();
                break;
            case R.id.one_min:
                fenshi_tv.setBackgroundResource(R.color.chart_background);
                day_line.setBackgroundResource(R.color.chart_background);
                one_min.setBackgroundResource(R.color.text_background);
                mKLineView.setDateFormat("hh:mm");
                mTimeLineView.setVisibility(View.GONE);
                mKLineView.setVisibility(View.VISIBLE);
                InitKline();
                break;
            case R.id.day_line:
                fenshi_tv.setBackgroundResource(R.color.chart_background);
                one_min.setBackgroundResource(R.color.chart_background);
                day_line.setBackgroundResource(R.color.text_background);
                mKLineView.setDateFormat("MM-dd");
                mTimeLineView.setVisibility(View.GONE);
                mKLineView.setVisibility(View.VISIBLE);
                InitKline();
                break;
            case R.id.klin_view:
//                type=Util.T1;
                break;
            case R.id.depthmap_view:

                break;
        }
    }

    //获取初始化K线
    private void InitKline() {

        RequestBody mRequestBody = new FormBody.Builder().add("pairName", pairName).add("period", type).build();
        Request mRequest = OkHttpUtils.HttpLocale(mRequestBody, OkHttpBase.GetOhlcvByPeriod);
        OkHttpUtils.getOkHttpClient().newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void  onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();

                try {
                    final JSONObject jsonObject = new JSONObject(string);
                    final String success = jsonObject.optString("success");
                    String message = jsonObject.optString("message");

                    if (success.equals("true")) {
//                        Fenshidata = new FenshiDataResponse();
                        String json = null;
                        try {
                            json = jsonObject.optString("data");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ohlcvInfolst = new Gson().fromJson(json, new TypeToken<List<OhlcvInfoBean>>() {
                        }.getType());


                        hisData = new ArrayList<>(100);
                        for (int i = 0; ohlcvInfolst != null && i < ohlcvInfolst.size(); i++) {
                            OhlcvInfoBean o = ohlcvInfolst.get(i);
                            HisData data = new HisData();

                            data.setOpen(o.getO());
                            data.setClose(o.getC());
                            data.setHigh(o.getH());
                            data.setLow(o.getL());
                            data.setVol(o.getV());

                            try {
                                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                data.setDate(utcFormat.parse((o.getT())).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            hisData.add(data);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mKLineView.initData(hisData);

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });

    }


    //初始化分时线
    private void InitFenshiLine() {

        RequestBody mRequestBody = new FormBody.Builder().add("pairName", pairName).add("period", type).build();
        Request mRequest = OkHttpUtils.HttpLocale(mRequestBody, OkHttpBase.GetOhlcvByPeriod);
        OkHttpUtils.getOkHttpClient().newCall(mRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void  onResponse(Call call, final Response response) throws IOException {
                final String string = response.body().string();

                try {
                    final JSONObject jsonObject = new JSONObject(string);
                    final String success = jsonObject.optString("success");
                    String message = jsonObject.optString("message");

                    if (success.equals("true")) {
//                        Fenshidata = new FenshiDataResponse();
                        String json = null;
                        try {
                            json = jsonObject.optString("data");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ohlcvInfolst = new Gson().fromJson(json, new TypeToken<List<OhlcvInfoBean>>() {
                        }.getType());


                        fenshiHisData = new ArrayList<>(100);
                        for (int i = 0; ohlcvInfolst != null && i < ohlcvInfolst.size(); i++) {
                            OhlcvInfoBean o = ohlcvInfolst.get(i);
                            HisData data = new HisData();

                            data.setOpen(o.getO());
                            data.setClose(o.getC());
                            data.setHigh(o.getH());
                            data.setLow(o.getL());
                            data.setVol(o.getV());

                            try {
                                DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                data.setDate(utcFormat.parse((o.getT())).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            fenshiHisData.add(data);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTimeLineView.setCount(fenshiHisData.size(),fenshiHisData.size(),fenshiHisData.size());
                                mTimeLineView.initData(fenshiHisData);
                                if(fenshiHisData.size()>0)
                                mTimeLineView.setLastClose(fenshiHisData.get(0).getClose());
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fenshi_tv:
                selectTab(v);
                type=Util.T1;
                mService.ohlcvSendMessage(pairName,type);
                break;
            case R.id.one_min:
                type=Util.T1;
                selectTab(v);
                mService.ohlcvSendMessage(pairName,type);
                break;
            case R.id.day_line:
                type=Util.D1;
                selectTab(v);
                mService.ohlcvSendMessage(pairName,type);
                break;
            case R.id.klin_view:
                selectTab(v);
                break;
            case R.id.depthmap_view:
                selectTab(v);
                break;
        }
    }


    static class KLineHandler extends Handler {

        private static final String TAG="Handler";

        private WeakReference<MainActivity> weakReference;

        public KLineHandler(MainActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public synchronized void handleMessage(final Message msg) {
            MainActivity activity = weakReference.get();
            if (msg.what == 1) {
                Log.i("hub", "接收到kline 更新数据");
                String json = (String) msg.obj;
                List<OhlcvInfoBean> updatdata = null;
                updatdata = new Gson().fromJson(json, new TypeToken<List<OhlcvInfoBean>>() {
                }.getType());

                if (updatdata != null && activity.ohlcvInfolst != null && activity.ohlcvInfolst.size() > 0) {
                    DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    for (OhlcvInfoBean bean : updatdata) {
                        if (activity.ohlcvInfolst.get(activity.ohlcvInfolst.size() - 1).getT().equals(bean.getT())) {
                            activity.ohlcvInfolst.set(activity.ohlcvInfolst.size() - 1, bean);
                            HisData data = new HisData();
                            data.setOpen(bean.getO());
                            data.setClose(bean.getC());
                            data.setHigh(bean.getH());
                            data.setLow(bean.getL());
                            data.setVol(bean.getV());
                            try {
                                data.setDate(utcFormat.parse((bean.getT())).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            activity.hisData.set(activity.hisData.size() - 1, data);
                            Log.i(TAG,"更新数据"+data.toString());
                        } else {
                            activity.ohlcvInfolst.remove(0);
                            activity.ohlcvInfolst.add(bean);
                            HisData data = new HisData();
                            data.setOpen(bean.getO());
                            data.setClose(bean.getC());
                            data.setHigh(bean.getH());
                            data.setLow(bean.getL());
                            data.setVol(bean.getV());
                            try {
                                data.setDate(utcFormat.parse((bean.getT())).getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            activity.hisData.remove(0);
                            activity.hisData.add(data);
                            Log.i(TAG,"添加数据"+data.toString());
                        }
                        activity.mKLineView.initData(activity.hisData);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
