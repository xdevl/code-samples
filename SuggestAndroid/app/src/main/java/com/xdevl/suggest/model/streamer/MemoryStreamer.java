package com.xdevl.suggest.model.streamer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Implementation of {@link Streamer} using an in memory array to stream its content.
 */
public class MemoryStreamer implements Streamer
{
    public static final int DEFAULT_SIZE=4096*1024 ;

    private class MemoryWriter extends Writer
    {
        @Override
        public void close() throws IOException {}
        @Override
        public void flush() throws IOException {}
        @Override
        public void write(char[] chars, int off, int len) throws IOException
        {
            if(mCount+len>=mChars.length)
                throw new IOException("Out of memory") ;
            System.arraycopy(chars,off,mChars,mCount,len) ;
            mCount+=len ;
        }
    }

    private class MemoryReader extends Reader
    {
        private int offset=0 ;
        @Override
        public void close() throws IOException {}
        @Override
        public int read(char[] chars, int off, int len) throws IOException
        {
            if(offset>=mCount)
                return -1 ;
            else if(offset+len>=mCount)
                len=mCount-offset ;
            System.arraycopy(mChars,offset,chars,off,len) ;
            offset+=len ;
            return len ;
        }
    }

    private final char[] mChars ;
    private int mCount=0 ;

    public MemoryStreamer()
    {
        this(DEFAULT_SIZE) ;
    }

    public MemoryStreamer(int size)
    {
        mChars=new char[size] ;
    }

    @Override
    public Reader getReader() throws IOException
    {
        return new MemoryReader() ;
    }

    @Override
    public Writer getWriter() throws IOException
    {
        mCount=0 ;
        return new MemoryWriter() ;
    }
}
