package com.xdevl.suggest.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.xdevl.suggest.R;
import com.xdevl.suggest.service.SuggestService;

public class InputActivity extends AppCompatActivity implements SuggestionFragment.InputProvider, TextWatcher
{
    private EditText mEditText ;
    private SuggestionFragment mSuggestionFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_input) ;
        mEditText=(EditText)findViewById(R.id.input) ;
        mSuggestionFragment=(SuggestionFragment)getSupportFragmentManager().findFragmentById(R.id.suggestions) ;
        mEditText.addTextChangedListener(this);
        SuggestService.synchronize(this) ;
    }

    @Override
    public String getInput()
    {
        return mEditText!=null?mEditText.getText().toString():"" ;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence,int i,int i1,int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence,int i,int i1,int i2) {}

    @Override
    public void afterTextChanged(Editable editable)
    {
        mSuggestionFragment.refresh() ;
    }
}