package com.xdevl.suggest.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SuggestService extends IntentService
{
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
            Log.d(SuggestService.class.getSimpleName(),"Action received: "+ACTION_SYNC) ;
    }
}
