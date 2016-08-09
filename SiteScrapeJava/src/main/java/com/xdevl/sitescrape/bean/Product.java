package com.xdevl.sitescrape.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java bean class to describe a product
 */
public class Product
{
    private final String mTitle ;
    private final long mSize ;
    private final float mUnitPrice ;
    private final String mDescription ;

    public Product(String title, long size, float unitPrice, String description)
    {
        mTitle=title ;
        mSize=size ;
        mUnitPrice=unitPrice ;
        mDescription=description ;
    }

    @JsonProperty("title")
    public String getTitle() {
        return mTitle ;
    }

    @JsonIgnore
    public float getUnitPrice() {
        return mUnitPrice ;
    }

    @JsonProperty("description")
    public String getDescription() {
        return mDescription ;
    }

    @JsonProperty("size")
    public String getFormatedSize() {
        return String.format("%.2fkb",mSize/1024.f) ;
    }

    @JsonProperty("unit_price")
    public String getFormatedUnitPrice() {
        return String.format("%.2f",mUnitPrice) ;
    }
}
