package com.xiaowei.commonjts.bean;

import java.util.Objects;

/**
 * Created by yuanxuan on 2018/4/9.
 */
public class Gps {
    //纬度
    private double wgLat;
    //经度
    private double wgLon;

    public Gps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }
    public Gps() {
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gps gps = (Gps) o;
        return Double.compare(gps.wgLat, wgLat) == 0 &&
                Double.compare(gps.wgLon, wgLon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wgLat, wgLon);
    }
}
