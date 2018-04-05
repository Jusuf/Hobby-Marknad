package se.netmine.hobby_marknad;

import com.orm.SugarRecord;

/**
 * Created by jusuf on 2017-10-03.
 */

public class FacilityOption extends SugarRecord{
    public String facilityId;
    public String name;
    boolean isSelected = false;

    public String facilityCategoryId;
    public String facilityCategoryName;


    public FacilityOption() {}

    public FacilityOption(String facilityId, String name, String facilityCategoryId, String facilityCategoryName) {
        super();
        this.facilityId = facilityId;
        this.name = name;
        this.facilityCategoryId = facilityCategoryId;
        this.facilityCategoryName = facilityCategoryName;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
