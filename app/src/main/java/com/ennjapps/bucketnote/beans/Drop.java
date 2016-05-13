package com.ennjapps.bucketnote.beans;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by haider on 07-05-2016.
 */
public class Drop extends RealmObject {
    //The date when this row_drop was created by the user, Example: July 13 2015 converted and stored in milliseconds
    @PrimaryKey
    private long added;
    //Example "I want to take a tour of the Bahamas"
    private String what;
    private long creationdate;




    public Drop() {

    }

    public Drop(String what, long added,long creationdate) {
        this.what = what;
        this.added = added;
        this.creationdate = creationdate;

    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }
    public long getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(long creationdate) {
        this.creationdate = creationdate;
    }


}