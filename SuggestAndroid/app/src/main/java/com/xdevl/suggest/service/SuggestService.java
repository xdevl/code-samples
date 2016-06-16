package com.xdevl.suggest.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.xdevl.suggest.model.dao.WordDao;
import com.xdevl.suggest.model.dao.WordStreamerDao;
import com.xdevl.suggest.model.iterator.WordReaderIterator;
import com.xdevl.suggest.model.streamer.FileStreamer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SuggestService extends IntentService
{
    public static WordDao getWordDao(Context context)
    {
        return new WordStreamerDao(new FileStreamer(new File(context.getFilesDir(),"words.txt"))) ;
    }

    public static void synchronize(Context context)
    {
        Intent intent=new Intent(context,SuggestService.class) ;
        intent.setAction(SuggestService.ACTION_SYNC) ;
        PendingIntent pendingIntent=PendingIntent.getService(context,0,intent,0) ;
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE) ;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),DELAY_SYNC,pendingIntent) ;
    }

    private static final String ACTION_SYNC=SuggestService.class.getName()+".ACTION_SYNC" ;
    private static final long DELAY_SYNC=5*1000 ; // synchronize every 5 seconds

    public SuggestService()
    {
        super(SuggestService.class.getSimpleName()) ;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(ACTION_SYNC.equals(intent.getAction()))
        {
            WordDao wordDao=getWordDao(getApplicationContext()) ;
            try {
                wordDao.populate(new WordReaderIterator(new FileReader(new File
                        (Environment.getExternalStorageDirectory(),"words.txt"))));
                Log.i(SuggestService.class.getSimpleName(),"Successfully parsed word file :)") ;
            } catch(FileNotFoundException e) {
                Log.i(SuggestService.class.getSimpleName(),"Word file not found '-_-") ;
            } catch(IOException e) {
                Log.e(SuggestService.class.getSimpleName(),"Failed to parse word file :(",e) ;
            }

        }
    }
}
