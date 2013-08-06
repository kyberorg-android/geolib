package net.virtalab.android.geolib;

import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Geolocation operations
 * <p/>
 */
public class Geo {
    /**
     * Class that works with GeoLocation
     */
    public static class Locator{
        /**
         * Constant used as return code when location found and stored to GeoBean
         */
        public static final int OK=0;
        /**
         * Return code used when location wasn't found (ie no network)
         */
        public static final int LOCATION_NOT_FOUND = -1;
        /**
         * Return code used when at least one param passed is Null
         */
        public static final int NULL_PARAMS_DETECTED = -2;

        /**
         * Finds latest known location and saves it to GeoBean
         * @param lm LocationManager object
         * @param provider Location provider
         * @param ll Location Listener
         * @return reply status
         */
        public static int findLocation(LocationManager lm,String provider,LocationListener ll){
            if(lm==null || provider==null || ll == null){ return NULL_PARAMS_DETECTED; }

            Location location = lm.getLastKnownLocation(provider);
            if(location == null){ return LOCATION_NOT_FOUND; }

            GeoBean geoBean = GeoBean.getBean();
            geoBean.setLocation(location);
            return OK;
        }

        /**
         * Builds String with location (coordinates)
         * @param location Location object
         * @return string with location
         */
        public static String getLocationString(Location location){
            if(location==null) { return Locator.getLocationErrorString(LOCATION_NOT_FOUND); }

            Resources r = GeoApp.getContext().getResources();
            Number lat = Locator.formatCoordinates(location.getLatitude());
            Number lng = Locator.formatCoordinates(location.getLongitude());

            return String.format(r.getString(R.string.geo_location_string),lat,lng);
        }

        /**
         * Builds Error String based on replyCode provided
         * @param replyCode error code received from findLocation()
         * @return Human readable error string
         */
        public static String getLocationErrorString(int replyCode){
            Resources r = GeoApp.getContext().getResources();
            String errorString;
            switch (replyCode){
                case LOCATION_NOT_FOUND:
                    errorString = r.getString(R.string.geo_no_location_found);
                    break;
                case NULL_PARAMS_DETECTED:
                    errorString = r.getString(R.string.geo_null_params_passed);
                    break;
                default:
                    errorString = "";
            }
            return errorString;
        }

        /**
         * Formats coordinates
         * @param coordinate unformatted coordinate
         * @return formatted number (2 digits+delimeter+4 digits)
         */
        private static Number formatCoordinates(double coordinate){
            DecimalFormat df = new DecimalFormat("##.####");
            String format = df.format(coordinate);
            try{
               return df.parse(format);
            }catch (ParseException pe){
               return coordinate;
            }
        }
    }
}
