package com.example.instagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("3MxyCgdRainx3U26MkJidm83EOQnBvdTwCd4jvB6")
                .clientKey("bx1mVy4MGlcwKEzPnAqSjObjyRWkjxNg4Y9B2ECH")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
