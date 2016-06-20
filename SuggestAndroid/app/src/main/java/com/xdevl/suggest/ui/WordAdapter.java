package com.xdevl.suggest.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.R;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>
{
    public interface OnSelectListener
    {
        void onSelect(Word word) ;
    }

    private final List<Word> mWords ;
    private final OnSelectListener mListener ;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView mValue ;
        private final OnSelectListener mListener ;
        private Word mWord ;

        public ViewHolder(View itemView, OnSelectListener listener)
        {
            super(itemView) ;
            mValue=(TextView)itemView.findViewById(R.id.value) ;
            mListener=listener ;
            if(listener!=null)
                itemView.setOnClickListener(this) ;
        }

        public void setWord(Word word)
        {
            mValue.setText(word.getValue()) ;
            mWord=word ;
        }

        @Override
        public void onClick(View view)
        {
            mListener.onSelect(mWord) ;
        }
    }

    public WordAdapter(OnSelectListener listener)
    {
        mWords=new ArrayList<>() ;
        mListener=listener ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_word,parent,false),mListener) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.setWord(mWords.get(position)) ;
    }

    @Override
    public int getItemCount()
    {
        return mWords.size() ;
    }

    public void populate(List<Word> words)
    {
        mWords.clear() ;
        mWords.addAll(words) ;
        notifyDataSetChanged() ;
    }
}
