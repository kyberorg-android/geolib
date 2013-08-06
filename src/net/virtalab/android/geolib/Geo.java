package net.virtalab.android.geolib;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

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
    }
}
