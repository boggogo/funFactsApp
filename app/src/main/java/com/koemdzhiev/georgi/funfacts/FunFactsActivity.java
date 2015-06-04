package com.koemdzhiev.georgi.funfacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;


public class FunFactsActivity extends ActionBarActivity {

    public static final String TAG = FunFactsActivity.class.getSimpleName();
    private  FactBook mFactBook = new FactBook();
    private ColorWheel colorWheel = new ColorWheel();
    private String fact;
    private TextView factLabel;
    private Button showFactButton;
    private int color;
    private RelativeLayout relativeLayout;
    private List<ParseObject> mFacts;
    private String[] localFacts;
    private String [] allFacts;
    private boolean alertDismised = false;

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
                //String[] localFacts =  mFactBook.getFacts();
                if(isNetworkAvailable()){
                    //load all facts, from database and local
                    loadDBFacts();
                    String fact = getFactFromAll();
                    factLabel.setText(fact);
                    alertDismised = false;
                }else {
                    //alert that the network is unavailable and local facts will be used
                    if(alertDismised == false) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FunFactsActivity.this);
                        builder.setTitle("Oops!");
                        builder.setMessage("No internet!. Only fun facts stored on the device will be used!");
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDismised = true;
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    //load local facts
                    fact = mFactBook.getFact();
                    color = colorWheel.getColor();

                    factLabel.setText(fact);
                    showFactButton.setTextColor(color);
                    relativeLayout.setBackgroundColor(color);
                }
            }
        };

        localFacts = mFactBook.getFacts();
        //load all facts from db to mFacts variable
        loadDBFacts();
        localFacts = mFactBook.getFacts();
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
        //-----

    }
    //load all facts into allFacts variable (local + database)
    private void loadDBFacts() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("funWorldFacts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> facts, ParseException e) {
                if (e == null) {
                    mFacts = facts;

                    allFacts = new String[mFacts.size() + localFacts.length];
                    //Toast.makeText(FunFactsActivity.this,"mFacts: "+ mFacts.size() + ", local "+localFacts.length, Toast.LENGTH_LONG).show();
                    int i = 0;
                    for (; i < mFacts.size(); i++) {
                        allFacts[i] = mFacts.get(i).get("fact") + "";
                        Log.i(TAG, allFacts[i]);
                    }
                    for (; i < localFacts.length; i++) {
                        allFacts[i] = localFacts[i];
                        Log.i(TAG, allFacts[i]);
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FunFactsActivity.this);
                    builder.setTitle("Oops!");
                    builder.setMessage("Cannot send get fun facts from the database. Please try again later!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
    //method to check if there is network available
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        //contition to check if there is a network and if the device is connected
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return isAvailable;
    }

    //returns a fact from all facts - local and database
    protected String getFactFromAll(){
        String fact = "";
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(allFacts.length);
        fact = allFacts[randomNumber];
        return fact;
    }
}

