package org.sdrc.dgacg.collect.android.models;

/**
 * Created by Subhadarshani on 27-12-2017.
 * this class is for set and retrive the values of data coming form itemsets.csv file
 */

public class ExternalChoiceData {
    private String name;
    private String id;
    private String label;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
