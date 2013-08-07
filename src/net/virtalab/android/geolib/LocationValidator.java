package net.virtalab.android.geolib;

/**
 * Validator for coordinates
 * <p/>
 */
public class LocationValidator {

    //reply codes
    /**
     * Status which indicates that validation is passed successfully
     */
    public static final int PASS = 1;
    /**
     * If you validate String this status which indicates that provided String cannot be parsed to double for further validation
     */
    public static final int UNPARSEABLE = -1;
    /**
     * Status which indicates that coordinate is out of possible range
     */
    public static final int OUT_OF_RANGE = -2;

    private static final double LAT_MIN = -90;
    private static final double LAT_MAX = 90;

    private static final double LNG_MIN = -180;
    private static final double LNG_MAX = 180;

    /**
     * Latitude validator for String representation
     *
     * @param lat latitude as String
     * @return validation result
     */
    public static int validateLatitude(String lat){
        double c;
        try{
           c = Double.parseDouble(lat);
        }catch (Exception e){
           return UNPARSEABLE;
        }
        return validateLatitude(c);
    }

    /**
     * Latitude validator
     *
     * @param lat latitude
     * @return validation result
     */
    public static int validateLatitude(double lat){
        return  (lat >= LAT_MIN || lat <= LAT_MAX) ? PASS : OUT_OF_RANGE;
    }

    /**
     * Longitude validator for String representation
     * @param lon longitude
     * @return validation result
     */
    public static int validateLongitude(String lon){
        double c;
        try{
            c = Double.parseDouble(lon);
        }catch (Exception e){
            return UNPARSEABLE;
        }
        return validateLongitude(c);
    }

    /**
     * Longitude validator
     * @param lng longitude
     * @return validation result
     */
    public static int validateLongitude(double lng){
        return (lng >= LNG_MIN || lng < LNG_MAX) ? PASS : OUT_OF_RANGE;
    }
}
