package com.xdevl.suggest.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import com.xdevl.suggest.Settings;
import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.dao.WordDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestionLoader extends AsyncTaskLoader<List<Word>>
{
    private final BroadcastReceiver receiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            mValue=intent.getStringExtra(Intent.EXTRA_TEXT) ;
            onContentChanged() ;
        }
    } ;

    private final WordDao mWordDao ;
    public String mValue ;
    private Exception mException ;

    public SuggestionLoader(Context context,WordDao wordDao,String value)
    {
        super(context) ;
        mWordDao=wordDao ;
        mValue=value ;
    }

    @Override
    public List<Word> loadInBackground()
    {
        try {
            mException=null ;
            return mWordDao.lookup(mValue,Settings.MAX_LOOKUP_RESULTS) ;
        } catch(IOException e) {
            mException=e ;
            return new ArrayList<>() ;
        }
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad() ;
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(receiver,new IntentFilter(Settings.INTENT_ACTION_REFRESH)) ;
    }

    @Override
    protected void onReset()
    {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver) ;
    }

    public Exception getException()
    {
        return mException ;
    }
}
