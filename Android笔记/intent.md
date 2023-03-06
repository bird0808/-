1. intent对象是component用来与操作系统通信的一种媒介工具。目前为止，我们唯一见过的component就是activity。实际上还有其他一些component：service、broadcast receiver以及content provider。intent是一种多用途通信工具。Intent类有多个构造方法，能满足不同的使用需求。
2. ` public Intent(Context packageContext, Class<?> cls)`
    传入该方法的Class类型参数告诉ActivityManager应该启动哪个activity；Context参数告诉ActivityManager在哪里可以找到它

    ```java
    Intent intent = new Intent(MainActivity.this, CheatActivity.class);
    startActivity(intent);
    ```
3. 一个activity启动另一个activity最简单的方式是使用startActivity方法：`public void startActivity(Intent intent) `你也许会想当然地认为，startActivity(Intent)方法是一个静态方法，启动activity就是调用Activity子类的该方法。实际并非如此。activity调用startActivity(Intent)方法时，调用请求实际发给了操作系统。准确地说，调用请求发送了操作系统的ActivityManager。ActivityManager负责创建Activity实例并调用其onCreate(Bundle)方法。
4. 显式intent与隐式intent 
如果通过指定Context与Class对象，然后调用intent的构造方法来创建Intent，则创建的是显式intent。在同一应用中，我们使用显式intent来启动activity。同一应用里的两个activity，却要借助于应用外部的ActivityManager通信，这似乎有点怪。不过，这种模式会让不同应用间的activity交互变得容易很多。一个应用的activity如需启动另一个应用的activity，可通过创建隐式intent来处理。
5. activity间的数据传递
   要将extra数据信息添加给intent，需要调用Intent.putExtra(...)方法。确切地说，是调用如下方法：
   `public Intent putExtra(String name, boolean value)` Intent.putExtra(...)方法形式多变。不变的是，它总是有两个参数。一个参数是固定为String类型的键，另一个参数是键值，可以是多种数据类型。该方法返回intent自身，因此，需要时可进行链式调用。