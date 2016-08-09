package com.xdevl.sitescrape.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Convenient Java bean class to display the application output
 */
public class Output
{
    private final List<Product> mResults ;
    private final float mTotal ;

    public Output(List<Product> results, float total)
    {
        mResults=results ;
        mTotal=total ;
    }

    @JsonProperty("results")
    public List<Product> getResults() {
        return mResults ;
    }

    @JsonProperty("total")
    public String getFormatedTotal() {
        return String.format("%.2f",mTotal) ;
    }
}
