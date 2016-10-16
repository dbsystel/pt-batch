package org.hisrc.ptbatch.util;

public class LonLatUtils {

    public static final double EQUATORIAL_EARTH_RADIUS_IN_M = 6378137d;
    private static final double RADIANS_IN_DEGREE = Math.PI / 180d;
    
    private LonLatUtils() {
        
    }

    public static double distance(double lon0, double lat0, double lon1, double lat1) {
        final double dlat = (lat1 - lat0) * RADIANS_IN_DEGREE;
        final double dlon = (lon1 - lon0) * RADIANS_IN_DEGREE;
        final double a = Math.pow(Math.sin(dlat / 2d), 2d) + Math.cos(lat0 * RADIANS_IN_DEGREE) * Math.cos(lat1 * RADIANS_IN_DEGREE)
                        * Math.pow(Math.sin(dlon / 2d), 2d);
        final double c = 2d * Math.atan2(Math.sqrt(a), Math.sqrt(1d - a));
        return EQUATORIAL_EARTH_RADIUS_IN_M * c;
    }
}