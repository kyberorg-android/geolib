package net.virtalab.android.geolib;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import net.virtalab.android.geolib.exception.AddressDecoderException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Decodes address from provided location
 * <p/>
 */
public class AddressDecoder {

    /**
     * Location obejct to decode
     */
    private Location location;

    /**
     * Application context
     */
    private Context ctx;

    /**
     * Address locale
     */
    private Locale locale;

    /**
     * Number of records to show
     */
    private int limit;

    /**
     * Constuctor with params replaces default constructor
     * @param params
     */
    private AddressDecoder(AddressDecoderParams params){
        //extract params
        this.location = params.getLocation();
        this.ctx = params.getContext();
        this.locale = params.getLocale();
        this.limit = params.getLimit();
    }

    /**
     * Static method which returns address decoder object
     *
     * @param params Parameter object built with AddressDecoderParams.Builder
     * @return
     */
    public static  AddressDecoder getDecoder(AddressDecoderParams params){
        return new AddressDecoder(params);
    }

    /**
     * Decodes address from location as String
     *
     * @return String with address (or addresses) or String with error is error occured
     */
    public String decode() throws AddressDecoderException {
        //validation
        if(location == null){ return this.generateErrorString(Status.LOCATION_IS_NULL); }
        //also we validate coordinates
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LocationValidator.Result latValidation = LocationValidator.validateLatitude(lat);
        if(latValidation== LocationValidator.Result.OUT_OF_RANGE){
            return this.generateErrorString(Status.LOCATION_OUT_OF_RANGE);
        }
        if(latValidation== LocationValidator.Result.UNPARSEABLE){
            return  this.generateErrorString(Status.LOCATION_UNPARSEABLE);
        }
        LocationValidator.Result lngValidation = LocationValidator.validateLongitude(lng);
        if(lngValidation == LocationValidator.Result.OUT_OF_RANGE){
            return this.generateErrorString(Status.LOCATION_OUT_OF_RANGE);
        }
        if(lngValidation== LocationValidator.Result.UNPARSEABLE){
            return  this.generateErrorString(Status.LOCATION_UNPARSEABLE);
        }


        if(limit <= 0 ){
            return this.generateErrorString(Status.LIMIT_IS_NOT_VALID);
        }
        //decode
        List<Address> addresses = decode0();

        if(addresses==null){
            return this.generateErrorString(Status.NO_ADDRESSES_FOUND);
        }
        if(addresses.size()==0){
            return this.generateErrorString(Status.NO_ADDRESSES_FOUND);
        }
        //address found!
        StringBuilder sb = new StringBuilder();
        Resources r = ctx.getResources();
        for (int i = 0; i < addresses.size() ; i++) {
            Address a = addresses.get(i);

            if(addresses.size()!=1){
                //no reason to show number if only one result found
                sb.append(AddressDecoder.printLn(String.format(r.getString(R.string.geolib_adecoder_address_n_title),i+1))); //Address N string
            } else {
                sb.append(AddressDecoder.printLn("")); //just new line
            }

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
     * @return list with resolved addresses
     */
    private List<Address> decode0() throws AddressDecoderException {
        if(ctx==null){
            return null;
        }
        List<Address> addresses = null;
        try{
            Geocoder geoCoder = new Geocoder(ctx,locale);
            addresses = geoCoder.getFromLocation(location.getLatitude(),location.getLongitude(),limit);
        }catch (IllegalArgumentException iae){
            //report and exit
            String message = generateErrorString(Status.LOCATION_OUT_OF_RANGE);
            throw new AddressDecoderException(Status.LOCATION_OUT_OF_RANGE,message);
        }catch (IOException ioe){
            //report and exit
            String message = generateErrorString(Status.SERVICE_IS_NA);
            throw new AddressDecoderException(Status.SERVICE_IS_NA,message);
        }catch (NullPointerException npe){
            return null;
        }

        return addresses;
    }

    /**
     * Generates error String depends on errorCode
     *
     * @param errorCode one of class constants
     * @return String with error message
     */
    private String generateErrorString(Status errorCode){
        Resources r = this.ctx.getResources();
        String errorString;
        switch (errorCode){
            case LOCATION_IS_NULL:
                errorString = r.getString(R.string.geolib_adecoder_error_location_null);
                break;
            case LIMIT_IS_NOT_VALID:
                errorString = r.getString(R.string.geolib_adecoder_error_limit_not_valid);
                break;
            case LOCATION_OUT_OF_RANGE:
                errorString = r.getString(R.string.geolib_adecoder_error_location_out_of_range);
                break;
            case LOCATION_UNPARSEABLE:
                errorString = r.getString(R.string.geolib_adecoder_error_location_unparseable);
                break;
            case SERVICE_IS_NA:
                errorString = r.getString(R.string.geolib_adecoder_error_service_na);
                break;
            case NO_ADDRESSES_FOUND:
                errorString = r.getString(R.string.geolib_adecoder_error_address_not_found);
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

    public enum Status{
        /**
         * Result which indicates that location object passed is NULL
         */
        LOCATION_IS_NULL,
        /**
         * Result which indicates that limit set is not valid (less that 0)
         */
        LIMIT_IS_NOT_VALID,
        /**
         * Result which indicates that coordinates inside location object are out of possible values (Refer to GeoCoder class for more)
         */
        LOCATION_OUT_OF_RANGE,
        /**
         * Result which indicates that location passed is not valid (ie unparseable)
         */
        LOCATION_UNPARSEABLE,
        /**
         * Result which indicates that resolving service is not available
         */
        SERVICE_IS_NA,
        /**
         * Result which indicates that no addresses found
         */
        NO_ADDRESSES_FOUND;
    }
}
