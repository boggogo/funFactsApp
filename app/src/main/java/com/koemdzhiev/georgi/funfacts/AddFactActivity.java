package com.koemdzhiev.georgi.funfacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class AddFactActivity extends ActionBarActivity {
    public static final String TAG = AddFactActivity.class.getSimpleName();
    private EditText mFunFact;
    private Button mAddBtn;
    private Button mDoneBtn;
    private ColorWheel colorWheel = new ColorWheel();
    private int color;
    private RelativeLayout mRelativeLayout;
    private ParseUser mCurrentUser;
    private ParseRelation<ParseObject> mRelation;
    private List<ParseObject> mFacts;
    private ArrayList<String> databaseFacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fact);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add a fun fact");

        mFunFact = (EditText) findViewById(R.id.addFunFactEditText);
        mAddBtn = (Button) findViewById(R.id.addFunFactBtn);
        mDoneBtn = (Button) findViewById(R.id.doneBtn);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.addFunFactRelativeLayout);
        color = colorWheel.getColor();
        mRelativeLayout.setBackgroundColor(color);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });
        mCurrentUser = ParseUser.getCurrentUser();
        mRelation = mCurrentUser.getRelation("funWorldFacts");

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFunFact.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFactActivity.this);
                    builder.setTitle("Oops!");
                    builder.setMessage("Cannot send an empty fact!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    SaveFactInDB();
                }
            }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFactActivity.this, FunFactsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        loadDBFacts();
    }

    private void SaveFactInDB() {
        ParseObject facts = new ParseObject("funWorldFacts");
        facts.put("fact", mFunFact.getText().toString() + "- added by user: " + mCurrentUser.getUsername());
        facts.put("createdBy", mCurrentUser);
        for(String s:databaseFacts){
            Log.i(TAG,s);
        }

        Log.i(TAG,mFunFact.getText().toString());


        if(databaseFacts.contains(mFunFact.getText().toString() +"- added by user: " + mCurrentUser.getUsername() )){
            //alert that the fact already exist
            AlertDialog.Builder builder = new AlertDialog.Builder(AddFactActivity.this);
            builder.setTitle("Oops!");
            builder.setMessage("This fun fact already exists in our database!");
            builder.setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            //add the fact
            facts.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        //success
                        Toast.makeText(AddFactActivity.this, "Your fact has been added to the database!", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d(TAG, "", e);
                    }
                }
            });
            loadDBFacts();
        }

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mFunFact.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addfact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            SaveFactInDB();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadDBFacts() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("funWorldFacts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> facts, ParseException e) {
                if (e == null) {
                    mFacts = facts;
                    databaseFacts = new ArrayList();

                    for (int i = 0; i < mFacts.size(); i++) {
                        databaseFacts.add(mFacts.get(i).get("fact").toString());
                        //Log.i(TAG, databaseFacts.get(i));
                    }

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFactActivity.this);
                    builder.setTitle("Oops!");
                    builder.setMessage("Cannot send get fun facts from the database. Please try again later!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}