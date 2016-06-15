package com.xdevl.suggest.model.iterator;

import com.xdevl.suggest.bean.Word;

import java.io.*;

/**
 * Implementation of {@link IOIterator<Word>} to fetch words from a Java reader
 */
public class WordReaderIterator implements IOIterator<Word>
{
    private final BufferedReader mReader ;
    private String nextLine=null ;

    public WordReaderIterator(Reader reader)
    {
        mReader=new BufferedReader(reader) ;
    }

    @Override
    public boolean hasNext() throws IOException
    {
        return nextLine!=null || (nextLine=mReader.readLine())!=null;
    }

    @Override
    public Word next() throws IOException
    {
        try {
            if(nextLine==null)
                hasNext();
            return new Word(nextLine);
        } finally {
            nextLine=null ;
        }
    }

    @Override
    public void close() throws IOException
    {
        mReader.close() ;
    }

}
