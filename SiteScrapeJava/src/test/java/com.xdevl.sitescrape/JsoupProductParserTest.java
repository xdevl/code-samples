package com.xdevl.sitescrape;

import com.xdevl.sitescrape.bean.Product;
import com.xdevl.sitescrape.model.Model;
import com.xdevl.sitescrape.model.parser.JsoupProductParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class JsoupProductParserTest
{
    private static final String EMPTY_HTML="<div><span></span></div>" ;

    private static String generateProductTitleAndLink(String title, String url)
    {
        return "<div class=\"productInfo\"><a href=\""+url+"\">"+title+"</a></div>" ;
    }

    private static String generateProductPrice(float price)
    {
        return "<div class=\"pricing\"><div class=\"pricePerUnit\">&pound"+price+"/unit</div></div>" ;
    }

    private static String generateProductDescription(String description)
    {
        return "<div id=\"information\"><h3>Description</h3><div class=\"productText\"><p>"+description+"</p></div></div>" ;
    }

    private static String generateProductList(Product ...products)
    {
        StringBuilder builder=new StringBuilder() ;
        builder.append("<div class=\"productLister\">") ;
        for(Product product: products)
        {
            builder.append("<div class=\"product\">") ;
            builder.append(generateProductTitleAndLink(product.getTitle(),"")) ;
            builder.append(generateProductPrice(product.getUnitPrice())) ;
            builder.append(generateProductDescription(product.getDescription())) ;
            builder.append("</div>") ;
        }
        return builder.toString() ;
    }

    /**
     * See {@link JsoupProductParser#parseTitle(Element)} and {@link JsoupProductParser#parseLink(Element)}
     */
    @Test
    public void parseProductTitleAndLink()
    {
        final String title="my title", url="http://www.google.com" ;
        Element titleElement=Jsoup.parse(generateProductTitleAndLink(title,url)) ;
        Assert.assertEquals(JsoupProductParser.parseTitle(null),"") ;
        Assert.assertEquals(JsoupProductParser.parseTitle(Jsoup.parse(EMPTY_HTML)),"") ;
        Assert.assertEquals(JsoupProductParser.parseTitle(titleElement),title) ;
        Assert.assertEquals(JsoupProductParser.parseLink(null),"") ;
        Assert.assertEquals(JsoupProductParser.parseLink(Jsoup.parse(EMPTY_HTML)),"") ;
        Assert.assertEquals(JsoupProductParser.parseLink(titleElement),url) ;
    }

    /**
     * see {@link JsoupProductParser#parsePrice(Element)}
     */
    @Test
    public void parseProductPrice()
    {
        Assert.assertEquals(JsoupProductParser.parsePrice(null),0,Model.PRICE_DELTA) ;
        Assert.assertEquals(JsoupProductParser.parsePrice(Jsoup.parse(EMPTY_HTML)),0,Model.PRICE_DELTA) ;
        final float price=6.70f ;
        Assert.assertEquals(JsoupProductParser.parsePrice(Jsoup.parse(generateProductPrice(price))),price,Model.PRICE_DELTA) ;
    }

    /**
     * see {@link JsoupProductParser#parseDescription(Element)}
     */
    @Test
    public void parseProductDescription()
    {
        Assert.assertEquals(JsoupProductParser.parseDescription(null),"") ;
        Assert.assertEquals(JsoupProductParser.parseDescription(Jsoup.parse(EMPTY_HTML)),"") ;
        final String description="my description" ;
        Assert.assertEquals(JsoupProductParser.parseDescription(Jsoup.parse(generateProductDescription(description))),description) ;
    }

    /**
     * see {@link JsoupProductParser#parseList(Element)}
     */
    @Test
    public void parseList()
    {
        Product[] products=new Product[]
        {
            new Product("Product 1",0,1.0f,"Product 1 description"),
            new Product("Product 2",0,2.0f,"Product 2 description"),
            new Product("Product 3",0,3.0f,"Product 3 description")
        } ;

        Assert.assertTrue(JsoupProductParser.parseList(null).isEmpty()) ;
        Assert.assertTrue(JsoupProductParser.parseList(Jsoup.parse(generateProductList())).isEmpty()) ;

        Elements elements=JsoupProductParser.parseList(Jsoup.parse(generateProductList(products))) ;
        Assert.assertEquals(elements.size(),products.length) ;
        for(int i=0;i<elements.size();++i)
        {
            Assert.assertEquals(JsoupProductParser.parseTitle(elements.get(i)),products[i].getTitle()) ;
            Assert.assertEquals(JsoupProductParser.parsePrice(elements.get(i)),products[i].getUnitPrice(),Model.PRICE_DELTA) ;
            Assert.assertEquals(JsoupProductParser.parseDescription(elements.get(i)),products[i].getDescription()) ;
        }
    }
}
