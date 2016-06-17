package com.xdevl.suggest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.xdevl.suggest.Settings;
import com.xdevl.suggest.bean.Word;
import com.xdevl.suggest.service.SuggestService;
import com.xdevl.suggest.R;

public class SuggestActivity extends AppCompatActivity implements SuggestionFragment.InputProvider, WordAdapter.OnSelectListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true) ;
        if(savedInstanceState==null)
            getSupportFragmentManager().beginTransaction().add(R.id.content,new SuggestionFragment()).commit() ;
        startService(new Intent(this,SuggestService.class)) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish() ;
                break ;
            default: return super.onOptionsItemSelected(item) ;
        }
        return true ;
    }

    @Override
    public String getInput()
    {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT) ;
    }

    @Override
    public void onSelect(Word word)
    {
        Intent result=new Intent(Settings.INTENT_ACTION_RESULT) ;
        result.putExtra(Intent.EXTRA_TEXT,word.getValue()) ;
        setResult(Activity.RESULT_OK,result) ;
        finish() ;
    }
}
