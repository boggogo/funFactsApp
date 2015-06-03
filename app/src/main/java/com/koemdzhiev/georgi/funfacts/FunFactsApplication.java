package com.koemdzhiev.georgi.funfacts;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by koemdzhiev on 03/06/2015.
 */
public class FunFactsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "xKW94gxINdPyPimNWSqp7VEjKPiUOoZswBftklAw", "YE41srO6dJNyr6pJI4tZ0q8nUFivUDtfdtBXqkqF");

    }
}
