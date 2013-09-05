package net.virtalab.android.geolib.exception;

import net.virtalab.android.geolib.AddressDecoder;

/**
 * Decoder-related exception
 *
 * Created at: 9/5/13
 */
public class AddressDecoderException extends Exception {
    private AddressDecoder.Status status;

    public AddressDecoderException(AddressDecoder.Status reason,String message){
        super(message);
        this.status =reason;
    }

    public AddressDecoder.Status getStatus(){
        return this.status;
    }
}
