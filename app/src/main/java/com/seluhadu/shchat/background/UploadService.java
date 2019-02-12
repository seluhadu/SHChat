package com.seluhadu.shchat.background;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;


public class UploadService extends Service {
    private static final int UPLOAD_CANClED = 101;
    private static final int UPLOAD_FINISHED = 102;
    private NotificationManager manager;
    private IBinder binder = new UploadBinder();
    private IncomingHandler incomingHandler;
    private PowerManager.WakeLock wakeLock;
    private HandlerThread uploadHandlerThread;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.setReferenceCounted(false);
        uploadHandlerThread = new HandlerThread("UploadHandler", Process.THREAD_PRIORITY_BACKGROUND);
        uploadHandlerThread.start();
        incomingHandler = new IncomingHandler(this, uploadHandlerThread.getLooper());
        sendBroadcast(new Intent("com.suluhadu.SHChat.UPLOAD_SERVICE_CREATED"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public class UploadBinder extends Binder {
        @Nonnull
        public UploadService getService() {
            return UploadService.this;
        }
    }

    class IncomingHandler extends Handler {
        WeakReference<UploadService> mService;

        IncomingHandler(UploadService uploadService, Looper looper) {
            super(looper);
            mService = new WeakReference<>(uploadService);
        }

        @Override
        public void handleMessage(Message msg) {
            final UploadService service = mService.get();
            if (service == null) {
                return;
            }
            switch (msg.what) {
                case UPLOAD_CANClED:
                    break;
                case UPLOAD_FINISHED:
                    break;
            }
        }
    }
}
