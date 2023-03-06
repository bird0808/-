在实际开发中LayoutInflater这个类还是非常有用的，它的作用类似于findViewById()。不同点是LayoutInflater是用来 找res/layout/下的xml布局文件，并且实例化；而findViewById()是找xml布局文件下的具体widget控件(如Button、TextView等)。对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；对于一个已经载入的界面，就可以使用Activity.findViewById()方法来获得其中的界面元素。
获得 LayoutInflater 实例的三种方式:
```java
LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater() 
LayoutInflater inflater = LayoutInflater.from(context);  
LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
```
这三种方式最终本质是都是调用的`Context.getSystemService()`。
`getSystemService()`是Android很重要的一个API，它是Activity的一个方法，根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。以下介绍系统相应的服务。

|传入的Name                |返回的对象         |说明:
|:------------------:|:--------------------:|:----------------:|
WINDOW_SERVICE            |WindowManager      |管理打开的窗口程序
LAYOUT_INFLATER_SERVICE   |LayoutInflater     |取得xml里定义的view
ACTIVITY_SERVICE          |ActivityManager    |管理应用程序的系统状态
POWER_SERVICE             |PowerManger        |电源的服务
ALARM_SERVICE             |AlarmManager       |闹钟的服务
NOTIFICATION_SERVICE      |NotificationManager|状态栏的服务
KEYGUARD_SERVICE          |KeyguardManager    |键盘锁的服务
LOCATION_SERVICE          |LocationManager    |位置的服务，如GPS
SEARCH_SERVICE            |SearchManager      |搜索的服务
VEBRATOR_SERVICE          |Vebrator           |手机震动的服务
CONNECTIVITY_SERVICE      |Connectivity       |网络连接的服务
WIFI_SERVICE              |WifiManager        |Wi-Fi服务
TELEPHONY_SERVICE         |TeleponyManager    |电话服务

inflate 方法
通过 sdk 的 api 文档，可以知道该方法有以下几种过载形式，返回值均是 View 对象，如下：
```java
public View inflate (int resource, ViewGroup root);
public View inflate (XmlPullParser parser, ViewGroup root);
public View inflate (XmlPullParser parser, ViewGroup root, boolean attachToRoot);
public View inflate (int resource, ViewGroup root, boolean attachToRoot);
```