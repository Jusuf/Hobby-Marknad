package se.netmine.hobby_marknad;

import com.orm.SugarRecord;

/**
 * Created by jusuf on 2017-10-03.
 */

public class Dealer {
    public String id;
    public String name;
    public String email;
    public String webpage;
    public String street;
    public String postalcode;
    public String city;
    public String phone;
    public String lat;
    public String lng;
    public boolean isDealer;
    public boolean isWorkShop;

    public Dealer(String id, String name, String email, String webpage, String street, String postalcode, String city, String phone, String lat, String lng, boolean isDealer, boolean isWorkShop) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.webpage = webpage;
        this.street = street;
        this.postalcode = postalcode;
        this.city = city;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.isDealer = isDealer;
        this.isWorkShop = isWorkShop;
    }
}
