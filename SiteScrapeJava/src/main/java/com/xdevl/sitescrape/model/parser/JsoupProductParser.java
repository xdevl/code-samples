package com.xdevl.sitescrape.model.parser;

import com.xdevl.sitescrape.bean.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link ProductParser} to the product list from a web page using Jsoup.
 * Note the strategy implemented here if some attribute are missing or malformed
 * when doing the parsing is to just set the corresponding value to "empty" instead of throwing an exception.
 */
public class JsoupProductParser implements ProductParser
{
    private static Pattern PRICE_PATTERN=Pattern.compile("&pound(\\d+\\.\\d+)/unit") ;

    @Override
    public List<Product> parse(String source) throws IOException
    {
        List<Product> results=new ArrayList<>() ;
        Document document=Jsoup.connect(source).get() ;
        for(Element element: parseList(document))
            results.add(parseProduct(element)) ;

        return results ;
    }

    /**
     * Parses a Jsoup element to turn it into a {@link Product}
     * @param element the Jsoup element to parse
     * @return the parsed {@link Product}
     * @throws IOException
     */
    protected Product parseProduct(Element element) throws IOException
    {
        Connection connection=Jsoup.connect(parseLink(element)) ;
        Document document=connection.get() ;
        return new Product(parseTitle(element),
                connection.response().bodyAsBytes().length,
                parsePrice(element),
                parseDescription(document)) ;
    }

    /**
     * Convenient method to get the text value of a Jsoup element
     * @param ancestor any ancestor of the element to get the text of
     * @param selector a Jsoup selector defining the element to get the text of
     * @return the element text value or an empty string if the element doesn't exist
     */
    protected static String getTextValue(Element ancestor, String selector)
    {
        if(ancestor==null)
            return "" ;

        Elements elements=ancestor.select(selector) ;
        return elements.isEmpty()?"":elements.first().text() ;
    }

    /**
     * Convenient method to get an attribute value of a Jsoup element
     * @param ancestor any ancestor of the element to get the attribute value of
     * @param selector a Jsoup selector defining the element to get the the attribute value of
     * @param attribute the attribute name to get the value of
     * @return the element attribute value or an empty string if the element doesn't exist
     */
    protected static String getAttributeValue(Element ancestor, String selector, String attribute)
    {
        if(ancestor==null)
            return "" ;

        Elements elements=ancestor.select(selector) ;
        return elements.isEmpty()?"":elements.first().attr(attribute) ;
    }

    /**
     * Returns all Jsoup elements matching a product for a given top level element.
     * A product is defined as: {@literal <div class="productLister">...<div class="product">...<!-- product definition --></div>...</div>}
     * @param element the top level element from which start the parsing
     * @return a list of Jsoup elements matching a product
     */
    public static Elements parseList(Element element)
    {
        if(element==null)
            return new Elements() ;

        return element.select(".productLister .product") ;
    }

    /**
     * Parses a product link given a Jsoup element.
     * A product link is defined as: {@literal <div class="productInfo">...<a href="link value">...</a>...</div>}
     * @param element the element to parse the product link from
     * @return the product link value
     */
    public static String parseLink(Element element)
    {
        return getAttributeValue(element,".productInfo a[href]","href") ;
    }

    /**
     * Parses a product title given a Jsoup element.
     * A product title is defined as: {@literal <div class="productInfo">...<a href="..."><!-- title --></a>...</div>}
     * @param element the element to parse the product title from
     * @return the product title value
     */
    public static String parseTitle(Element element)
    {
        return getTextValue(element,".productInfo a[href]") ;
    }

    /**
     * Parses a product price given a Jsoup element.
     * A product price is defined as: {@literal <div class="pricing">...<div class="pricePerUnit">...&pound<!-- price -->/unit...</div>...</div>}
     * @param element the element to parse the product price from
     * @return the product price value
     */
    public static float parsePrice(Element element)
    {
        Matcher matcher=PRICE_PATTERN.matcher(getTextValue(element,".pricing .pricePerUnit")) ;
        return matcher.find()?Float.parseFloat(matcher.group(1)):0 ;
    }

    /**
     * Parses a product description given a Jsoup element.
     * A product description is defined as: {@literal <div id="information">...
     * <h3>Description</h3><div class="productText"><p><!-- description --></p>...</div>...</div>}
     * @param element the element to parse the product description from
     * @return the product description value
     */
    public static String parseDescription(Element element)
    {
        return getTextValue(element,"#information h3:matches(Description) + div.productText > p") ;
    }
}
