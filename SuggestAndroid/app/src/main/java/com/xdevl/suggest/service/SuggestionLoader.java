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
            String newValue=intent.getStringExtra(Intent.EXTRA_TEXT) ;
            if(!mValue.equals(newValue))
            {
                mValue=newValue ;
                onContentChanged() ;
            }
            else if(isStarted() && mResult!=null)
                deliverResult(mResult) ;
        }
    } ;

    private final WordDao mWordDao ;
    private boolean mRegistered ;
    public String mValue ;
    private Exception mException ;
    private List<Word> mResult ;

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
            mResult=null ;
            return (mResult=mWordDao.lookup(mValue,Settings.MAX_LOOKUP_RESULTS)) ;
        } catch(IOException e) {
            mException=e ;
            return new ArrayList<>() ;
        }
    }

    @Override
    protected void onStartLoading()
    {
        if(takeContentChanged() || (mResult==null && mException==null))
            forceLoad() ;
        else deliverResult(mResult) ;

        if(!mRegistered)
        {
            LocalBroadcastManager.getInstance(getContext())
                    .registerReceiver(receiver,new IntentFilter(Settings.INTENT_ACTION_REFRESH)) ;
            mRegistered=true ;
        }
    }

    @Override
    protected void onReset()
    {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver) ;
        mRegistered=false ;
    }

    public Exception getException()
    {
        return mException ;
    }
}
