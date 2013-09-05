package net.virtalab.android.geolib;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import net.virtalab.android.geolib.exception.LocatorException;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Class contains method to work with Location in Android.
 * <p/>
 * Also it provides location string (and error string) generator methods
 */
public class Locator {

    private LocationManager lm;
    private String provider;
    private Context ctx;

    /**
     * Returns locator
     *
     * @param params Parameter object built with Locator.Builder
     * @return  Locator object
     */
    public static Locator getLocator(LocatorParams params){
        if(params == null){ return null; }
        return new Locator(params);
    }

    /**
     *  Constructor
     *
     * @param params Parameter object built with Locator.Builder
     */
    public Locator(LocatorParams params){
        this.lm = params.getLocationManager();
        this.provider = params.getProvider();
        this.ctx = params.getContext();
    }

    /**
     * Tries to request Location from LocationManager
     *
     * @return Location object or null when not found
     */
    public Location findLocation() throws LocatorException {

        if(lm==null){
            throw generateLocatorException(Failure.LOCATION_SERVICE_NOT_AVAILABLE);
        }

        if( (provider==null) || (!isProviderValid(provider)) ){
            throw generateLocatorException(Failure.PROVIDER_IS_NOT_VALID);
        }

        return lm.getLastKnownLocation(provider);
    }

    /**
     * Generates Location string which you can use is your application
     *
     * @param location valid Location object
     * @return Location String when location is ok. Error string is passed location is null.
     */
    public String getLocationString(Location location){
        if(location == null){ return this.getErrorString(Failure.LOCATION_IS_NULL); }
        Number lat = Locator.formatCoordinate(location.getLatitude());
        Number lng = Locator.formatCoordinate(location.getLongitude());

        Resources r = ctx.getResources();
        return String.format(r.getString(R.string.geolib_locator_location_string),lat,lng);
    }

    /**
     * Generates Error String which depends on errorCode. You can use is your application
     *
     * @param replyCode valid replyCode. See Locator constants
     * @return Error String
     */
    public String getErrorString(Failure replyCode){
        Resources r = this.ctx.getResources();
        String errorString;
        switch (replyCode){
            case LOCATOR_PARAMS_IS_NULL:
               errorString = r.getString(R.string.geolib_locator_error_locator_params_null);
               break;
            case PROVIDER_IS_NOT_VALID:
                errorString = r.getString(R.string.geolib_locator_error_provider_not_valid);
                break;
            case LOCATION_SERVICE_NOT_AVAILABLE:
                errorString = r.getString(R.string.geolib_locator_error_location_service_na);
                break;
            case LOCATION_IS_NULL:
                errorString = r.getString(R.string.geolib_locator_error_location_null);
                break;
            default:
                errorString = r.getString(R.string.geolib_locator_error_unknown_error);
                break;
        }
        return errorString;
    }

    /**
     * Checks if provider string contains valid provider defined at LocationManager class
     * @param provider unchecked string with provider
     * @return test result
     */
    private static boolean isProviderValid(String provider){
        boolean isGPSProvider = provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER);
        boolean isNetworkProvider = provider.equalsIgnoreCase(LocationManager.NETWORK_PROVIDER);
        boolean isPassiveProvider = provider.equalsIgnoreCase(LocationManager.PASSIVE_PROVIDER);

        return (isGPSProvider || isNetworkProvider || isPassiveProvider);
    }

    /**
     * Formats given coordinate with default format (NN.NNNN)
     *
     * @param coordinate unformatted coordinate
     * @return formatted coordinate or origin coordinate if parse exception occurs
     */
    public static Number formatCoordinate(double coordinate){
        DecimalFormat df = new DecimalFormat("##.####");
        return formatCoordinate(coordinate,df);
    }

    /**
     * Formats given coordinate with given format
     * @param coordinate unformatted coordinate
     * @param df format
     * @return formatted coordinate or origin coordinate if parse exception occurs
     */
    public static Number formatCoordinate(double coordinate, DecimalFormat df){
        String format = df.format(coordinate);
        try{
            return df.parse(format);
        }catch (ParseException pe){
            return coordinate;
        }
    }

    private LocatorException generateLocatorException(Failure failure){
          String message = this.getErrorString(failure);
           return new LocatorException(failure,message);
    }
    public enum Failure {
        /**
         * Result which indicates that parameter passed to method is NULL
         */
        LOCATOR_PARAMS_IS_NULL,
        /**
         * Result which indicates that given provider string contains value out of providers scope which can be found at LocationManager class doc
         */
        PROVIDER_IS_NOT_VALID,
        /**
         * Result which indicates that location service is not available at the device
         */
        LOCATION_SERVICE_NOT_AVAILABLE,
        /**
         * Result which indicates that location param passed to method is NULL
         */
        LOCATION_IS_NULL;

    }
}
