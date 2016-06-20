package com.xdevl.suggest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import com.xdevl.suggest.model.streamer.MemoryStreamer;
import com.xdevl.suggest.model.streamer.Streamer;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class MemoryStreamerTest
{
    @Rule
    public ExpectedException mExpectedException=ExpectedException.none() ;

    @Test
    public void writeRead() throws IOException
    {
        Streamer streamer=new MemoryStreamer() ;
        String str="Hello World" ;
        char[] buffer=new char[2*str.length()] ;
        streamer.getWriter().write(str) ;
        int length=streamer.getReader().read(buffer) ;
        assertTrue(length==str.length()) ;
        assertTrue(new String(buffer,0,length).equals(str)) ;
    }

    @Test
    public void outOfMemory() throws IOException
    {
        mExpectedException.expect(IOException.class) ;
        mExpectedException.expectMessage("Out of memory") ;
        Streamer streamer=new MemoryStreamer(8) ;
        streamer.getWriter().write("Hello world");
    }
}
