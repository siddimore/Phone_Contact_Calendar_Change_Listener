package com.example.morefamily.contactchange;


/**
 * Created by MoreFamily on 3/15/2016.
 */

        import android.app.Service;
        import android.content.Intent;
        import android.database.ContentObserver;
        import android.database.Cursor;
        import android.os.Handler;
        import android.os.IBinder;
        import android.os.Looper;
        import android.provider.ContactsContract;
        import android.widget.Toast;

public class contactListener extends Service {

    private int mContactCount;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContactCount = getContactCount();
        this.getContentResolver().registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI, true, mObserver);
    }

    private int getContactCount() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    null);
            if (cursor != null) {
                return cursor.getCount();
            } else {
                return 0;
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
                    Toast.makeText(contactListener.this.getApplicationContext(), "Contact changed...", Toast.LENGTH_SHORT).show();
                }
            });
            final int currentCount = getContactCount();
            if (currentCount < mContactCount) {
                // Contact Deleted
            } else if (currentCount == mContactCount) {
                // Contact Update.
            } else {
                // Contact Inserted.
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