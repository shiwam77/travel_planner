package com.aoezdemir.todoapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.lang.ref.WeakReference;


public class RouterEmptyActivity extends Activity {

    public static final String INTENT_IS_WEB_API_ACCESSIBLE = "IS_WEB_API_ACCESSIBLE";
    private static final String TAG = RouterEmptyActivity.class.getSimpleName();
    private WeakReference<RouterEmptyActivity> activityReference = new WeakReference<>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        RouterEmptyActivity activity = activityReference.get();
        if (true) {
            intent = new Intent(activityReference.get(), LoginActivity.class);
        } else {
            intent = new Intent(activityReference.get(), OverviewActivity.class);
            intent.putExtra(INTENT_IS_WEB_API_ACCESSIBLE, false);
        }
        activity.startActivity(intent);
        finish();
    }
}