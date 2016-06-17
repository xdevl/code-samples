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
     * Lookup for a list of words starting with {@value}.
     * @param value the value to lookup (case insensitive)
     * @param value the maximum number of matching words to return (ignored if less than one)
     * @return a list of words starting with {@code value}. If {@code value} is empty or null, an empty list will be returned
     * @throws IOException if the call failed to complete normally
     */
    List<Word> lookup(String value,int max) throws IOException ;

    /**
     * Populate the bank of words, old existing words will be discarded. All words are stored as lower case.
     * {@code iterator} lifecycle will be managed by the callee (ie: the caller doesn't need to worry about closing it)
     * @param iterator the iterator to fetch the new words from
     * @throws IOException if the call failed to complete normally
     */
    void populate(IOIterator<Word> iterator) throws IOException ;
}
