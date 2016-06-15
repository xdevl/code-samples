package com.xdevl.suggest.model.iterator;

import java.io.Closeable;
import java.io.IOException;

/**
 * Convenient interface to iterate over objects when getting the next value can potentially fail and throw an exception.
 * It also inherits the closeable interface in order to close any potential resource which may have been opened
 * (ie: database or server connection, file descriptor, etc...)
 * @param <T> the type of the objects to iterate over
 */
public interface IOIterator<T> extends Closeable
{
    boolean hasNext() throws IOException ;
    T next() throws IOException ;
    void close() throws IOException ;
}
