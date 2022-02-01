package com.example.chattingapp.Model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.View.Activity.RoomActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static final String CHANNEL_ID = "chatAPP";
    static final int notificationId = 0;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String roomID = remoteMessage.getData().get("roomID");
        Room room = new Room();
        room.setId(roomID);

        createNotificationChannel();

        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("data", room);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_splash)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //호출
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());


    }

    private void createNotificationChannel() {
        // Android8.0 이상인지 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널에 필요한 정보 제공
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            // 중요도 설정, Android7.1 이하는 다른 방식으로 지원한다.(위에서 설명)
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // 채널 생성
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
