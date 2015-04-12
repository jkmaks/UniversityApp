package com.jkmaks.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jkmaks.Types.Campus;
import com.jkmaks.Types.Event;
import com.jkmaks.myuw.R;

import java.util.ArrayList;


/**
 * Copied from Clifton's book Android User Interface Design
 * by Max
 * 1/8/2015
 */
public class StartingActivity extends Activity implements StartingFragment.ProgressListener {
    private static final String TAG_DATA_LOADER = "dataLoader";
    private static final String TAG_SPLASH_SCREEN = "splashScreen";
    private StartingFragment mDataLoaderFragment;
    private SplashScreenFragment mSplashScreenFragment;
    private static ArrayList<Campus> campuses;
    private static ArrayList<Event> events;

    @Override
    public void onCompletion(Double result) {
        Intent intent = new Intent(this, MainActivity.class);

        campuses = mDataLoaderFragment.getCampuses();
        events = mDataLoaderFragment.getEvents();
        //Log.d("Events that are given from starting activity are", events + "");
        intent.putExtra("events", events);
        intent.putExtra("campuses", campuses);
        this.finish();
        startActivity(intent);
    }

    @Override
    public void onProgressUpdate(int progress) {
        mSplashScreenFragment.setProgress(progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FragmentManager fm = getFragmentManager();
        mDataLoaderFragment = (StartingFragment)
                fm.findFragmentByTag(TAG_DATA_LOADER);

        if (mDataLoaderFragment == null) {
            mDataLoaderFragment = new StartingFragment();
            mDataLoaderFragment.setActivity(this);
            mDataLoaderFragment.setProgressListener(this);
            mDataLoaderFragment.startLoading();
            fm.beginTransaction().add(mDataLoaderFragment,
                    TAG_DATA_LOADER).commit();
        } else {
            if (checkCompletionStatus()) {
                return;
            }
        }
        // Show loading fragment
        mSplashScreenFragment = (SplashScreenFragment)
                fm.findFragmentByTag(TAG_SPLASH_SCREEN);
        if (mSplashScreenFragment == null) {
            mSplashScreenFragment = new SplashScreenFragment();
            fm.beginTransaction().add(android.R.id.content,
                    mSplashScreenFragment, TAG_SPLASH_SCREEN).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mDataLoaderFragment != null) {
            checkCompletionStatus();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDataLoaderFragment != null) {
            mDataLoaderFragment.removeProgressListener();
        }
    }

    /**
     * Checks if data is done loading, if it is, the result is handled
     *
     * @return true if data is done loading
     */
    private boolean checkCompletionStatus() {
        if (mDataLoaderFragment.hasResult()) {
            onCompletion(mDataLoaderFragment.getResult());
            FragmentManager fm = getFragmentManager();
            mSplashScreenFragment = (SplashScreenFragment)
                    fm.findFragmentByTag(TAG_SPLASH_SCREEN);
            if (mSplashScreenFragment != null) {
                fm.beginTransaction().remove(mSplashScreenFragment).
                        commit();
            }
            return true;
        }
        mDataLoaderFragment.setProgressListener(this);
        return false;
    }
}