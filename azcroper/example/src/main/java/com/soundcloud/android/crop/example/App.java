package com.soundcloud.android.crop.example;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

/**
 * Created by prashant.chovatiya on 3/15/2018.
 */

public class App extends Application
{
    static String APP_FOLDERNAME = "Download";

    public static String getAppFolderBasePath()
    {
        return  Environment.getExternalStorageDirectory().toString();
    }

    public static void  showLog(String tag,String tag2)
    {
        Log.i(tag,tag2);
    }
    public static void  showLog(String tag2)
    {
        Log.i("tag",tag2);
    }
}
