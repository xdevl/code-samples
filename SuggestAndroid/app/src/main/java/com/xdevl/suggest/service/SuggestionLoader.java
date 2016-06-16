package com.xdevl.suggest.service;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.dao.WordDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestionLoader extends AsyncTaskLoader<List<Word>>
{
    private final WordDao mWordDao ;
    private final String mValue ;
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
            return mWordDao.lookup(mValue);
        } catch(IOException e) {
            mException=e ;
            return new ArrayList<>() ;
        }
    }

    public Exception getException()
    {
        return mException ;
    }
}
