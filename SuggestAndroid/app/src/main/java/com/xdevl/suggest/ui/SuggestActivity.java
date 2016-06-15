package com.xdevl.suggest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xdevl.suggest.R;
import com.xdevl.suggest.service.SuggestService;

public class SuggestActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        SuggestService.synchronize(this) ;
    }
}
