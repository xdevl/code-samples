package com.xdevl.sitescrape;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdevl.sitescrape.bean.Output;
import com.xdevl.sitescrape.bean.Product;
import com.xdevl.sitescrape.model.Model;
import com.xdevl.sitescrape.model.parser.JsoupProductParser;

import java.io.IOException;
import java.util.List;

public class Main
{
    private static final String DEFAULT_URL="http://hiring-tests.s3-website-eu-west-1.amazonaws.com/2015_Developer_Scrape/5_products.html" ;

    public static void main(String[] args)
    {
        Model model=new Model(new JsoupProductParser()) ;
        try {
            // Fetches product list
            List<Product> results=model.fetchProducts(args.length==0?DEFAULT_URL:args[0]) ;
            // Compute the sum of all the products
            float total=model.sum(results) ;
            ObjectMapper objectMapper=new ObjectMapper() ;
            // Outputs the results as Json on the console
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Output(results,total))) ;
        } catch(IOException e) {
            e.printStackTrace() ;
        }
    }
}
