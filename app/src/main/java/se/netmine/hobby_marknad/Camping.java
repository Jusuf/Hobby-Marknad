package se.netmine.hobby_marknad;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;

/**
 * Created by jusuf on 2017-10-03.
 */

public class Camping extends SugarRecord<Camping>{

    public String campingId;
    public String name;
    public String phone;
    public String email;
    public String webpage;
    public String openFrom;
    public String openTo;
    public String fullServiceFrom;
    public String fullServiceTo;
    public String street;
    public String postalcode;
    public String city;
    public String lat;
    public String lng;
    public String descSV;
    public String stars;

    public Camping(){}

    public Camping(String campingId, String name, String phone, String email, String webpage, String openFrom, String openTo, String fullServiceFrom, String fullServiceTo, String street, String postalcode, String city, String lat, String lng, String descSV, String stars) {
        this.campingId = campingId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.webpage = webpage;
        this.openFrom = openFrom;
        this.openTo = openTo;
        this.fullServiceFrom = fullServiceFrom;
        this.fullServiceTo = fullServiceTo;
        this.street = street;
        this.postalcode = postalcode;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.descSV = descSV;
        this.stars = stars;
    }



    @Ignore
    public ArrayList<Accommodation> accommodations;

    @Ignore
    public ArrayList<Facility> facilities;

    @Ignore
    public ArrayList<String> images;
}
