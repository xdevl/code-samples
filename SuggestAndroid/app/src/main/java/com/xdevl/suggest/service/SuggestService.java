package com.xdevl.suggest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.xdevl.suggest.Settings;
import com.xdevl.suggest.model.dao.WordDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SuggestService extends Service implements Runnable
{
    @Override
    public void onCreate()
    {
        super.onCreate() ;
        new Thread(this).start() ;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        return START_STICKY ;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null ;
    }

    @Override
    public void run()
    {
        WordDao wordDao=Settings.getWordDao(getApplicationContext()) ;
        // For logging file path only, we can't assume the list of words will necessarily come from a file
        File sourceFile=Settings.getWordSourceFile() ;
        while(true)
        {
            try {
                wordDao.populate(Settings.geSourcetWordIterator()) ;
                Log.i(SuggestService.class.getSimpleName(),"Successfully parsed word file at "+sourceFile.getAbsolutePath()+" :)") ;
                Thread.sleep(Settings.DELAY_SYNC) ;
            } catch(FileNotFoundException e) {
                Log.i(SuggestService.class.getSimpleName(),"Word file not found at "+sourceFile.getAbsolutePath()+" '-_-") ;
            } catch(IOException e) {
                Log.e(SuggestService.class.getSimpleName(),"Failed to parse word file at "+sourceFile.getAbsolutePath()+" :(",e) ;
            } catch(InterruptedException e) {
                // Don't do anything, this is a START_STICKY service anyway
            }
        }
    }
}
