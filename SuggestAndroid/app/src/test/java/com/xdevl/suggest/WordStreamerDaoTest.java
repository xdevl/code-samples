package com.xdevl.suggest;

import org.junit.Test;
import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.dao.WordDao;
import com.xdevl.suggest.model.dao.WordStreamerDao;
import com.xdevl.suggest.model.iterator.IOIterator;
import com.xdevl.suggest.model.iterator.WordReaderIterator;
import com.xdevl.suggest.model.streamer.MemoryStreamer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;

public class WordStreamerDaoTest
{
    @Test
    public void populate() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("hello\nworld")));
        IOIterator<Word> iterator=wordDao.iterator() ;
        Assert.assertEquals(iterator.next().getValue(),"hello"); ;
        Assert.assertEquals(iterator.next().getValue(),"world") ;
        Assert.assertFalse(iterator.hasNext()) ;
    }

    @Test
    public void lookupEmptyWords() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        Assert.assertTrue(wordDao.lookup("hello",0).isEmpty()) ;
    }

    @Test
    public void emptyLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nblue"))) ;
        Assert.assertTrue(wordDao.lookup(null,0).isEmpty()) ;
        Assert.assertTrue(wordDao.lookup("",0).isEmpty()) ;
    }

    @Test
    public void noMatchLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nblue"))) ;
        Assert.assertTrue(wordDao.lookup("yellow",0).isEmpty()) ;
    }

    @Test
    public void singleMatchLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nblue"))) ;
        List<Word> results=wordDao.lookup("green",0) ;
        Assert.assertTrue(results.size()==1) ;
        Assert.assertEquals(results.get(0).getValue(),"green") ;
    }

    @Test
    public void multipleMatchLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nblue\ngrey"))) ;
        List<Word> results=wordDao.lookup("gr",0) ;
        Assert.assertTrue(results.size()==2) ;
        String tmp=results.get(0).getValue()+results.get(1).getValue() ;
        Assert.assertTrue(tmp.equals("greengrey") || tmp.equals("greygreen")) ;
    }

    @Test
    public void maxMatchLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nblue\ngrey"))) ;
        List<Word> results=wordDao.lookup("gr",1) ;
        Assert.assertTrue(results.size()==1) ;
    }

    @Test
    public void duplicateLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nblue\nred"))) ;
        List<Word> results=wordDao.lookup("re",1) ;
        Assert.assertTrue(results.size()==1) ;
        Assert.assertEquals(results.get(0).getValue(),"red") ;
    }

    @Test
    public void caseSensitivityLookup() throws IOException
    {
        WordDao wordDao=new WordStreamerDao(new MemoryStreamer()) ;
        wordDao.populate(new WordReaderIterator(new StringReader("red\ngreen\nbLuE\nred"))) ;
        List<Word> results=wordDao.lookup("BLue",1) ;
        Assert.assertTrue(results.size()==1) ;
        Assert.assertEquals(results.get(0).getValue(),"blue") ;
    }
}