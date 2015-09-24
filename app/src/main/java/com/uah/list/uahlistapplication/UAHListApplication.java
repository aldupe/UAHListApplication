package com.uah.list.uahlistapplication;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;


import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.data.IBMData;

/**
 * Created by ADPM on 30/07/2015.
 */
public final class UAHListApplication extends Application {
        private static final String APP_ID = "applicationID";
        private static final String APP_SECRET = "applicationSecret";
        private static final String APP_ROUTE = "applicationRoute";
        private static final String PROPS_FILE = "uahlist.properties";

        public static final int EDIT_ACTIVITY_RC = 1;
        private static final String CLASS_NAME = UAHListApplication.class.getSimpleName();
        List<Item> itemList;

        public UAHListApplication() {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity,Bundle savedInstanceState) {
                    Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
                }
                @Override
                public void onActivityStarted(Activity activity) {
                    Log.d(CLASS_NAME, "Activity started: " + activity.getLocalClassName());
                }
                @Override
                public void onActivityResumed(Activity activity) {
                    Log.d(CLASS_NAME, "Activity resumed: " + activity.getLocalClassName());
                }
                @Override
                public void onActivitySaveInstanceState(Activity activity,Bundle outState) {
                    Log.d(CLASS_NAME, "Activity saved instance state: " + activity.getLocalClassName());
                }
                @Override
                public void onActivityPaused(Activity activity) {
                    Log.d(CLASS_NAME, "Activity paused: " + activity.getLocalClassName());
                }
                @Override
                public void onActivityStopped(Activity activity) {
                    Log.d(CLASS_NAME, "Activity stopped: " + activity.getLocalClassName());
                }
                @Override
                public void onActivityDestroyed(Activity activity) {
                    Log.d(CLASS_NAME, "Activity destroyed: " + activity.getLocalClassName());
                }
            });
        }

        @Override
        public void onCreate() {
            super.onCreate();
            itemList = new ArrayList<Item>();
            // Read from properties file
            Properties props = new java.util.Properties();
            Context context = getApplicationContext();
            try {
                AssetManager assetManager = context.getAssets();
                props.load(assetManager.open(PROPS_FILE));
                Log.i(CLASS_NAME, "Found configuration file: " + PROPS_FILE);
            } catch (FileNotFoundException e) {
                Log.e(CLASS_NAME, "The uahlist.properties file was not found.", e);
            } catch (IOException e) {
                Log.e(CLASS_NAME, "The bluelist.properties file could not be read properly.", e);
            }
            // initialize the IBM core backend-as-a-service
            IBMBluemix.initialize(this, props.getProperty(APP_ID), props.getProperty(APP_SECRET), props.getProperty(APP_ROUTE));
            // initialize the IBM Data Service
            IBMData.initializeService();
            // register the Item Specialization
            Item.registerSpecialization(Item.class);
        }

        /**
         * Returns the itemList, an array of Item objects.
         * @return itemList
         */
        public List<Item> getItemList() {
            return itemList;
        }

        public void setItemList(List<Item> list) {
            itemList = list;
        }
}
