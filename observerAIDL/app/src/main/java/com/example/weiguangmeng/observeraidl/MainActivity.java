package com.example.weiguangmeng.observeraidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.weiguangmeng.observeraidl.event.ServiceRjo;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    IPostEventService service;
    PostEventServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        initService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(ServiceRjo rjo) {
        Log.d(TAG, "service rjo is " + rjo.getMessage());
    }

    class PostEventServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder bindService) {
            service = IPostEventService.Stub.asInterface(bindService);
            Log.d(TAG, "service is connected!");
            try {
                service.postEvent();
            } catch (RemoteException e) {
                // e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.d(TAG, "service is disconnected!");

        }
    }

    /**
     * This is our function which binds our activity(MainActivity) to our service(AddService).
     */
    private void initService() {
        Log.i(TAG, "initService()");
        connection = new PostEventServiceConnection();
        Intent i = new Intent();
        i.setAction("com.example.weiguangmeng.observeraidl.PostEventService");
        Log.d(TAG, "class name is " + com.example.weiguangmeng.observeraidl.PostEventService.class.getName());
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "initService() bound value: " + ret);
    }

    /**
     * This is our function to un-binds this activity from our service.
     */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }


}
