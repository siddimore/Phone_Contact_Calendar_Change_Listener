package com.example.morefamily.contactchange;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.widget.Toast;

/**
 * Created by MoreFamily on 3/22/2016.
 */
public class eventListener extends Service {

    private int mContactCount;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.getContentResolver().registerContentObserver(
                CalendarContract.Events.CONTENT_URI, true, mObserver);
        mContactCount = getContactCount();
    }

    private int getContactCount() {
        Cursor cursor = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(eventListener.this.getApplicationContext(), "Inside Permission Granted Block.", Toast.LENGTH_SHORT).show();
                    cursor = getContentResolver().query(
                            CalendarContract.Events.CONTENT_URI, null, null, null,
                            null);
                    if (cursor != null) {
                        return cursor.getCount();
                    } else {
                        return 0;
                    }
                    //return TODO;
                }
            }

        } catch (Exception ignore) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    private ContentObserver mObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            Handler handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(eventListener.this.getApplicationContext(), "Event changed...", Toast.LENGTH_SHORT).show();
                }
            });
            final int currentCount = getContactCount();
            if (currentCount < mContactCount) {
                // Event Deleted.
            } else if (currentCount == mContactCount) {
                // Event Updated.
            } else {
                // Event added.
            }
            mContactCount = currentCount;
        }

    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

}
