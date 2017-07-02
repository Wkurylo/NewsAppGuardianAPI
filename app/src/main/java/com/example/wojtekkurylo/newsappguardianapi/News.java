package com.example.wojtekkurylo.newsappguardianapi;

import java.util.Date;

/**
 * Created by wojtekkurylo on 01.07.2017.
 */

public final class News {

    /**
     * section name of news
     */
    private String mSection;

    /**
     * title of news
     */
    private String mTitle;

    /**
     * date of earthquake in miliseconds
     */
    private String mDate;

    /**
     * link to news details webpage
     */
    private String mLinkUrl;

    /**
     * Public constructor com.example.wojtekkurylo.quakereport.Earthquake
     *
     * @param section storing the section name of news
     * @param title storing the title of news
     * @param date storing the date of news
     * @param linkUrl storing the link to news details webpage
     */
    public News(String section, String title,String date, String linkUrl){
        mSection = section;
        mTitle = title;
        mDate = date;
        mLinkUrl = linkUrl;
    }

    /**
     * Public method, returning String mSection
     */
    public String getSection(){
        return mSection;
    }

    /**
     * Public method, returning String mSection
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * Public method, returning String mSection
     */
    public String getDate(){
        return mDate;
    }

    /**
     * Public method, returning String mSection
     */
    public String getLinkUrl(){
        return mLinkUrl;
    }





}
