package com.xdevl.suggest.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xdevl.suggest.R;
import com.xdevl.suggest.bean.Word;

import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>
{
    private final List<Word> mWords=new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView mValue ;

        public ViewHolder(View itemView)
        {
            super(itemView) ;
            mValue=(TextView)itemView.findViewById(R.id.value) ;
        }

        public void setWord(Word word)
        {
            mValue.setText(word.getValue()) ;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_word,parent,false)) ;
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
