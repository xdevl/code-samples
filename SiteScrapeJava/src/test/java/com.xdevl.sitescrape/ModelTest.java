package com.xdevl.sitescrape;

import com.xdevl.sitescrape.bean.Product;
import com.xdevl.sitescrape.model.Model;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelTest
{
    @Test
    public void sum()
    {
        Model model=new Model(null) ;
        Assert.assertEquals(model.sum(null),0,Model.PRICE_DELTA) ;
        Assert.assertEquals(model.sum(new ArrayList<Product>()),0,Model.PRICE_DELTA) ;
        Assert.assertEquals(model.sum(Arrays.asList(
                new Product("title",0,1.0f,"description"),
                new Product("title",0,2.0f,"description"),
                new Product("title",0,3.0f,"description"),
                new Product("title",0,4.0f,"description"))),10.0f,Model.PRICE_DELTA) ;
    }
}
