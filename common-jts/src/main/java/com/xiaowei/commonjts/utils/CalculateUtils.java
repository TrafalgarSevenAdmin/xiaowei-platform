package com.xiaowei.commonjts.utils;

import com.rundi.common.jts.bean.Gps;

/**
 * Created by yuanxuan on 2018/4/11.
 * 图形测量工具类
 */
public class CalculateUtils {

    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 测量两点间的距离 单位:千米
     * @param gps1
     * @param gps2
     * @return
     */
    public static double GetDistance(Gps gps1, Gps gps2) {
        double a, b, d, sa2, sb2;
        double lat1 = gps1.getWgLat();
        double long1 = gps1.getWgLon();
        double lat2 = gps2.getWgLat();
        double long2 = gps2.getWgLon();
        lat1 = rad(lat1);
        lat2 = rad(lat2);
        a = lat1 - lat2;
        b = rad(long1 - long2);
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * EARTH_RADIUS
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
                * Math.cos(lat2) * sb2 * sb2));
        return d;
    }
}
