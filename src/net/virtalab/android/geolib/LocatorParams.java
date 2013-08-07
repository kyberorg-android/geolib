package net.virtalab.android.geolib;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Object with parameters passed to Locator methods
 * <p/>
 */
public class LocatorParams {
    private LocationManager lm =null;
    private String provider = null;

    /**
     * Builder for LocatorParams
     */
    public static class Builder {
        //Compulsory params
        private String provider = null;

        //Optional params - init with defaults
        private LocationManager lm = (LocationManager) GeoApp.getContext().getSystemService(Context.LOCATION_SERVICE);

        /**
         * Constructor with compulsory params
         * @param provider string with provider
         */
        public Builder(String provider){
            this.provider = provider;
        }

        /**
         * Allows setting location manager
         * @param lm custom location manager
         * @return builder object
         */
        public Builder locationManager(LocationManager lm){
            this.lm = lm;
            return this;
        }

        /**
         * Triggers build
         * @return LocatorParams object
         */
        public LocatorParams build(){
            return new LocatorParams(this);
        }
    }

    /**
     * Constructs object from builder
     * @param builder builder
     */
    private LocatorParams(Builder builder){
        lm = builder.lm;
        provider = builder.provider;
    }

    //Getters

    /**
     * Returns LocationManager (custom or gotten from context)
     * @return LocationManager
     */
    LocationManager getLocationManager(){
        return this.lm;
    }

    /**
     * Returns string with location provider
     * @return string with provider
     */
    String getProvider(){
        return this.provider;
    }
}
