package com.example.administrator.mykline.model;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class OhlcvInfoBean {

    /**
     * time : 2017-08-02
     * price_o : 10526.00
     * price_c : 10469.34
     * price_h : 10557.47
     * price_l : 10461.95
     * volume : 248511557
     * volume_price : 3051亿
     * zf_bfb : 0.91%
     */
    //周期
    public String p;

    //时间戳
    public String t;

    //开盘价
    public double o;

    // 最高价
    public double h;

    //收盘价
    public double c;

    //最低价
    public double l;

    //成交量
    public double v;

    private String volume_price;
    private String zf_bfb;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public double getO() {
        return o;
    }

    public void setO(double o) {
        this.o = o;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public String getVolume_price() {
        return volume_price;
    }

    public void setVolume_price(String volume_price) {
        this.volume_price = volume_price;
    }

    public String getZf_bfb() {
        return zf_bfb;
    }

    public void setZf_bfb(String zf_bfb) {
        this.zf_bfb = zf_bfb;
    }
}
