package se.netmine.hobby_marknad;

/**
 * Created by jusuf on 2017-10-03.
 */

public class Facility {
    public String id;
    public String name;
    public Category category;
    boolean isSelected = false;

    public Facility(String id, String name, Category category) {
        super();
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
