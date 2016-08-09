package com.xdevl.sitescrape.model.parser;

import com.xdevl.sitescrape.bean.Product;

import java.io.IOException;
import java.util.List;

/**
 * Generic interface defining a Product list parser. Sub-implementations could parse the
 * product list from a web page, a local file, a web service, etc...
 */
public interface ProductParser
{
    /**
     * Parse a given source to build a product list
     * @param source the source containing the product list to parse
     * @return a list of products
     * @throws IOException if the parsing fails or the source isn't available
     */
    List<Product> parse(String source) throws IOException ;
}
