package com.example.chattingapp.Model;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.MainActivity;
import com.example.chattingapp.View.Activity.RoomActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "chatAPP";
    private final int NOTIFICATION_GROUP_ID = 0;
    private final String GROUP_KEY_CHATTING = "CHATTING";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        int roomID = Integer.parseInt(remoteMessage.getData().get("roomID"));

        ActivityManager manager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo info = list.get(0);
        switch (info.topActivity.getClassName()) {
            case "com.example.chattingapp.View.Activity.RoomActivity":
                //TODO 현재 입장한 room_id와 푸시알림의 room_id 비교
                //TODO 같으면 푸시를 띄우지 않고, 다르면 푸시를 띄운다.
                break;
            case "com.example.chattingapp.View.Activity.RoomListFragment":
                //TODO 푸시알림을 띄우고, 푸시 데이터를 액티비티에 적용시킨다.
                break;
            default:
                //TODO 푸시알림만 띄운다.
                break;
        }

        createNotificationChannel();

        //호출
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(roomID, getMessageBuilder(title, body, roomID).build());
        notificationManager.notify(NOTIFICATION_GROUP_ID, getSummaryBuilder().build());
    }

    @NonNull
    private NotificationCompat.Builder getSummaryBuilder() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_splash)
                .setContentIntent(getRoomListIntent())
                .setGroup(GROUP_KEY_CHATTING)
                .setGroupSummary(true);
    }

    @NonNull
    private NotificationCompat.Builder getMessageBuilder(String title, String body, int roomID) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_splash)
                .setContentIntent(getRoomIntent(roomID))
                .setStyle(new NotificationCompat.MessagingStyle(getPerson(getNikName()))
                        .addMessage(body, System.currentTimeMillis(), getPerson(title)))
                .setAutoCancel(true)
                .addAction(getAction("읽음", getRoomIntent(roomID)))
                .addAction(getAction("답장", getRoomIntent(roomID)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(GROUP_KEY_CHATTING);
    }

    @NonNull
    NotificationCompat.Action getAction(String title, PendingIntent intent) {
        return new NotificationCompat.Action.Builder(0, title, intent).build();
    }

    @NonNull
    private Person getPerson(String name) {
        return new Person.Builder()
                .setIcon(IconCompat.createWithResource(this, R.drawable.profile_default))
                .setName(name)
                .build();
    }

    private String getNikName() {
        return SharedPreferenceUtil.getData(this, "nikName");
    }

    private PendingIntent getRoomListIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selectedItem", R.id.menu_chat);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent getRoomIntent(int roomID) {
        Room room = new Room();
        room.setId(roomID);

        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("data", room);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, roomID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void createNotificationChannel() {
        // Android8.0 이상인지 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 채널에 필요한 정보 제공
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            // 중요도 설정, Android7.1 이하는 다른 방식으로 지원한다.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            // 채널 생성
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
