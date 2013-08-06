package net.virtalab.android.geolib;

import android.location.Location;

/**
 * Bean that holds location
 * <p/>
 */
public class GeoBean {
    private static GeoBean self = null;

    private Location location;

    public static GeoBean getBean(){
        if(self==null){
            self = new GeoBean();
        }
        return self;
    }
    private GeoBean(){}

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
}
