1. 创建并发出通知：
   安卓8.0以上，需要创建渠道channel，才能兼容。
   请注意，NotificationCompat.Builder 构造函数要求您提供渠道 ID。这是兼容 Android 8.0（API 级别 26）及更高版本所必需的，但会被较旧版本忽略。
   请注意，NotificationChannel 构造函数需要一个 importance，它会使用 NotificationManager 类中的一个常量。此参数确定出现任何属于此渠道的通知时如何打断用户，但您还必须使用 setPriority() 设置优先级，才能支持 Android 7.1 和更低版本
   ```java
   public class MainActivity extends AppCompatActivity {
       private NotificationManager manager;
       private NotificationCompat.Builder mBuilder;//创建Builder方便以后更新通知
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           Button btn=findViewById(R.id.button4);
           Button btn1=findViewById(R.id.button);
           btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   send(btn);
               }
           });
           btn1.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v){
               cancelNotification(btn1);
               }
           });


       }
       public void send(View e){
           manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//创建通知管理器
           if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){//如果安卓版本大于O，创建渠道
               NotificationChannel channel=new NotificationChannel("id","通知",NotificationManager.IMPORTANCE_HIGH);
               manager.createNotificationChannel(channel);

           }
           mBuilder=new NotificationCompat.Builder(this,"id")
                   .setSmallIcon(R.drawable.ic_launcher_foreground)//设置小图标
                   .setTicker("music is playing")
                   .setContentText("你是来搞笑的吧")
                   .setColor(Color.parseColor("#00ff00"))//设置小图标颜色
                   .setWhen(System.currentTimeMillis())//设置系统时间，如果设置了大图标可能不够空间显示时间
                   .setAutoCancel(true)//点击是否取消通知
                   .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                  
           manager.notify(1,mBuilder.build());//用notify来发出通知，记住传递到notify()的通知ID因为如果之后您想要更新或移除通知，将需要使用这个 ID。
       }
       public void cancelNotification(View v){
           manager.cancel(1);
       }
   }
   ```
2. 设置点击事件
   每个通知都应该对点按操作做出响应，通常是在应用中打开对应于该通知的 Activity。为此，您必须指定通过 PendingIntent 对象定义的内容 Intent，并将其传递给 setContentIntent()。
   PendingIntent 的四个参数：Context， requestCode, Intent, flags，分别对应上下文对象、请求码、请求意图用以指明启动类及数据传递、关键标志位。
   ```java
    Intent intent = new Intent(this, MainActivity2.class);   //InTENT 跳转
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
   ```
3. 短内容通知：通过 `setContentText()` 方法设置，它只会显示一行文字，再多的话会用省略号省略。
   长内容通知：通过 `setStyle(new NotificationCompat.BigTextStyle().bigText()) `方法设置，它可以显示多行文字。
   图片通知：通过` setStyle(new NotificationCompat.BigPictureStyle() .bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.apple))) `方法设置，它可以在通知时显示一张图片。
