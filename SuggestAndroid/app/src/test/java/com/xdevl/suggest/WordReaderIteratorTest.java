package com.xdevl.suggest;

import org.junit.Assert;
import org.junit.Test;
import com.xdevl.suggest.model.iterator.WordReaderIterator;

import java.io.IOException;
import java.io.StringReader;

public class WordReaderIteratorTest
{
    @Test
    public void emptyReader() throws IOException
    {
        WordReaderIterator iterator=new WordReaderIterator(new StringReader("")) ;
        Assert.assertFalse(iterator.hasNext()) ;
    }

    @Test
    public void singleWord() throws IOException
    {
        WordReaderIterator iterator=new WordReaderIterator(new StringReader("hello")) ;
        Assert.assertTrue(iterator.hasNext()) ;
        Assert.assertEquals(iterator.next().getValue(),"hello") ;
        Assert.assertFalse(iterator.hasNext()) ;
    }

    @Test
    public void multipleWords() throws IOException
    {
        WordReaderIterator iterator=new WordReaderIterator(new StringReader("hello\nworld")) ;
        Assert.assertTrue(iterator.hasNext()) ;
        Assert.assertEquals(iterator.next().getValue(),"hello") ;
        Assert.assertTrue(iterator.hasNext()) ;
        Assert.assertEquals(iterator.next().getValue(),"world") ;
        Assert.assertFalse(iterator.hasNext()) ;
    }

    @Test
    public void emptyLinesWithExtraSpaces() throws IOException
    {
        WordReaderIterator iterator=new WordReaderIterator(new StringReader("\n\n   hello   \n  \n   world\n \n\n")) ;
        Assert.assertTrue(iterator.hasNext()) ;
        Assert.assertEquals(iterator.next().getValue(),"hello") ;
        Assert.assertTrue(iterator.hasNext()) ;
        Assert.assertEquals(iterator.next().getValue(),"world") ;
        Assert.assertFalse(iterator.hasNext()) ;
    }
}
