package com.koemdzhiev.georgi.funfacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class FunFactsActivity extends ActionBarActivity {

    public static final String TAG = FunFactsActivity.class.getSimpleName();
    private  FactBook mFactBook = new FactBook();
    private ColorWheel colorWheel = new ColorWheel();
    private String fact;
    private TextView factLabel;
    private Button showFactButton;
    private int color;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);
        //Variables
        factLabel = (TextView) findViewById(R.id.factTextView);
        showFactButton = (Button) findViewById(R.id.showFactButton);

        relativeLayout = (RelativeLayout) findViewById(R.id.myRelativeLayout);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fact = mFactBook.getFact();
                color = colorWheel.getColor();

                factLabel.setText(fact);
                showFactButton.setTextColor(color);
                relativeLayout.setBackgroundColor(color);
            }
        };
        factLabel.setText(mFactBook.getFact());
        showFactButton.setOnClickListener(listener);


        //Creating a Toast
        //Toast.makeText(this,"Activity was created!",Toast.LENGTH_LONG).show();
        //Log.d(TAG,"Logging from the onCreate method");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_fact){
            //Toast.makeText(this,"CLICKED!",Toast.LENGTH_LONG).show();

            if (ParseUser.getCurrentUser() == null){
                //no user
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this,AddFactActivity.class);
                startActivity(intent);
            }
        }else if(item.getItemId() == R.id.action_sign_out){
            ParseUser.logOut();
            Toast.makeText(FunFactsActivity.this,"You have been signed out!",Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        factLabel.setText(mFactBook.getFact());
        color = colorWheel.getColor();
        relativeLayout.setBackgroundColor(color);
        showFactButton.setTextColor(color);
    }

}
