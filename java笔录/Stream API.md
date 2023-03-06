# 1.Stream API

Stream API是java8的新特性，它提供了对集合类（ArrayList、HashMap等）的操作，可以对集合类中的数据进行各种过滤、查找、计算等操作。

使用stream的三个步骤：

- 1.创建stream
- 2.中间操作，对stream执行多种操作，在终止操作前不会真正执行。
- 3.终止操作，执行了终止操作后的steam流不能再次使用

# 2.创建stream的方法

```java
public class Test<T>{

    public static void main(String[] args) {

        //1.通过集合类的stream()方法获取stream
        //default Stream stream() : 返回一个顺序流
        //default Stream parallelStream() : 返回一个并行流
        List<Integer> list1= Arrays.asList(1,2,3,4,5);
        Stream stream=list1.stream();

        //通过Arrays的stream()方法
        //static Stream stream(T[] array): 返回一个流
        //public static IntStream stream(int[] array)
        //public static LongStream stream(long[] array)
        //public static DoubleStream stream(double[] array)
        int[] list2={1,2,3,4,5};
        IntStream stream1=Arrays.stream(list2);

        //通过Stream.of()
        //public static Stream of(T… values) : 返回一个流
        Stream<Integer> stream2 = Stream.of(1,2,3,4,5);
    }
}
```

# 3.中间操作

多个中间操作可以连接起来形成一个流水线，除非流水线上触发终止操作，否则中间操作不会执行任何的处理！而在终止操作时一次性全部处理，称为“惰性求值”。

常用方法:

|              方法               | 描述                                                         |
| :-----------------------------: | ------------------------------------------------------------ |
|       filter(Predicatep)        | 接收 Lambda ， 从流中排除某些元素                            |
|           distinct()            | 筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素 |
|       limit(long maxSize)       | 截断流，使其元素不超过给定数量                               |
|          skip(long n)           | 跳过元素，返回一个扔掉了前 n 个元素的流。 若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补 |
|         map(Function f)         | 接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。 |
| mapToDouble(ToDoubleFunction f) | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的 DoubleStream。 |
|    mapToInt(ToIntFunction f)    | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的 IntStream。 |
|   mapToLong(ToLongFunction f)   | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的 LongStream。 |
|       flatMap(Function f)       | 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流 |
|            sorted()             | 产生一个新流，其中按自然顺序排序                             |
|     sorted(Comparator com)      | 产生一个新流，其中按比较器顺序排序                           |

# 4.终止操作

流进行了终止操作后，不能再次使用。**终止操作要在中间操作后调用**

| 方法                                 | 描述                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| allMatch(Predicate p)                | 检查是否匹配所有元素                                         |
| **anyMatch(Predicate p) **           | 检查是否至少匹配一个元素                                     |
| noneMatch(Predicate p)               | 检查是否没有匹配所有元素                                     |
| findFirst()                          | 返回第一个元素                                               |
| findAny()                            | 返回当前流中的任意元素                                       |
| count()                              | 返回流中元素总数                                             |
| max(Comparator c)                    | 返回流中最大值                                               |
| min(Comparator c)                    | 返回流中最小值                                               |
| forEach(Consumer c)                  | 内部迭代(使用 Collection 接口需要用户去做迭代，称为外部迭代。 相反，Stream API 使用内部迭代——它帮你把迭代做了) |
| reduce(T identity, BinaryOperator b) | 可以将流中元素反复结合起来，得到一个值。返回 T               |
| reduce(BinaryOperator b)             | 可以将流中元素反复结合起来，得到一个值。返回 Optional        |
| collect(Collector c)                 | 将流转换为其他形式。接收一个 Collector接口的实现， 用于给Stream中元素做汇总的方法 |

```java
public class Test<T>{

    public static void main(String[] args) {

        //使用filter筛选大于1的数字并用forEach打印
        Stream<Integer> stream=Stream.of(1,2,3,4,5);
        stream.filter(a->a>1).forEach(a-> System.out.printf("%d ",a));//2 3 4 5 
        System.out.println();
        //使用filter筛选大于1的数字并用forEach打印
        Stream<Integer> stream1=Stream.of(5,4,3,2,1);//1 2 3 4 5 
        //默认升序排序
        stream1.sorted().forEach(a-> System.out.printf("%d ",a));
        
    }
}
```

