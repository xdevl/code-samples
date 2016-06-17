package com.xdevl.suggest.model.dao;

import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.iterator.IOIterator;
import com.xdevl.suggest.model.iterator.WordReaderIterator;
import com.xdevl.suggest.model.streamer.Streamer;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link WordDao} using a {@link Streamer} as storage strategy.
 * Each word gets persisted on a single line.
 */
public class WordStreamerDao implements WordDao
{
    private final Streamer mStreamer ;

    public WordStreamerDao(Streamer streamer)
    {
        mStreamer=streamer ;
    }

    @Override
    public synchronized List<Word> lookup(String value,int max) throws IOException
    {
        // We start with a set as we don't want to return duplicates
        Set<Word> results=new HashSet<>() ;
        // Sanity checks
        if(value==null || value.isEmpty())
            return new ArrayList<>(results) ;

        value=value.toLowerCase() ;
        final IOIterator<Word> iterator=iterator() ;
        try {
            while(iterator.hasNext() && (max<1 || results.size()<max))
            {
                Word word=iterator.next() ;
                if(word.getValue().indexOf(value)==0)
                    results.add(word) ;
            }
            iterator.close() ;
        } catch(IOException e) {
            // We do best effort here, trying to close what we can
            try { iterator.close(); } catch(IOException _e) {}
            throw e ;
        }
        return new ArrayList<>(results) ;
    }

    @Override
    public synchronized void populate(IOIterator<Word> iterator) throws IOException
    {
        // Sanity check
        if(iterator==null)
            return ;

        final BufferedWriter writer=new BufferedWriter(mStreamer.getWriter());
        try {
            while(iterator.hasNext())
            {
                writer.write(iterator.next().getValue().toLowerCase());
                writer.newLine();
            }
            writer.close() ;
            iterator.close() ;
        } catch(IOException e) {
            // We do best effort here, trying to close what we can
            try { writer.close(); } catch(IOException _e) {}
            try { iterator.close(); } catch(IOException _e) {}
            throw e ;
        }
    }

    private IOIterator<Word> iterator() throws IOException
    {
        try {
            return new WordReaderIterator(mStreamer.getReader());
        } catch(FileNotFoundException e) {
            return new WordReaderIterator(new StringReader("")) ;
        }
    }
}
