package com.dh.common.map;


public class MapUtils {

	private final static double EARTH_RADIUS = 6378.137; // 地球半径

	private static double rad(double d) {
		return d * Math.PI / 180.0; // 计算弧长
	}

	// lng1 第一个点经度，lat1第一点纬度；lng2第二点经度，lat2第二点纬度
	public static double getShortestDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
//		s = Math.round(s * 10000) / 10000;
		// s = s * 1000; //换算成米
		return s; // 得到千米数

	}

	public static void main(String[] args) {
		double lng1 = 116.481028;
		double lat1 = 39.989643;
		double lng2 = 114.481028;
		double lat2 = 39.989643;

		double q = getShortestDistance(lng1, lat1, lng2, lat2);
		System.out.println(q);

	}
}
