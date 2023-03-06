# 1.事务

事务具有原子性，一旦执行，就必须全部执行完毕，否则回滚已经执行的操作，回滚到未执行前的状态。

# 2.使用事务

使用事务时需要手动关闭mysql的自动事务提交，同时注意事务中的操作必须使用同一个链接connection。

下面例子try中的操作如果没有全部执行成功，将会回滚到未执行的状态。

```java
public static void main(String[] args) throws ClassNotFoundException, SQLException {
    //注册驱动
    Class.forName("com.mysql.cj.jdbc.Driver");
    //创建连接
    Connection connection = DriverManager.getConnection(URL, DBNAME, PASSWORD);
    //创建statement
    String sql = "select id as i ,name from person";
    String sql1 = "update person set name='夏爱' where id=2;";
    try {
        //关闭自动事务提交
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement(sql1);
        preparedStatement.executeUpdate();
        //事务手动提交
        connection.commit();
    }catch (Exception e){
        //事务回滚
        connection.rollback();
        System.out.println(e);
    }

}
```

