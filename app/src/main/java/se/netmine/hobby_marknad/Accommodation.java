package se.netmine.hobby_marknad;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by jusuf on 2017-10-03.
 */

public class Accommodation extends SugarRecord<Facility> {
    public String accommodationId;
    public String name;

    Camping camping;
    String campingId;

    public Accommodation() {}

    public Accommodation(String accommodationId, String name, String campingId) {
        this.accommodationId = accommodationId;
        this.name = name;
        this.campingId = campingId;
    }
}
