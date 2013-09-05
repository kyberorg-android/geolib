package net.virtalab.android.geolib;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Object with parameters passed to Locator methods
 *
 */
public class LocatorParams {
    private LocationManager lm =null;
    private String provider = null;
    private Context ctx = null;

    /**
     * Builder for LocatorParams
     */
    public static class Builder {
        //Compulsory params
        private String provider = null;
        private Context ctx = null;

        //Optional params - init with defaults
        private LocationManager lm = null; //lazy init

        /**
         * Constructor with compulsory params
         * @param provider string with provider
         * @param ctx Application context
         */
        public Builder(String provider,Context ctx){
            this.provider = provider;
            this.ctx = ctx;
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
            this.lm = (LocationManager) this.ctx.getSystemService(Context.LOCATION_SERVICE);
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
        ctx = builder.ctx;
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

    Context getContext(){
        return this.ctx;
    }
}
