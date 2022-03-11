# 채팅 앱  
  
### 기능  
  - [권한 동의](https://github.com/AnYeRim/ChattingApp/blob/main/README.md#%EA%B6%8C%ED%95%9C-%EB%8F%99%EC%9D%98)
  - [회원 가입](https://github.com/AnYeRim/ChattingApp/blob/main/README.md#%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85)
  - [로그인 (+ 자동 로그인)](https://github.com/AnYeRim/ChattingApp/blob/main/README.md#%EB%A1%9C%EA%B7%B8%EC%9D%B8--%EC%9E%90%EB%8F%99-%EB%A1%9C%EA%B7%B8%EC%9D%B8)
  - [1:1 채팅 (소켓 통신, FCM)](https://github.com/AnYeRim/ChattingApp/blob/main/README.md#11-%EC%B1%84%ED%8C%85)
   
### 실행 화면  
  
  
-----------------
  
### 권한 동의  

1. 필요한 권한에 동의되어 있는 경우, 스플래시 화면을 띄운다.  
  
``` C
@Override
protected void onResume() {
    super.onResume();

    if (noNeedAgreePermissions()) {
        showSplash();
    }
}
``` 
| <img src="https://user-images.githubusercontent.com/49059414/157839408-969eb5a4-b71d-4d73-ad5c-53a5bf209bc7.jpg"  width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157839649-3228c1d9-f041-4c91-9698-81dc2fea5307.jpg"  width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157839668-d6a321cc-c354-4251-9ef5-8527b2de87dd.jpg"  width="250" height="528"/>  |
|---|---|---|  
  
2. 아직 권한 동의가 되어있지 않는 경우, 동의 요청 창을 띄운다.
``` C
private void showRequestPermissions() {
        if (checkDeniedPermissions(PermissionActivity.this, initPms)) {
            showDialogRequestPermissions();
            return;
        }

        requestPermissions(initPms, REQUEST_PERMISSIONS_CODE);
    }
```
이 때 이미 거부한 적이 있는 경우, (권한 설정창으로 이동시키는) 권한 요청 다이얼로그를 띄운다.  
(requestPermissions로 권한 요청하여 2번 이상 거부되면 더이상 권한 요청 창이 띄워지지 않기 때문에,  
 권한 설정 창으로 이동시키도록 구현)  

-----------------  
### 회원가입  

1. 약관 동의 -> 휴대폰 인증 -> 비밀번호 기입  
 -> 개인정보 기입 -> 이메일 인증(선택사항) 을 거쳐 회원가입을 완료한다.  
 
| <img src="https://user-images.githubusercontent.com/49059414/157840322-6ccc19e2-600e-440c-9b66-f1811df160f3.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840326-812d71ef-db3a-4e4c-9f4d-2e931b4e899a.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840333-a91acd7b-638f-4eb9-b598-51f4ef790d95.jpg" width="250" height="528"/>  |
|---|---|---|
| <img src="https://user-images.githubusercontent.com/49059414/157840334-fc5510b3-4f6e-4c06-a192-39c235a22d38.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840339-28266df1-43a3-4f40-b912-76cfc9c62351.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840380-f57ed8cb-3099-4e2c-accf-de77732e2a3b.jpg" width="250" height="528"/>  |  

이 때, READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE 권한에 동의된 경우  
전화번호 인증 화면에서 휴대폰의 번호가 자동으로 입력되게 구현하였다.  
(한국인만 대상인 앱이라고 가정)  

``` C
private void setPhoneNumber() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        return;
        }

        String phoneNum = "";
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        phoneNum = telephonyManager.getLine1Number().toString();
        if(phoneNum.startsWith("+82")) {
            phoneNum = phoneNum.replace("+82", "0");
        }
        binding.edtPhone.setText(phoneNum);
    }
```
  
-----------------  
### 로그인 (+ 자동 로그인)  
  
1. 아이디와 비밀번호가 모두 일치하는 유저가 있으면 로그인 성공.  
(아이디는 폰번호 혹은 이메일)
  
| <img src="https://user-images.githubusercontent.com/49059414/157840553-16a86d4e-ebd8-4fcb-841d-9649e197ad60.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840564-567e4101-60b2-4403-a811-8bfbf0c0c442.jpg"  width="250" height="528"/> |
|---|---|  
  
2. 로그인 성공 시, 서버에서 JWT 토큰을 생성하여 클라이언트로 넘겨준다.  
받은 JWT 토큰을 SharedPreference에 저장.  
  
``` C
private void doLogin() {
    Call<ResponseUser> call = getApiInterface().doGetUser(user);
    ...
            if (isSuccessResponse(response)) {
                setSharedPreference("token", response.body().getToken());
                setSharedPreference("nikName", response.body().getUser().getNikname());
                setSharedPreference("userID", response.body().getUser().getId());
                startActivity(SplashActivity.class);
                getActivity().finish();
            }
    ...
}
```
  
3. 앱 실행 시, 스플래시 화면에서 토큰 여부에 따라 로그인/메인 화면을 띄운다.
  
``` C
Handler handler = new Handler();
handler.postDelayed(() -> {
    if (isEmptyToken()) {
        startActivity(LoginActivity.class);
    }else {
        startActivity(MainActivity.class);
    }
    finish();
}, 1000);
```
  
-----------------  
### 1:1 채팅  
  
1. 친구를 클릭해서 1:1 채팅방에 입장할 수 있다.
  
| <img src="https://user-images.githubusercontent.com/49059414/157840775-5a5f7174-eb81-4d12-a09b-cd5df308e9fe.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840835-1ee09013-a994-4c8b-bf01-cd1d6847db6c.jpg" width="250" height="528"/> |
|---|---|
  
이 때, 해당 친구의 채팅방을 찾아 입장하는데 채팅방이 없는 경우 채팅방을 만들고 입장한다.  
``` C
private void findRoom() {
    Call<ArrayList<Room>> call = getApiInterface().doFindRoom(friends.getId());
    ...
            if (response.isSuccessful()) {
                if (response.body() == null) {
                    createRoom();
                    return;
                }

                Room room = response.body().get(0);
                startActivity(RoomActivity.class, room);
            }
    ....
}
```
  
2. 채팅방 리스트 목록에서 선택하여 채팅방에 입장할 수 있다.
  
| <img src="https://user-images.githubusercontent.com/49059414/157840770-ff5a2e3c-023e-4fd5-8d93-2da1f1124794.jpg" width="250" height="528"/> | <img src="https://user-images.githubusercontent.com/49059414/157840831-6240cb0c-3e12-47b3-9ceb-e7805125f9a0.jpg" width="250" height="528"/> |
|---|---|  
  
3. 채팅방에 입장하면 소켓 연결을 하고, 메세지를 보낼 수 있다.
    
``` C
protected void onCreate(Bundle savedInstanceState) {
    if (room == null) {
        room = (Room) getIntent().getExtras().getSerializable("data");
        ...
        setSocket();
        socket.emit("joinRoom", room.getId());
    }
}

private void setSocket() {
    if (socket == null || !socket.connected()) {
        socket = SocketClient.getInstance();
        socket.connect();
        socket.on("getSendMessage", onSendMessage);
        socket.on("getReadMessage", onReadMessage);
    }
}
```
  
이 때, 서버에서는 메세지 전달 요청이 들어오면  
소켓 브로드캐스트를 통해 방에 입장해있는 다른 유저에게 전달하고    
FCM을 통해 방의 멤버인 유저에게 메세지를 전달한다.

4. 방에 입장되어 있는 유저인 경우, 소켓으로 메세지를 전달 받아  
화면에 메세지를 추가하고 소켓으로 읽었다고 전달한다.

``` C
Emitter.Listener onSendMessage = data -> {
   ...
            addMessage(JsonToMessage((JSONObject) data[0]));
            readSocketMessage();
   ...
};
```

5. 방에 입장되어 있지 않더라도 FCM으로 메세지를 전달받아 푸시 알림을 띄운다.
  
``` C
public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
    ...
    String title = remoteMessage.getData().get("title");
    String body = remoteMessage.getData().get("body");
    String roomID = remoteMessage.getData().get("roomID");
    
    // 채널을 만든다.
    createNotificationChannel();

    // 메세지 온 것을 배열로 저장해놓는다.
    setMessageArray(title, body);

    // 알림 띄우기
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
    notificationManager.notify(notificationId, getBuilder(title, body).build());
}
```
  
  이 때, 알림 스타일로 MessagingStyle을 줘서 대화 알림으로 알림 중에서도 상단에 뜨게 한다.
  (메세지는 3개까지만 보관하여 띄우도록 구현)
  
``` C
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

private NotificationCompat.MessagingStyle getStyle() {
    return new NotificationCompat.MessagingStyle(getMyPerson())
            .addMessage(message[0], System.currentTimeMillis(), sender[0])
            .addMessage(message[1], System.currentTimeMillis(), sender[1])
            .addMessage(message[2], System.currentTimeMillis(), sender[2]);
}
```
  
