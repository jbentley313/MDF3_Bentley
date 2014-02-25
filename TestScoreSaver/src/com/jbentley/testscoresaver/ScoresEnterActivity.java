package com.jbentley.testscoresaver;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ScoresEnterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_enter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scores_enter, menu);
        return true;
    }
    
}
