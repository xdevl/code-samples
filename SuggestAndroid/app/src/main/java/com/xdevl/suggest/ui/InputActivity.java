package com.xdevl.suggest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.xdevl.suggest.R;
import com.xdevl.suggest.Settings;
import com.xdevl.suggest.service.SuggestService;

public class InputActivity extends AppCompatActivity implements TextWatcher, Runnable
{
    private EditText mEditText ;
    private final Handler mHandler=new Handler(Looper.getMainLooper()) ;
    private long mLastChanged ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_input) ;
        mEditText=(EditText)findViewById(R.id.input) ;
        mEditText.addTextChangedListener(this);
        startService(new Intent(this,SuggestService.class)) ;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence,int i,int i1,int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence,int i,int i1,int i2) {}

    @Override
    public void afterTextChanged(Editable editable)
    {
        mLastChanged=System.currentTimeMillis() ;
        mHandler.postDelayed(this,Settings.DELAY_LOOKUP) ;
    }

    @Override
    public void run()
    {
        // Trigger refresh only if nothing has been typed for Settings.DELAY_LOOKUP millis
        if(System.currentTimeMillis()-mLastChanged>=Settings.DELAY_LOOKUP)
        {
            Intent intent=new Intent(Settings.INTENT_ACTION_REFRESH);
            intent.putExtra(Intent.EXTRA_TEXT,mEditText.getText().toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}
