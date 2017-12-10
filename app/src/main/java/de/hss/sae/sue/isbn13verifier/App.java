package de.hss.sae.sue.isbn13verifier;

import android.app.Application;
import android.content.Context;

/**
 * Created by robin.hartmann on 30.01.2017.
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
