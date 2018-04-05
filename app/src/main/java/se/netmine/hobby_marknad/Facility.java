package se.netmine.hobby_marknad;

import com.orm.SugarRecord;

/**
 * Created by jusuf on 2017-10-03.
 */

public class Facility extends SugarRecord{
    public String facilityId;
    public String name;
    public String facilityCategoryId;
    public String facilityCategoryName;

    public String campingId;
    Camping camping;

    public Facility() {}

    public Facility(String facilityId, String name, String facilityCategoryId, String campingId, String facilityCategoryName) {
        super();
        this.facilityId = facilityId;
        this.name = name;
        this.facilityCategoryId = facilityCategoryId;
        this.facilityCategoryName = facilityCategoryName;

        this.campingId = campingId;
    }

}
