# 1.构建centos

1. 新建目录`mkdir docker-hadoop`

2. 使用`re -Z` 上传`jdk1.8`的`tar.gz`压缩包

3. 编辑`Dockerfile`文件，`vim Dockerfile`

    ```dockerfile
    FROM centos:7.5.1804
    # 安装jdk
    RUN mkdir -p /opt/software && mkdir -p /opt/service
    ADD jre-8u361-linux-x64.tar.gz /opt/service
    
    # 安装语音包
    RUN yum -y install kde-l10n-Chinese glibc-commom vim
    RUN localedef -c -f UTF-8 -i zh_CN zh_CN.utf8
    RUN echo "LANG=zh_CN.UTF-8">>/etc/locale.conf
    
    # 解决login环境变量失效问题
    RUN touch /etc/profile.d/my_env.sh
    RUN echo -e "export LC_ALL=zh_CN.UTF-8\nexport JAVA_HOME=/opt/service/jre1.8.0_361\nexport PATH=\$PATH:\$JAVA_HOME/bin" >>/etc/profile.d/my_env.sh 
    
    # 安装ssh服务并更换阿里云yum源
    RUN curl -o /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo
    RUN sed -i -e '/mirrors.cloud.aliyuncs.com/d' -e '/mirrors.aliyuncs.com/d' /etc/yum.repos.d/CentOS-Base.repo
    RUN yum makecache
    
    RUN yum install -y openssh-server openssh-clients  net-tools lrzsz
    RUN sed -i '/^HostKey/'d /etc/ssh/sshd_config
    RUN echo 'HostKey /etc/ssh/ssh_host_rsa_key' >> /etc/ssh/sshd_config
    
    # 生成ssh key
    RUN ssh-keygen -t rsa -b 2048 -f /etc/ssh/ssh_host_rsa_key
    
    # 更改root用户登录密码
    RUN echo 'root:123456' | chpasswd
    # 暴露22端口
    EXPOSE 22
    RUN echo '#!/bin/bash'>>/opt/run.sh
    RUN echo '/usr/sbin/sshd -D'>> /opt/run.sh
    RUN chmod +x /opt/run.sh
    CMD ["/opt/run.sh"]
    ```

4. build镜像 `docker build -t centos_ssh_java:1.0 ./`

# 2.搭建hadoop

1. 使用以下命令创建三个容器，命名为hadoop102~104

    - `docker create -it --name hadoop102 -h hadoop102 centos_ssh_java:1.0`
    - `docker create -it --name hadoop103 -h hadoop103 centos_ssh_java:1.0`
    - `docker create -it --name hadoop104 -h hadoop104 centos_ssh_java:1.0` 

2. 创建docker配置脚本

    1. `cd /usr/local/bin`

    2. `vim docker.sh`

    3. 输入以下脚本

        ```sh
        #!/bin/bash
        #启动容器
        docker start hadoop102
        docker start hadoop103
        docker start hadoop104
        #搭建网桥，网段和网关记得填自己虚拟机的
        brctl addbr br0;\
        ip link set dev br0 up;\
        ip addr del 192.168.50.108/24 dev ens33;\
        ip addr add 192.168.50.108/24 dev br0;\
        brctl addif br0 ens33;\
        ip route add default via 192.168.50.1 dev br0
        #睡5秒钟
        sleep 5
        #给容器配置ip和网关
        pipework br0 hadoop102 192.168.50.102/24@192.168.50.2
        pipework br0 hadoop103 192.168.50.103/24@192.168.50.2
        pipework br0 hadoop104 192.168.50.104/24@192.168.50.2
        ```

    4. `chmod 777 docker.sh`，修改权限

    5. 运行`docker.sh`
    
3. 依次进入三个容器。使用`vim /etc/host`修改主机ip映射

    ```
    172.17.0.4      hadoop104
    172.17.0.3      hadoop103
    172.17.0.2      hadoop102
    ```

4. 安装hadoop

    1. `cd /opt/software/`

    2. `rz-E`将hadoop压缩包上传到这里

    3. `tar -zxvf hadoop-3.1.3.tar.gz -C /opt/service/`：解压

    4. `vim /etc/profile.d/my_env.sh`添加hadoop的环境变量

        ```sh
        export LC_ALL=zh_CN.UTF-8
        export JAVA_HOME=/opt/service/jre1.8.0_361
        export PATH=$PATH:$JAVA_HOME/bin
        
        #HADOOP_HOME
        export HADOOP_HOME=/opt/service/hadoop-3.1.3
        export PATH=$PATH:$HADOOP_HOME/bin
        export PATH=$PATH:$HADOOP_HOME/sbin
        ```

    5. `source /etc/profile.d/my_env.sh`:令环境变量生效

    6. `hadoop version`：测试安装是否成功

    7. 进入hadoop存放配置文件的目录`cd /opt/service/hadoop-3.1.3/etc/hadoop`

        `vim core-site.xml` 修改core-site.xml

        ```xml
        <configuration>
            <!-- 指定NameNode的地址 -->
            <property>
                <name>fs.defaultFS</name>
                <value>hdfs://hadoop102:8020</value>
            </property>
        
            <!-- 指定hadoop数据的存储目录 -->
            <property>
                <name>hadoop.tmp.dir</name>
                <value>/opt/service/hadoop-3.1.3/data</value>
            </property>
        
            <!-- 配置HDFS网页登录使用的静态用户为root -->
            <property>
                <name>hadoop.http.staticuser.user</name>
                <value>root</value>
            </property>
        </configuration>
        ```

        `vim hdfs-site.xml` 

        ```xml
        <configuration>
        	<!-- nn web端访问地址-->
        	<property>
                <name>dfs.namenode.http-address</name>
                <value>hadoop102:9870</value>
            </property>
        	<!-- 2nn web端访问地址-->
            <property>
                <name>dfs.namenode.secondary.http-address</name>
                <value>hadoop104:9868</value>
            </property>
        </configuration>
        ```

        `vim yarn-site.xml`

        ```xml
        <configuration>
            <!-- 指定MR走shuffle -->
            <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
            </property>
        
            <!-- 指定ResourceManager的地址-->
            <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>hadoop103</value>
            </property>
        
            <!-- 环境变量的继承 -->
            <property>
                <name>yarn.nodemanager.env-whitelist</name>
                <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
            </property>
        </configuration>
        ```

        `vim mapred-site.xml`

        ```xml
        <configuration>
        	<!-- 指定MapReduce程序运行在Yarn上 -->
            <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
            </property>
        </configuration>
        ```

    8. 修改配置文件workers，`vim /opt/service/hadoop-3.1.3/etc/hadoop/workers`，把原本的localhost删除

        ```
        hadoop102
        hadoop103
        hadoop104
        ```

        注意：该文件中添加的内容结尾不允许有空格，文件中不允许有空行

    9. 配置历史服务器
        `vim mapred-site.xml`

        ```xml
        <!-- 历史服务器端地址 -->
        <property>
            <name>mapreduce.jobhistory.address</name>
            <value>hadoop102:10020</value>
        </property>
        
        <!-- 历史服务器web端地址 -->
        <property>
            <name>mapreduce.jobhistory.webapp.address</name>
            <value>hadoop102:19888</value>
        </property>
        ```

    10. 配置日志
        日志聚集概念：应用运行完成以后，将程序运行日志信息上传到HDFS系统上。

        日志聚集功能好处：可以方便的查看到程序运行详情，方便开发调试。

        注意：开启日志聚集功能，需要重新启动NodeManager 、ResourceManager和HistoryServer。

        `vim yarn-site.xml`

        ```xml
        <!-- 开启日志聚集功能 -->
        <property>
            <name>yarn.log-aggregation-enable</name>
            <value>true</value>
        </property>
        <!-- 设置日志聚集服务器地址 -->
        <property>  
            <name>yarn.log.server.url</name>  
            <value>http://hadoop102:19888/jobhistory/logs</value>
        </property>
        <!-- 设置日志保留时间为7天 -->
        <property>
            <name>yarn.log-aggregation.retain-seconds</name>
            <value>604800</value>
        </property>
        ```

    11. 以上的步骤都是在hadoop102上设置的，我们需要用scp把这些设置分发到其他两个服务器上，更新他们的配置。

         `scp -r /opt/service/hadoop-3.1.3 hadoop103:/opt/service`

         `scp -r /opt/service/hadoop-3.1.3 hadoop104:/opt/service`

         `scp -r /etc/profile.d/my_env.sh hadoop103:/etc/profile.d`

         `scp -r /etc/profile.d/my_env.sh hadoop104:/etc/profile.d`

    12. 

    

    

    

    


​    

