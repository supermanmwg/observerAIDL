package com.example.weiguangmeng.observeraidl;

import com.example.weiguangmeng.observeraidl.IPostEventService;
import com.example.weiguangmeng.observeraidl.event.BaseEvent;
import com.example.weiguangmeng.observeraidl.event.ServiceRjo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by weiguangmeng on 16/2/20.
 */
public class PostEventService extends Service {

    private static final String TAG = "PostEventService";

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        Log.i(TAG, "onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IPostEventService.Stub mBinder = new IPostEventService.Stub() {
        @Override
        public void postEvent() throws RemoteException {
            ServiceRjo rjo = new ServiceRjo();
            rjo.setMessage("post service");
            EventBus.getDefault().post(rjo);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onDestroy");
    }

    public void onEventMainThread(BaseEvent rjo) {
        Log.d(TAG, "service rjo is " + rjo.getMessage());
    }

}
