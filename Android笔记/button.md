创建并绑定一个按钮 `Button btn=findViewById(R.id.button);`
绑定按钮的点击事件，通过创建View.OnClickListener()并重写onClick方法
```java
btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(btn);
            }
        });
```
所有的监听器都以匿名内部类来实现。这样做有两大好处。第一，使用匿名内部类，可以相对集中地实现监听器方法，一眼可见；第二，事件监听器一般只在一个地方使用，使用匿名内部类，就不用去创建繁琐的命名类了。

匿名内部类实现了OnClickListener接口，因此它也必须实现该接口唯一的onClick(View)方法。onClick(View)现在是个空方法。虽然必须实现onClick(View)方法，但具体如何实现取决于使用者，因此即使是个空方法，编译器也可以编译通过。 



toast的使用：
调用Toast类的以下方法，可创建toast:
`public static Toast makeText(Context context, int resId, int duration)`
该方法的Context参数通常是Activity的一个实例（Activity本身就是Context的子类）。第二个参数是toast要显示字符串消息的资源ID，也可以是字符串。Toast类必须借助Context才能找到并使用字符串资源ID。第三个参数通常是两个Toast常量中的一个，用来指定toast消息的停留时间。

创建toast后，可调用Toast.show()方法在屏幕上显示toast消息。

在makeText(...)里，传入QuizActivity实例作为Context的参数值。注意此处应输入的参数是QuizActivity.this，不要想当然地直接输入this。因为匿名类的使用，这里的this指的是监听器View.OnClickListener。
```java
mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
            }
        });
```
