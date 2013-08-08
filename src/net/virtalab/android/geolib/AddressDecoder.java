package net.virtalab.android.geolib;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Decodes address from provided location
 * <p/>
 */
public class AddressDecoder {
    /**
     * Status which indicates that everything worked fine and no error occured
     */
    public static final int OK = 0;
    /**
     * Status which indicates that parameter passed to method is NULL
     */
    public static final int PARAMS_IS_NULL = 2;
    /**
     * Status which indicates that location object passed is NULL
     */
    public static final int LOCATION_IS_NULL = 33;
    /**
     * Status which indicates that limit set is not valid (less that 0)
     */
    public static final int LIMIT_IS_NOT_VALID = 34;
    /**
     * Status which indicates that coordinates inside location object are out of possible values (Refer to GeoCoder class for more)
     */
    public static final int LOCATION_OUT_OF_RANGE = 35;
    /**
     * Status which indicates that resolving service is not available
     */
    public static final int SERVICE_IS_NA = 36;
    /**
     * Status which indicates that Address is not found
     */
    public static final int ADDRESS_NOT_FOUND = 37;

    /**
     * Status of decoding address
     */
    private static int DECODE_STATUS;

    /**
     * Decodes address from location as String
     *
     * @param params Parameter object built with AddressDecoderParams.Builder
     * @return String with address (or addresses) or String with error is error occured
     */
    public static String decodeAddressToString(AddressDecoderParams params){
        if(params==null){
            return AddressDecoder.generateErrorString(PARAMS_IS_NULL);
        }
        //extract params
        Location location = params.getLocation();
        Locale locale = params.getLocale();
        int limit = params.getLimit();

        //validation
        if(location == null){ return AddressDecoder.generateErrorString(LOCATION_IS_NULL); }
        //also we validate coordinates
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        int latValidation = LocationValidator.validateLatitude(lat);
        if(latValidation==LocationValidator.OUT_OF_RANGE){
            return AddressDecoder.generateErrorString(LOCATION_OUT_OF_RANGE);
        }
        int lngValidation = LocationValidator.validateLongitude(lng);
        if(lngValidation == LocationValidator.OUT_OF_RANGE){
            return AddressDecoder.generateErrorString(LOCATION_OUT_OF_RANGE);
        }

        if(limit <= 0 ){
            return AddressDecoder.generateErrorString(LIMIT_IS_NOT_VALID);
        }
        //decode
        List<Address> addresses = decode(location,locale,limit);
        //check decode status
        if(DECODE_STATUS!=OK){
           return AddressDecoder.generateErrorString(DECODE_STATUS);
        }
        if(addresses==null){
            return AddressDecoder.generateErrorString(ADDRESS_NOT_FOUND);
        }
        if(addresses.size()==0){
            return AddressDecoder.generateErrorString(ADDRESS_NOT_FOUND);
        }
        //address found!
        StringBuilder sb = new StringBuilder();
        Resources r = GeoApp.getContext().getResources();
        for (int i = 0; i < addresses.size() ; i++) {
            Address a = addresses.get(i);
            sb.append(AddressDecoder.printLn(String.format(r.getString(R.string.geolib_adecoder_address_n_title),i+1))); //Address N string
            sb.append(AddressDecoder.printLn(a.getAddressLine(i)));
            sb.append(AddressDecoder.printLn(a.getLocality()));
            sb.append(AddressDecoder.printLn(a.getPostalCode()));
            sb.append(AddressDecoder.printLn(a.getCountryName()));
        }
        return sb.toString();
    }

    /**
     * Decoder
     *
     * @param location valid location object
     * @param locale valid locale object
     * @param limit how many records to display
     * @return list with resolved addresses
     */
    private static List<Address> decode(Location location,Locale locale,int limit){
        Geocoder geoCoder = new Geocoder(GeoApp.getContext(),locale);
        List<Address> addresses = null;
        try{
            addresses = geoCoder.getFromLocation(location.getLatitude(),location.getLongitude(),limit);
        }catch (IllegalArgumentException iae){
            //report and exit
            AddressDecoder.DECODE_STATUS = LOCATION_OUT_OF_RANGE;
            return null;
        }catch (IOException ioe){
            //report and exit
            AddressDecoder.DECODE_STATUS = SERVICE_IS_NA;
        }
        //report and return
        AddressDecoder.DECODE_STATUS = OK;
        return addresses;
    }

    /**
     * Generates error String depends on errorCode
     *
     * @param errorCode one of class constants
     * @return String with error message
     */
    private static String generateErrorString(int errorCode){
        Resources r = GeoApp.getContext().getResources();
        String errorString;
        switch (errorCode){
            case PARAMS_IS_NULL:
                errorString = r.getString(R.string.geolib_adecoder_error_params_null);
                break;
            case LOCATION_IS_NULL:
                errorString = r.getString(R.string.geolib_adecoder_error_location_null);
                break;
            case LIMIT_IS_NOT_VALID:
                errorString = r.getString(R.string.geolib_adecoder_error_limit_not_valid);
                break;
            case LOCATION_OUT_OF_RANGE:
                errorString = r.getString(R.string.geolib_adecoder_error_location_out_of_range);
                break;
            case SERVICE_IS_NA:
                errorString = r.getString(R.string.geolib_adecoder_error_service_na);
                break;
            case ADDRESS_NOT_FOUND:
                errorString = r.getString(R.string.geolib_adecoder_error_address_not_found);
                break;
            case OK:
                errorString = "";
                break;
            default:
                errorString = r.getString(R.string.geolib_adecoder_error_unknown_error);
        }
        return errorString;
    }

    /**
     * Just line + line terminator
     * @param str line without terminator
     * @return line + line terminator
     */
    private static String printLn(String str){
        if(str==null){ return ""; }
        String lt = System.getProperty("line.separator");
        return str + lt;
    }
}
