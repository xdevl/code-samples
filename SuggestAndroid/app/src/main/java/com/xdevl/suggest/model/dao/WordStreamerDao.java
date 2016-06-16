package com.xdevl.suggest.model.dao;

import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.iterator.IOIterator;
import com.xdevl.suggest.model.iterator.WordReaderIterator;
import com.xdevl.suggest.model.streamer.Streamer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    public List<Word> lookup(String value) throws IOException
    {
        List<Word> words=new ArrayList<>() ;
        // Sanity checks
        if(value==null || value.isEmpty())
            return words;

        final IOIterator<Word> iterator=iterator() ;
        try {
            while(iterator.hasNext())
            {
                Word word=iterator.next() ;
                if(word.getValue().indexOf(value)==0)
                    words.add(word) ;
            }
        } catch(IOException e) {
            // We do best effort here, trying to close what we can
            try { iterator.close(); } catch(IOException _e) {}
            throw e ;
        }
        return words;
    }

    @Override
    public void populate(IOIterator<Word> iterator) throws IOException
    {
        // Sanity check
        if(iterator==null)
            return ;

        final BufferedWriter writer=new BufferedWriter(mStreamer.getWriter());
        try {
            while(iterator.hasNext())
            {
                writer.write(iterator.next().getValue());
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

    @Override
    public IOIterator<Word> iterator() throws IOException
    {
        try {
            return new WordReaderIterator(mStreamer.getReader());
        } catch(FileNotFoundException e) {
            return new WordReaderIterator(new StringReader("")) ;
        }
    }
}
