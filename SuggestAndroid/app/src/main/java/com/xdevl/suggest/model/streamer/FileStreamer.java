package com.xdevl.suggest.model.streamer;

import java.io.*;

/**
 * Implementation of {@link Streamer} using a file to stream its content.
 */
public class FileStreamer implements Streamer
{
    private File mFile ;

    public FileStreamer(File file)
    {
        mFile=file ;
    }

    @Override
    public Reader getReader() throws IOException
    {
        return new FileReader(mFile) ;
    }

    @Override
    public Writer getWriter() throws IOException
    {
        return new FileWriter(mFile) ;
    }
}
