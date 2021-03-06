package com.xdevl.suggest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xdevl.suggest.R;
import com.xdevl.suggest.Settings;
import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.model.dao.WordDao;
import com.xdevl.suggest.service.SuggestionLoader;

import java.util.List;

public class SuggestionFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Word>>
{
    public static SuggestionFragment create(String value)
    {
        Bundle bundle=new Bundle() ;
        bundle.putString(Intent.EXTRA_TEXT,value) ;
        SuggestionFragment fragment=new SuggestionFragment() ;
        fragment.setArguments(bundle) ;
        return fragment ;
    }

    private WordDao mWordDao ;
    private WordAdapter mAdapter ;
    private TextView mTextView ;
    private RecyclerView mRecyclerView ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        mWordDao=Settings.getWordDao(getContext()) ;
        mAdapter=new WordAdapter(getActivity()instanceof WordAdapter.OnSelectListener?(WordAdapter.OnSelectListener)getActivity():null) ;
        View view=inflater.inflate(R.layout.fragment_suggest,container,false) ;
        mTextView=(TextView)view.findViewById(R.id.msg) ;
        mRecyclerView=(RecyclerView)view.findViewById(R.id.suggestions) ;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
        mRecyclerView.setAdapter(mAdapter) ;
        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState) ;
        getActivity().getSupportLoaderManager().initLoader(0,null,this) ;
    }

    @Override
    public Loader<List<Word>> onCreateLoader(int id,Bundle args)
    {
        return new SuggestionLoader(getContext(),mWordDao,
                getArguments()!=null?getArguments().getString(Intent.EXTRA_TEXT):"") ;
    }

    @Override
    public void onLoadFinished(Loader<List<Word>> loader,List<Word> data)
    {
        Exception exception=loader instanceof SuggestionLoader?((SuggestionLoader)loader).getException():null ;
        if(exception!=null)
            mTextView.setText(exception.getMessage()) ;
        else mTextView.setText(R.string.msg_empty) ;
        boolean empty=exception!=null || data.isEmpty() ;
        mTextView.setVisibility(empty?View.VISIBLE:View.GONE) ;
        mRecyclerView.setVisibility(empty?View.GONE:View.VISIBLE) ;
        mAdapter.populate(data) ;
    }

    @Override
    public void onLoaderReset(Loader<List<Word>> loader) {}
}
