package io.realworld.saveoursoul;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;

/**
 * Created by sourabh on 13/6/17.
 */

public class SendSmsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
