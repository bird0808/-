# 1.Dockerfile

Docker可以通过从Dockerfile中读取指令来自动构建镜像。Dockerfile是一个文本文档，其中包含 用户可以在命令行上调用以组装镜像的所有命令。本页介绍了可以在Dockerfile中使用的命令。

dockerfile 可以为镜像指定各种环境，方便利用自定义的镜像快速部署。

构建步骤：

1. 编写Dockerfile文件

1. `docker build`命令构建镜像

1. `docker run`依据镜像运行容器实例

Dockerfile编写：

- 每条保留字指令都必须为大写字母，且后面要跟随至少一个参数

- 指令按照从上到下顺序执行

- `#`表示注释

- 每条指令都会创建一个新的镜像层并对镜像进行提交

Docker引擎执行Docker的大致流程：

1. docker从基础镜像运行一个容器
2. 执行一条指令并对容器做出修改
3. 执行类似`docker commit`的操作提交一个新的镜像层
4. docker再基于刚提交的镜像运行一个新容器
5. 执行Dockerfile中的下一条指令，直到所有指令都执行完成

# 2.保留字

- `FROM 镜像名` ：基础镜像，当前新镜像是基于哪个镜像的，指定一个已经存在的镜像作为模板。Dockerfile第一条必须是FROM

- `MAINTAINER 姓名 邮箱`：镜像维护者的姓名和邮箱地址

- `RUN shell命令`：容器构建时需要运行的命令，好比进入了容器时自己需要敲的命令，`RUN`是在`docker build`时运行。

- `EXPOSE`端口号[/网络协议]：当前容器对外暴露出的端口。默认协议TCP

- `ENV 环境变量名 变量值`:用来在构建镜像过程中设置环境变量。这个环境变量可以在后续使用`$环境变量名`来引用

- `WORKDIR`:指定在创建容器后， 终端默认登录进来的工作目录。

```dockerfile
# 创建一个环境变量，并设置终端默认进来的目录为环境变量的目录
ENV CATALINA_HOME /usr/local/tomcat
WORKDIR $CATALINA_HOME
```

- `USER <user>[:<group>]`：指定该镜像以什么样的用户去执行，如果不指定，默认是`root`。（一般不修改该配置）

- `VOLUME 文件路径`:挂载容器数据卷，用于数据保存和持久化工作。类似于 `docker run` 的`-v`参数。
    注意这里的文件路径是指容器内的挂载点，通过 VOLUME 指令创建的挂载点，无法指定主机上对应的目录，是自动生成的。

    可以通过`docker inspect` 查看通过该dockerfile创建的镜像生成的容器挂载点对应的宿主机目录

- `ADD 宿主机文件 容器目录`：将宿主机目录下（或远程文件）的文件拷贝进镜像，且会自动处理URL和解压tar压缩包。

- `COPY [--chown=<user>:<group>] <源路径1>...  <目标路径>`：复制指令，从上下文目录中复制文件或者目录到容器里指定路径。

    **[--chown=<user>:<group>]**：可选参数，用户改变复制到容器内文件的拥有者和属组。

- `CMD <命令>`：指定容器启动后要干的事情。Dockerfile中如果出现多个`CMD`指令，只有最后一个生效。`CMD`会被`docker run`之后的参数替换。

```dockerfile
# CMD ["可执行文件", "参数1", "参数2" ...]
# 因为tomcat的Dockerfile中指定了 CMD ["catalina.sh", "run"]
# 所以直接docker run 时，容器启动后会自动执行 catalina.sh run
docker run -it -p 8080:8080 tomcat

# 指定容器启动后执行 /bin/bash
# 此时指定的/bin/bash会覆盖掉Dockerfile中指定的 CMD ["catalina.sh", "run"]
docker run -it -p 8080:8080 tomcat /bin/bash
```



- `ENTRYPOINT`：用来指定一个容器启动时要运行的命令。类似于`CMD`命令，但是`ENTRYPOINT`不会被`docker run`后面的命令覆盖，这些命令参数会被当做参数送给`ENTRYPOINT`指令指定的程序。`ENTRYPOINT`可以和`CMD`一起用，一般是可变参数才会使用`CMD`，这里的`CMD`等于是在给`ENTRYPOINT`传参。当指定了`ENTRYPOINT`后，`CMD`的含义就发生了变化，不再是直接运行命令，而是将`CMD`的内容作为参数传递给`ENTRYPOINT`指令，它们两个组合会变成 `<ENTRYPOINT> "<CMD>"`。

    比如下面，`nginx -c`是开头前两个固定的命令，CMD的命令会作为参数添加到ENTRYPOINT这两个定参后面，而且CMD可以被docker run后面的命令替代。

    **注意**：如果 Dockerfile 中如果存在多个 ENTRYPOINT 指令，仅最后一个生效。

```dockerfile
ENTRYPOINT ["nginx", "-c"]  # 定参
CMD ["/etc/nginx/nginx.conf"] # 变参
```

示例：

新建一个空目录，再用vim Dockerfile新建一个文件，然后填入下面的内容构建一个带vim的Ubuntu

```
FROM ubuntu
MAINTAINER bird 160849360@qq.com

ENV MYPATH /usr/local
WORKDIR $MYPATH

# 更新apt
RUN apt-get update
# 安装vim
RUN apt-get -y install vim

CMD echo $MYPATH
CMD echo /bin/bash
```

填好后使用`docker build -t 镜像名：TAG .`根据刚刚的Dockerfile构建自己的镜像。`.`表示上下文路径，是指 docker 在构建镜像，有时候想要使用到本机的文件（比如复制），docker build 命令得知这个路径后，会将路径下的所有内容打包。如果未说明最后一个参数，那么默认上下文路径就是 Dockerfile 所在的位置。

**注意**：上下文路径下不要放无用的文件，因为会一起打包发送给 docker 引擎，如果文件过多会造成过程缓慢。