package com.meghdutpoc.android.Helper;

import android.app.Application;

import com.mazenrashed.printooth.Printooth;

public class Controller extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
    }
}
