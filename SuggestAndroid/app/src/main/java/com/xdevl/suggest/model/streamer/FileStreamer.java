package com.xdevl.suggest.model.streamer;

import java.io.*;

/**
 * Implementation of {@link Streamer} using a file to stream its content.
 */
public class FileStreamer implements Streamer
{
    private final File mFile ;
    private final String mEncoding ;

    public FileStreamer(File file, String encoding)
    {
        mFile=file ;
        mEncoding=encoding ;
    }

    @Override
    public Reader getReader() throws IOException
    {
        return new InputStreamReader(new FileInputStream(mFile),mEncoding) ;
    }

    @Override
    public Writer getWriter() throws IOException
    {
        return new OutputStreamWriter(new FileOutputStream(mFile),mEncoding) ;
    }
}
