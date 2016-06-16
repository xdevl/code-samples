package com.xdevl.suggest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xdevl.suggest.R;
import com.xdevl.suggest.service.SuggestService;

public class SuggestActivity extends AppCompatActivity implements SuggestionFragment.InputProvider
{
    public static final String INTENT_EXTRA_INPUT=SuggestActivity.class.getName()+".INTENT_EXTRA_INPUT" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        SuggestService.synchronize(this) ;

        if(savedInstanceState==null)
            getSupportFragmentManager().beginTransaction().add(R.id.content,new SuggestionFragment()).commit() ;
    }

    @Override
    public String getInput()
    {
        return getIntent().getStringExtra(INTENT_EXTRA_INPUT) ;
    }
}
