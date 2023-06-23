# lambda表达式

java的lambda表达式本质上是一个匿名函数，他可以实现函数式接口的方法，函数式编程，可以简化代码，但是会增加代码理解的难度。

**注意lambda表达式只能实现函数式接口**

# 函数式接口

- 只包含一个抽象方法（Single Abstract Method，简称SAM）的接口，称为函数式接口。当然该接口可以包含其他非抽象方法。**即实现接口必须要重写的方法只有一个的是函数式接口**
- 你可以通过 Lambda 表达式来创建该接口的对象。（若 Lambda 表达式抛出一个受检异常(即：非运行时异常)，那么该异常需要在目标接口的抽象方法上进行声明）。
- 我们可以在一个接口上使用 `@FunctionalInterface `注解，这样做可以检查它是否是一个函数式接口。同时 javadoc 也会包含一条声明，说明这个接口是一个函数式接口。
- 在`java.util.function`包下定义了Java 8 的丰富的函数式接口

```java
//函数式接口示例
@FunctionalInterface
public interface face {
    //必须要实现的方法
    public abstract int show(int a);
    
    //default不必须重写
    default void show1(){
        System.out.println("default");
    }
    //static不必须重写
    static void show2(){
        System.out.println("static");
    }
    //toString不必须重写
    String toString();
}
```

# lambda表达式基础用法

格式:

```
(参数)->{

方法体;

};
```

```java
public class Test<T>{

    public static void main(String[] args) {
        
        //利用lambda实现四个接口
        
        Int1 int1=()->{
            System.out.println("无参无返回值的接口");
        };

        int1.show();
        
        Int2 int2=(String a)->{
            System.out.println(a);
        };
        int2.show("单参数无返回值的接口");
        
        Int3 int3=(String a,int b)->{
            System.out.println(a+b);
        };
        int3.show("string a ",100);
        
        Int4 int4=(String a)->{
            return a;
        };
        System.out.println(int4.show("有返回值的接口"));
    }


    private interface Int1{
        public abstract void show();
    }

    private interface Int2{
        public abstract void show(String a);
    }
    
    private interface Int3{
        public abstract void show(String a,int b);
    }
    
    private interface Int4{
        public abstract String show(String a);
    }
}
```

# lambda表达式进阶

在定义函数式接口时，由于已经定义过了参数的类型，所以lambda表达式可以不写参数类型

上方例四就可以这样写：

```java
Int4 int4=(a)->{
    return a;
};
System.out.println(int4.show("有返回值的接口"));
```

当参数只有一个,还能进一步省略括号

```java
Int4 int4 = a ->{
    return a;
};
System.out.println(int4.show("有返回值的接口"));
```

当方法体只有一句代码，还可以省略{}，return不要写：

```java
Int4 int4 = a -> a;
System.out.println(int4.show("有返回值的接口"));
```

# 函数引用

当逻辑过于复杂，不应该在lambda表达式中写，可以使用函数引用，简化lambda表达式的写法。

- 情况1：对象 :: 实例方法名

- 情况2：类 :: 静态方法名

- 情况3：类 :: 实例方法名


引用非静态方法，需要新建一个对象进行引用，例如`new Test()::calculate`。calculate为Test类实例化后的一个实例方法

```java
public class Test<T>{


    private interface Sum{
        public abstract int calculate(int a, int b);
    }
    public static int calculate(int a,int b){
        if(a>b){
            return a-b;
        }
        else if(a<b) {
            return b - a;
        }
        return a+b;
    }
    public static void main(String[] args) {
        //不是方法引用
        Sum sum=(a,b)->calculate(a,b);

        //采用方法引用,通过 类名::静态方法名 的方式引用静态方法
        Sum sum1= Test::calculate;
        

        System.out.println(sum.calculate(2,2));

    }
}
```

**需要注意的是引用的方法需要 返回值类型和参数列表 和函数式接口的方法一样**

# 构造方法引用

当一个函数式接口仅仅是为了获得一个类的实例对象，我们可以用构造方法引用简化接口的实现

通过 类名::new 可以引用构造方法

```java
public class Test<T>{
    private static class Pig{
        String name;
        int age;

        Pig(){
            System.out.println("Pig的无参构造");
        }

        public Pig(String name, int age) {
            this.name = name;
            this.age = age;
            System.out.println("Pig的双参构造");
        }
    }
    @FunctionalInterface
    private interface getPig{
        public abstract Pig get();
    }

    @FunctionalInterface
    private interface getPig2{
        public abstract Pig get(String name,int age);
    }
    public static void main(String[] args) {
        //类名::new,获取Pig的无参构造
        getPig getPig= Pig::new;
        Pig pig=getPig.get();//Pig的无参构造

        //带参数的构造方法引用
        getPig2 getPig2=Pig::new;
        Pig pig1=getPig2.get("小猪",18);//Pig的双参构造


    }
}
```