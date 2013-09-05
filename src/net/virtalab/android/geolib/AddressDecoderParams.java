package net.virtalab.android.geolib;

import android.content.Context;
import android.location.Location;

import java.util.Locale;

/**
 * Object with parameters passed to AddressDecoder methods
 * <p/>
 */
public class AddressDecoderParams {
    private Location location;
    private Locale locale;
    private int limit;
    private Context ctx;

    /**
     * Builder class
     */
    public static class Builder{
        //Compulsory params
        private Location location;
        private Context ctx;

        //Optional params - init with defaults
        private Locale locale = Locale.getDefault();
        private int limit = 1;

        /**
         * Constructor with compulsory params
         *
         * @param location Valid Location object
         * @param ctx Aplication Context
         */
        public Builder(Location location,Context ctx){
            this.location = location;
            this.ctx = ctx;
        }

        /**
         * Set custom locale if you don't want you default locale
         * @param locale custom locate
         * @return Builder object
         */
        public Builder locale(Locale locale){
            this.locale = locale;
            return this;
        }

        /**
         * Allows to set number of addresses to show
         * @param limit Max number of addresses to return. Smaller numbers (1 to 5) are recommended
         * @return Builder object
         */
        public Builder limit(int limit){
            this.limit = limit;
            return this;
        }

        /**
         * Triggers build
         * @return AddressDecoderParam object
         */
        public AddressDecoderParams build(){
            return new AddressDecoderParams(this);
        }
    }
    private AddressDecoderParams(Builder builder){
        this.location = builder.location;
        this.ctx = builder.ctx;
        this.locale = builder.locale;
        this.limit = builder.limit;

    }
    //Getters
    Location getLocation(){
        return this.location;
    }
    Locale getLocale(){
        return this.locale;
    }
    int getLimit(){
        return this.limit;
    }
    Context getContext(){
        return this.ctx;
    }
}
