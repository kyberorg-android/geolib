package net.virtalab.android.geolib.exception;

import net.virtalab.android.geolib.Locator;

/**
 * Locator-specific exception
 *
 * Created at: 9/5/13
 */
public class LocatorException extends Exception {

    private Locator.Failure reason;

    public LocatorException(Locator.Failure reason,String message){
           super(message);
           this.reason =reason;
    }

    public Locator.Failure getReason(){
        return this.reason;
    }
}
