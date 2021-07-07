package com.example.instagram;

import android.app.Application;

import com.example.instagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    public static final String APPLICATION_ID = "3MxyCgdRainx3U26MkJidm83EOQnBvdTwCd4jvB6";
    public static final String CLIENT_KEY = "bx1mVy4MGlcwKEzPnAqSjObjyRWkjxNg4Y9B2ECH";
    public static final String SERVER = "https://parseapi.back4app.com";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(SERVER)
                .build()
        );
    }
}
