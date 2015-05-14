package com.koemdzhiev.georgi.funfacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class FunFactsActivity extends ActionBarActivity {

    public static final String TAG = FunFactsActivity.class.getSimpleName();
    private  FactBook mFactBook = new FactBook();
    private Button shareButtonl;
    private ColorWheel colorWheel = new ColorWheel();
    private String fact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);
        //Variables
        final TextView factLabel = (TextView) findViewById(R.id.factTextView);
        final Button showFactButton = (Button) findViewById(R.id.showFactButton);
        final Button shareButton = (Button) findViewById(R.id.shareButton);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.myRelativeLayout);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fact = mFactBook.getFact();
                int color = colorWheel.getColor();

                factLabel.setText(fact);
                showFactButton.setTextColor(color);
                shareButton.setTextColor(color);
                relativeLayout.setBackgroundColor(color);
            }
        };
        showFactButton.setOnClickListener(listener);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareTextIntent = new Intent();
                shareTextIntent.setAction(Intent.ACTION_SEND);
                shareTextIntent.putExtra(Intent.EXTRA_TEXT,fact);
                shareTextIntent.setType("text/plain");
                startActivity(shareTextIntent);
            }
        });
        //Creating a Toast
        //Toast.makeText(this,"Activity was created!",Toast.LENGTH_LONG).show();
        //Log.d(TAG,"Logging from the onCreate method");

    }

}
