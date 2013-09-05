package net.virtalab.android.geolib;

/**
 * Validator for coordinates
 * <p/>
 */
public class LocationValidator {

    public static final double LAT_MIN = -90;
    public static final double LAT_MAX = 90;

    public static final double LNG_MIN = -180;
    public static final double LNG_MAX = 180;

    /**
     * Latitude validator for String representation
     *
     * @param lat latitude as String
     * @return validation result
     */
    public static Result validateLatitude(String lat){
        double c;
        try{
           c = Double.parseDouble(lat);
        }catch (Exception e){
           return Result.UNPARSEABLE;
        }
        return validateLatitude(c);
    }

    /**
     * Latitude validator
     *
     * @param lat latitude
     * @return validation result
     */
    public static Result validateLatitude(double lat){
        return  (lat >= LAT_MIN && lat <= LAT_MAX) ? Result.PASS : Result.OUT_OF_RANGE;
    }

    /**
     * Longitude validator for String representation
     * @param lon longitude
     * @return validation result
     */
    public static Result validateLongitude(String lon){
        double c;
        try{
            c = Double.parseDouble(lon);
        }catch (Exception e){
            return Result.UNPARSEABLE;
        }
        return validateLongitude(c);
    }

    /**
     * Longitude validator
     * @param lng longitude
     * @return validation result
     */
    public static Result validateLongitude(double lng){
        return (lng >= LNG_MIN && lng <= LNG_MAX) ? Result.PASS : Result.OUT_OF_RANGE;
    }

    //reply codes
    public enum Result {
        /**
         * Result which indicates that validation is passed successfully
         */
        PASS,
        /**
         * If you validate String this status which indicates that provided String cannot be parsed to double for further validation
         */
        UNPARSEABLE,
        /**
         * Result which indicates that coordinate is out of possible range
         */
        OUT_OF_RANGE;
        }
}
