package com.xdevl.suggest.model.iterator;

import com.xdevl.suggest.bean.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Implementation of {@link IOIterator < Word >} to fetch words from a Java reader.
 * Each line should only contain a single word, all empty lines (ie: containing only
 * blank or no characters) are ignored. Spaces at the beginning and end of each lines
 * will be removed but if there is a space between 2 characters, it will be parsed as part of the word.
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
        if(nextLine!=null)
            return true ;
        while((nextLine=mReader.readLine())!=null && (nextLine=nextLine.trim()).isEmpty()) ;

        return nextLine!=null ;
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
