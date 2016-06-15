package com.xdevl.suggest.model.streamer;

import java.io.*;

/**
 * Generic interface to stream characters from a storage medium.
 * Implementations could use various different streaming source (ie: in memory, file, socket, etc...)
 */
public interface Streamer
{
    Reader getReader() throws IOException;
    Writer getWriter() throws IOException;
}
