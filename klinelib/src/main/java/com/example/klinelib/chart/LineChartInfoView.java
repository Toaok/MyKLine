package com.example.klinelib.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;


import com.example.klinelib.R;
import com.example.klinelib.model.HisData;
import com.example.klinelib.util.DateUtils;
import com.example.klinelib.util.DoubleUtil;

import java.util.Locale;

/**
 * 分时图点击的信息
 * Created by Administrator on 2017/9/25.
 */

public class LineChartInfoView extends ChartInfoView {

    private TextView mTvPrice;
    private TextView mTvChangeRate;
    private TextView mTvVol;
    private TextView mTvTime;

    public LineChartInfoView(Context context) {
        this(context, null);
    }

    public LineChartInfoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_line_chart_info, this);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        mTvChangeRate = (TextView) findViewById(R.id.tv_change_rate);
        mTvVol = (TextView) findViewById(R.id.tv_vol);
    }

    @Override
    public void setData(double lastClose, HisData data) {
        mTvTime.setText(DateUtils.formatTime(data.getDate()));
        mTvPrice.setText(DoubleUtil.formatDecimal(data.getClose()));
//        mTvChangeRate.setText(String.format(Locale.getDefault(), "%.2f%%", (data.getClose()- data.getOpen()) / data.getOpen() * 100));
        mTvChangeRate.setText(String.format(Locale.getDefault(), "%.2f%%", (data.getClose() - lastClose) / lastClose * 100));
        mTvVol.setText(data.getVol() + "");
        removeCallbacks(mRunnable);
        postDelayed(mRunnable, 2000);
    }

}
