package com.xdevl.suggest;

import android.content.Context;
import android.os.Environment;
import com.xdevl.suggest.model.dao.WordDao;
import com.xdevl.suggest.model.dao.WordStreamerDao;
import com.xdevl.suggest.model.streamer.FileStreamer;

import java.io.File;

/**
 * Holds constants and various other static application settings.
 */
public class Settings
{
    public static final String INTENT_SUFFIX="com.xdevl.suggest" ;
    public static final String INTENT_ACTION_RESULT=INTENT_SUFFIX+".action.ACTION_RESULT" ;
    public static final String INTENT_ACTION_SYNC=INTENT_SUFFIX+".action.ACTION_SYNC" ;

    public static final long DELAY_SYNC=5*1000 ; // synchronize every 5 seconds
    public static final int MAX_LOOKUP_RESULTS=100 ; // maximum number of result to return when doing a lookup

    public static File getWordSourceFile()
    {
        return new File(Environment.getExternalStorageDirectory(),"words.txt") ;
    }

    public static WordDao getWordDao(Context context)
    {
        return new WordStreamerDao(new FileStreamer(new File(context.getFilesDir(),"words.txt"))) ;
    }

    private Settings() {}
}
