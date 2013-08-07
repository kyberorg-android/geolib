package net.virtalab.android.geolib;

import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Class contains method to work with Location in Android.
 * <p/>
 * Also it provides location string (and error string) generator methods
 */
public class Locator {
    /**
     * Status which indicates that everything worked fine and no error occured
     */
    public static final int OK = 0;
    /**
     * Status which indicates that parameter passed to method is NULL
     */
    public static final int LOCATOR_PARAMS_IS_NULL = 2;
    /**
     * Status which indicates that given provider string is NULL
     */
    public static final int PROVIDER_IS_NULL = 33;
    /**
     * Status which indicates that given provider string contains value out of providers scope which can be found at LocationManager class doc
     */
    public static final int PROVIDER_IS_NOT_VALID = 34;
    /**
     * Status which indicates that location service is not available at the device
     */
    public static final int LOCATION_SERVICE_NOT_AVAILABLE = 35;
    /**
     * Status which indicates that location was not found
     */
    public static final int LOCATION_IS_NOT_FOUND = 36;
    /**
     * Status which indicates that location param passed to method is NULL
     */
    public static final int LOCATION_IS_NULL = 37;

    /**
     * Tries to request Location from LocationManager
     *
     * @param params Parameter object built with Locator.Builder
     * @return LocationBean with replyCode and Location object. Is replyCode is not Locator.OK - no Location object present.
     */
    public static LocationBean findLocation(LocatorParams params){

        if(params == null){ return new LocationBean(LOCATOR_PARAMS_IS_NULL); }

        LocationManager lm = params.getLocationManager();
        String provider = params.getProvider();

        if(lm==null){ return new LocationBean(LOCATION_SERVICE_NOT_AVAILABLE); }
        if(provider==null){ return new LocationBean(PROVIDER_IS_NULL); }
        if(!isProviderValid(provider)){ return  new LocationBean(PROVIDER_IS_NOT_VALID); }

        Location location = lm.getLastKnownLocation(provider);
        if(location==null) { return new LocationBean(LOCATION_IS_NOT_FOUND); }

        return new LocationBean(OK,location);
    }

    /**
     * Generates Location string which you can use is your application
     *
     * @param bean valid locationBean
     * @return location string (or error string if status is not Locator.OK) or null if passed locationBean is null
     */
    public static String getLocationString(LocationBean bean){
        if(bean == null){ return null; }

        if(bean.getReplyCode()!=OK){
            return Locator.getErrorString(bean.getReplyCode());
        }
        return Locator.getLocationString(bean.getLocation());
    }

    /**
     * Generates Location string which you can use is your application
     *
     * @param location valid Location object
     * @return Location String when location is ok. Error string is passed location is null.
     */
    public static String getLocationString(Location location){
        if(location == null){ return Locator.getErrorString(LOCATION_IS_NULL); }
        Number lat = Locator.formatCoordinate(location.getLatitude());
        Number lng = Locator.formatCoordinate(location.getLongitude());

        Resources r = GeoApp.getContext().getResources();
        return String.format(r.getString(R.string.geolib_locator_location_string),lat,lng);
    }

    /**
     * Generates Error String which depends on errorCode. You can use is your application
     *
     * @param bean Bean with replyCode
     * @return Error String or null if bean is null
     */
    public static String getErrorString(LocationBean bean){
        if(bean==null){ return null; }
        return getErrorString(bean.getReplyCode());
    }

    /**
     * Generates Error String which depends on errorCode. You can use is your application
     *
     * @param replyCode valid replyCode. See Locator constants
     * @return Error String
     */
    public static String getErrorString(int replyCode){
        Resources r = GeoApp.getContext().getResources();
        String errorString;
        switch (replyCode){
            case LOCATOR_PARAMS_IS_NULL:
               errorString = r.getString(R.string.geolib_locator_error_locator_params_null);
               break;
            case PROVIDER_IS_NULL:
                errorString = r.getString(R.string.geolib_locator_error_provider_null);
                break;
            case PROVIDER_IS_NOT_VALID:
                errorString = r.getString(R.string.geolib_locator_error_provider_not_valid);
                break;
            case LOCATION_SERVICE_NOT_AVAILABLE:
                errorString = r.getString(R.string.geolib_locator_error_location_service_na);
                break;
            case LOCATION_IS_NOT_FOUND:
                errorString = r.getString(R.string.geolib_locator_error_no_location_found);
                break;
            case LOCATION_IS_NULL:
                errorString = r.getString(R.string.geolib_locator_error_location_null);
                break;
            case OK:
                errorString = "";
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
}
