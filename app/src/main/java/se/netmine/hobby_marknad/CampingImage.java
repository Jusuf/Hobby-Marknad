package se.netmine.hobby_marknad;

import com.orm.SugarRecord;

/**
 * Created by jusuf on 2017-10-03.
 */

public class CampingImage extends SugarRecord<CampingImage>{
    public String fileName;

    public String campingId;
    Camping camping;

    public CampingImage() {}

    public CampingImage(String fileName, String campingId) {
        super();
        this.fileName = fileName;
        this.campingId = campingId;
    }

}
