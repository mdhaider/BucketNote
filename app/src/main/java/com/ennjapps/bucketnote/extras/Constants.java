package com.ennjapps.bucketnote.extras;

/**
 * Created by haider on 07-05-2016.
 */
public interface Constants {

    String TEXTS="texts";
    String ADDED = "added";
    String POSITION = "position";
    String KEY = "sort_option";
    //The items whose target completion date is the nearest
    int SORT_ASCENDING_DATE = 1;
    //The items whose target completion date is the farthest
    int SORT_DESCENDING_DATE = 2;
    //The items that are complete as marked by the user
    int SHOW_COMPLETE = 3;
    //The items that are incomplete
    int SHOW_INCOMPLETE = 4;
}
