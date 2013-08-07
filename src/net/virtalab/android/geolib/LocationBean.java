package net.virtalab.android.geolib;

import android.location.Location;

/**
 * Bean that holds replyCode and Location object
 * <p/>
 */
public class LocationBean {

    private int replyCode;
    private Location location = null;

    /**
     * Default constructor when no error occured and location object is ready to use
     *
     * @param replyCode should be Locator.OK
     * @param location ready to use Location object
     */
    LocationBean(int replyCode,Location location){
         this.replyCode = replyCode;
         this.location = location;
    }

    /**
     * Constructor when error occured
     * @param replyCode error Status. See Locator constants for more.
     */
    LocationBean(int replyCode){
         this.replyCode = replyCode;
    }

    /**
     * Returns location obeject
     * @return ready to use location object or null if object is null
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Returns replyCode. You should normally check it first before retrieve Location object
     * @return one of Locator constants. If replyCode is Locator.OK, Location object present and ready to use
     */
    public int getReplyCode() {
        return this.replyCode;
    }

}
