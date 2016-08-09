package com.xdevl.sitescrape.model;

import com.xdevl.sitescrape.bean.Product;
import com.xdevl.sitescrape.model.parser.ProductParser;

import java.io.IOException;
import java.util.List;

/**
 * Class implementing the application business logic
 */
public class Model
{
    public static final float PRICE_DELTA=0.001f ;

    private final ProductParser mProductParser;

    public Model(ProductParser productParser)
    {
        mProductParser=productParser;
    }

    /**
     * Fetches a product list
     * @param source the source from which to fetch the product list
     * @return a product list
     * @throws IOException if source is unavailable/invalid
     */
    public List<Product> fetchProducts(String source) throws IOException
    {
        return mProductParser.parse(source) ;
    }

    /**
     * Sums the unit price of a product list
     * @param products the product list to compute the sum of
     * @return the unit price sum
     */
    public float sum(List<Product> products)
    {
        if(products==null)
            return 0 ;

        float result=0 ;
        for(Product product: products)
            result+=product.getUnitPrice() ;
        return result ;
    }
}
