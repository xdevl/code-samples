package com.xdevl.suggest.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.xdevl.suggest.Settings;
import com.xdevl.suggest.model.dao.WordDao;
import com.xdevl.suggest.model.iterator.WordReaderIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SuggestService extends IntentService
{
    public static void synchronize(Context context)
    {
        Intent intent=new Intent(context,SuggestService.class) ;
        intent.setAction(Settings.INTENT_ACTION_SYNC) ;
        PendingIntent pendingIntent=PendingIntent.getService(context,0,intent,0) ;
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE) ;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),Settings.DELAY_SYNC,pendingIntent) ;
    }

    public SuggestService()
    {
        super(SuggestService.class.getSimpleName()) ;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(Settings.INTENT_ACTION_SYNC.equals(intent.getAction()))
        {
            WordDao wordDao=Settings.getWordDao(getApplicationContext()) ;
            File sourceFile=Settings.getWordSourceFile() ;
            try {
                wordDao.populate(new WordReaderIterator(new FileReader(sourceFile)));
                Log.i(SuggestService.class.getSimpleName(),"Successfully parsed word file at "+sourceFile.getAbsolutePath()+" :)") ;
            } catch(FileNotFoundException e) {
                Log.i(SuggestService.class.getSimpleName(),"Word file not found at "+sourceFile.getAbsolutePath()+" '-_-") ;
            } catch(IOException e) {
                Log.e(SuggestService.class.getSimpleName(),"Failed to parse word file at "+sourceFile.getAbsolutePath()+" :(",e) ;
            }

        }
    }
}
