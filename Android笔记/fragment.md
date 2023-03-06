1. 要使用fragment，需要在activity的xml布局文件上为其定义容器,activity只负责托管fragment
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/fragment_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</FrameLayout>
```
   FrameLayout是服务于CrimeFragment的容器视图。注意该容器视图是个通用性视图，不单
   单用于CrimeFragment类，你还可以用它托管其他的fragment。

2. 然后创建fragment的类管理fragment视图，并且创建该类对应的xml布局文件。
   在fragment类中，需要重写
   `public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)`函数
   该方法实例化 fragment 视图的布局，然后将实例化的 View 返回给托管 activity 。
   LayoutInflater及ViewGroup是实例化布局的必要参数。Bundle用来存储恢复数据，可供该方
   法从保存状态下重建视图。
```java
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_main_list,container,false);
        /*可以在这里利用v绑定fragment视图的组件并进行相应的设置*/
        mRecyclerView=v.findViewById(R.id.main_recycler_view);
        return v;
    }
```
   在onCreateView(...)方法中，fragment的视图是直接通过调用LayoutInflater.inflate(...)
   方法并传入布局的资源ID生成的。第二个参数是视图的父视图，我们通常需要父视图来正确配置
   组件。第三个参数告诉布局生成器是否将生成的视图添加给父视图。这里，传入了false参数，
   因为我们将以代码的方式添加视图。

3. 动态加载 Fragment 则需要通过 FragmentTransaction 碎⽚事务组件完成。⾸先，需要获取 FragmentManager 对象；
   其次，调⽤ FragmentManager 对象的 beginTransaction() ⽅法获得 FragmentTransaction 对象；在此基础上调⽤ FragmentTransaction 对象的相应⽅法添加、替换 Fragment ⾄指定的 ViewGroup 视图容器；最后，调⽤FragmentTransaction 对象的 commit() ⽅法提交碎⽚事务，执⾏ Fragment 加载操作。
```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        //获取fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        /*
        获取fragment视图，首先，使用R.id.fragment_container的容器视图资源ID，向FragmentManager请求并获
        取fragment。如果要获取的fragment在队列中，FragmentManager就直接返回它。
        为什么要获取的fragment可能有了呢？前面说过，设备旋转或回收内存时，Android系统会销
        毁CrimeActivity，而后重建时，会调用Activity.onCreate(Bundle)方法。activity被
        销毁时，它的FragmentManager会将fragment队列保存下来。这样，activity重建时，新的
        FragmentManager会首先获取保存的队列，然后重建fragment队列，从而恢复到原来的状态。
        当然，如果指定容器视图资源ID的fragment不存在，则fragment变量为空值。这时应该新建
        Fragment，并启动一个新的fragment事务，将新建fragment添加到队列中。
        */
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);//先查找有没有这个fragment
        if (fragment == null) {
            fragment = createFragment();//createFragment()创建所需要的新的fragment视图类
            //创建一个新的fragment事务，执行一个fragment添加操作，然后提交该事务。
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
```