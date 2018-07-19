package com.xiaowei.commonjts.utils;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.xiaowei.commonjts.bean.Gps;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanxuan on 2017/10/11.
 */
public class GeometryUtil {
    private static GeometryFactory geometryFactory;

    static {
        if (geometryFactory == null) {
            geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        }
    }

    public static GeometryFactory getGeometryFactory() {
        if (geometryFactory == null) {
            geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        }
        return geometryFactory;
    }

    /**
     * 把面转换成Gps集合
     *
     * @return
     */
    public static List<Gps> transGeometry(Geometry geometry) {
        //判断geometry是否为空
        if (geometry == null) {
            return null;
        }
        List<Gps> gpsList = new ArrayList<>();
        //得到点对象的数组
        Coordinate[] coordinates = geometry.getCoordinates();
        for (int i = 0; i < coordinates.length; i++) {
            //转换成Gps
            gpsList.add(new Gps(coordinates[i].y, coordinates[i].x));
        }
        return gpsList;
    }

    /**
     * 把点字符串数组转换成面对象
     *
     * @param shapea:格式 "1.11,2.22"
     * @return
     */
    public static Polygon getPolygon(String[] shapea) {
        if (shapea == null || shapea.length < 4) {
            return null;
        }
        //判断是否首尾点一样
        if (!shapea[0].equals(shapea[shapea.length - 1])) {
            return null;
        }
        Coordinate[] coordinates = getCoordinates(shapea);
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        return polygon;
    }

    /**
     * 把Gps集合转换成面对象
     *
     * @param gpsList
     * @return
     */
    public static Polygon getPolygon(List<Gps> gpsList) {
        if (gpsList == null || gpsList.size() < 4) {
            return null;
        }
        //判断是否首尾点一样
        if (!gpsList.get(0).equals(gpsList.get(gpsList.size() - 1))) {
            return null;
        }
        Coordinate[] coordinates = getCoordinatesByGps(gpsList);
        Polygon polygon = geometryFactory.createPolygon(coordinates);
        return polygon;
    }

    /**
     * 把点字符串数组转换成多点对象
     *
     * @param shapea
     * @return
     */
    public static MultiPoint getMultiPoint(String[] shapea) {
        if (shapea == null || shapea.length < 1) {
            return null;
        }
        Coordinate[] coordinates = getCoordinates(shapea);
        MultiPoint multiPoint = geometryFactory.createMultiPoint(coordinates);
        return multiPoint;
    }

    /**
     * 把Gps集合转换成多点对象
     *
     * @param gpsList
     * @return
     */
    public static MultiPoint getMultiPoint(List<Gps> gpsList) {
        if (gpsList == null || gpsList.size() < 1) {
            return null;
        }
        Coordinate[] coordinates = getCoordinatesByGps(gpsList);
        MultiPoint multiPoint = geometryFactory.createMultiPoint(coordinates);
        return multiPoint;
    }

    /**
     * 点的字符串转换为点对象
     *
     * @param shapea 格式"1.11,2.22"
     * @return
     */
    public static Point getPoint(String shapea) {
        //x,y
        String[] split = shapea.split(",");
        Coordinate coordinate = new Coordinate(Double.parseDouble(split[0]),
                Double.parseDouble(split[1]));
        Point point = geometryFactory.createPoint(coordinate);
        return point;
    }

    /**
     * Gps转换为点对象
     *
     * @param gps
     * @return
     */
    public static Point getPoint(Gps gps) {
        //x,y
        Coordinate coordinate = new Coordinate(gps.getWgLon(), gps.getWgLat());
        Point point = geometryFactory.createPoint(coordinate);
        return point;
    }

    /**
     * 点对象转换成gps
     * @param point
     * @return
     */
    public static Gps getGps(Point point) {
        Coordinate coordinate = point.getCoordinate();
        return new Gps(coordinate.y,coordinate.x);
    }


    /**
     * 把点的字符串数组转换成点对象数组
     *
     * @param shapea
     * @return
     */
    public static Coordinate[] getCoordinates(String[] shapea) {
        if (shapea == null || shapea.length == 0) {
            return null;
        }
        Coordinate[] coordinates = new Coordinate[shapea.length];
        for (int i = 0; i < shapea.length; i++) {
            String doubles = shapea[i];
            //x,y
            String[] split = doubles.split(",");
            Coordinate coordinate = new Coordinate(Double.parseDouble(split[0]),
                    Double.parseDouble(split[1]));
            //把坐标对象加入数组
            coordinates[i] = coordinate;
        }
        return coordinates;
    }

    /**
     * 把Gps集合转换成点对象数组
     *
     * @param gpsList
     * @return
     */
    public static Coordinate[] getCoordinatesByGps(List<Gps> gpsList) {
        if (gpsList == null || gpsList.size() == 0) {
            return null;
        }
        Coordinate[] coordinates = new Coordinate[gpsList.size()];
        for (int i = 0; i < gpsList.size(); i++) {
            Gps gps = gpsList.get(i);
            //x,y
            Coordinate coordinate = new Coordinate(gps.getWgLon(), gps.getWgLat());
            //把坐标对象加入数组
            coordinates[i] = coordinate;
        }
        return coordinates;
    }

    /**
     * 多面的点的字符串二位数组转换成多面对象
     *
     * @param multiShapea
     * @return
     */
    public static MultiPolygon getMultiPolygon(List<String[]> multiShapea) {
        Polygon[] polygons = new Polygon[multiShapea.size()];
        for (int i = 0; i < multiShapea.size(); i++) {
            String[] shapea = multiShapea.get(i);
            //面对象
            Polygon polygon = getPolygon(shapea);
            //如果有一个面对象为空,判定多面对象为空
            if (polygon == null) {
                return null;
            } else {
                polygons[i] = polygon;
            }
        }
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygons);
        return multiPolygon;
    }

    /**
     * 将WKT字符串转换成geometry
     *
     * @param wkt
     * @return
     * @throws ParseException
     */
    public static Geometry transWKT(String wkt) throws ParseException {
        Geometry geometry = null;
        if(StringUtils.isEmpty(wkt)){
            return geometry;
        }
        if (wkt != null && !"".equals(wkt)) {
            WKTReader wktReader = new WKTReader();
            geometry = wktReader.read(wkt);
            geometry.setSRID(4326);
        }
        return geometry;
    }

    /**
     * 把多面对象转换成面对象的数组
     *
     * @param multiPolygon
     * @return
     */
    public static Polygon[] transMultiPolygon(MultiPolygon multiPolygon) {
        if (multiPolygon == null) {
            return null;
        }
        //获取面的数量
        int numGeometries = multiPolygon.getNumGeometries();
        Polygon[] polygons = new Polygon[numGeometries];
        for (int i = 0; i < numGeometries; i++) {
            polygons[i] = (Polygon) multiPolygon.getGeometryN(i);
        }
        return polygons;
    }

    /**
     * 把Gps转换成点字符串数组
     *
     * @param gps
     * @return
     */
    public static String[] transGpsToArr(Gps gps) {
        if (gps == null) {
            return null;
        }
        return new String[]{gps.getWgLon() + "", gps.getWgLat() + ""};
    }

    /**
     * 把Gps集合转换成点字符串二维数组
     *
     * @param gpsList
     * @return
     */
    public static String[][] transGpsListToArrArr(List<Gps> gpsList) {
        if (gpsList == null || gpsList.size() == 0) {
            return null;
        }
        //点字符串二维数组
        String[][] shapeaa = new String[gpsList.size()][2];
        for (int i = 0; i < gpsList.size(); i++) {
            Gps gps = gpsList.get(i);
            //设置单点
            shapeaa[i] = transGpsToArr(gps);
        }
        return shapeaa;
    }

}
