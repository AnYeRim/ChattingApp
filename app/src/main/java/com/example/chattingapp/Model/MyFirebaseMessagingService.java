package com.example.chattingapp.Model;

import android.annotation.SuppressLint;
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

import com.example.chattingapp.Model.DTO.Room;
import com.example.chattingapp.R;
import com.example.chattingapp.Utils.SharedPreferenceUtil;
import com.example.chattingapp.View.Activity.MainActivity;
import com.example.chattingapp.View.Activity.RoomActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String CHANNEL_ID = "chatAPP";
    private final int notificationId = 0;

    private Person[] sender = new Person[3];
    private String[] senderName = new String[3];
    private String[] message = new String[3];

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String roomID = remoteMessage.getData().get("roomID");

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

        setMessageArray(title, body);

        //호출
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, getBuilder(title, body).build());
    }

    @NonNull
    private NotificationCompat.Builder getBuilder(String title, String body) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_splash)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(getRoomListIntent())
                .setStyle(getStyle())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    @NonNull
    private NotificationCompat.MessagingStyle getStyle() {
        return new NotificationCompat.MessagingStyle(getMyPerson())
                .addMessage(message[0], System.currentTimeMillis(), sender[0])
                .addMessage(message[1], System.currentTimeMillis(), sender[1])
                .addMessage(message[2], System.currentTimeMillis(), sender[2]);
    }

    @NonNull
    private Person getMyPerson() {
        return new Person.Builder().setName(SharedPreferenceUtil.getData(this, "nikName")).build();
    }

    private PendingIntent getRoomListIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selectedItem",R.id.menu_chat);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent getRoomIntent(String roomID) {
        Room room = new Room();
        room.setId(roomID);

        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra("data", room);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void setMessageArray(String title, String body) {
        int index = Arrays.asList(senderName).indexOf(title);
        // 이전 목록에 동일 보낸이가 없으면
        if (index == -1) {
            // 빈 곳 없으면 한 칸씩 밀고 맨 끝에 넣기
            if(sender[0] != null && sender[1] != null && sender [2] != null){
                for(int i = 0; i <3; i++){
                    setMessageData(i, sender[i+1], senderName[i+1], message[i+1]);
                }
                setMessageData(2, new Person.Builder().setName(title).build(), title, body);
            }
            // 빈 곳을 찾아 데이터 넣기
            for (int i = 0; i < 3; i++) {
                if(sender[i] == null) {
                    setMessageData(i, new Person.Builder().setName(title).build(), title, body);
                    break;
                }
            }
        }else {
            // 이전 목록에 이미 보낸이가 있으면 해당 배열에 추가
            setMessageData(index, new Person.Builder().setName(title).build(), title, body);
        }
    }

    private void setMessageData(int i, Person person, String title, String body) {
        sender[i] = person;
        senderName[i] = title;
        message[i] = body;
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
