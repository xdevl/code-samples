package com.xdevl.suggest.model.dao;

import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.iterator.IOIterator;

import java.io.IOException;
import java.util.List;

/**
 * Generic interface to access a bank of words. Implementations could use various different storage strategies
 * (ie: database, file, web server, etc...)
 */
public interface WordDao
{
    /**
     * Lookup for a list of words starting with {@value}
     * @param value the value to lookup
     * @return a list of words starting with {@code value}
     * @throws IOException if the call failed to complete normally
     */
    List<Word> lookup(String value) throws IOException ;

    /**
     * Populate the bank of words, old existing words will be discarded
     * @param iterator the iterator to fetch the new words from
     * @throws IOException if the call failed to complete normally
     */
    void populate(IOIterator<Word> iterator) throws IOException ;

    /** Returns an {@link IOIterator<Word>} to this bank of words.
     * No assumption can be made about the words order.
     * @return an iterator to this bank of words
     * @throws IOException if the call failed to complete normally
     */
    IOIterator<Word> iterator() throws IOException ;
}
