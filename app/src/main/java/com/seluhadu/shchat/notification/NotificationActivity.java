package com.seluhadu.shchat.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.seluhadu.shchat.R;

import static com.seluhadu.shchat.notification.App.CHANNEL_ID;

public class NotificationActivity extends AppCompatActivity {
    private NotificationManagerCompat managerCompat;
    private EditText mInput;
    private EditText mTittle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        managerCompat = NotificationManagerCompat.from(this);
        Button mSend = findViewById(R.id.send);
        mInput = findViewById(R.id.text);
        mTittle = findViewById(R.id.tittle);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    private void sendNotification() {
        String message = mInput.getText().toString();
        String tittle = mTittle.getText().toString();

        Intent activityIntent = new Intent(this, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("message", message);
        broadcastIntent.putExtra("tittle", tittle);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(tittle)) {
            @SuppressLint("ResourceAsColor") Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(message)
                    .setColor(R.color.grayTextDarkTheme)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.mipmap.ic_launcher, tittle, actionIntent)
                    .build();
            managerCompat.notify(1, notification);
        }
    }
}
