# 1. 静态ip

## 1.1debian系列linux的ip设置

1. `vim /etc/network/interfaces`

2. 文件中添加静态ip设置，network和broadcast一般不用设置。

    ```properties
    auto eth0
    iface eth0 inet static
    address 192.168.1.3
    netmask 255.255.255.0 
    gateway 192.168.1.2 #网关
    # network 192.168.1.0 子网ip
    # broadcast 192.168.1.255 广播地址
    ```

3. 重启`service networking restart`

## 1.2centos的ip设置

如果是centos，要修改的文件是：`/etc/sysconfig/network-scripts/ifcfg-ens33`，有的centos版本默认网卡也改名成eth0了就修改`/etc/sysconfig/network-scripts/ifcfg-eth0`文件，修改后记得重启网络服务。

```properties
DEVICE=ens33
TYPE=Ethernet
ONBOOT=yes
BOOTPROTO=static
NAME="ens33"
IPADDR=192.168.10.102
PREFIX=24
GATEWAY=192.168.10.2
DNS1=192.168.10.2
```



# 2.修改主机名

方法一：

1. `vim /etc/hostname`

2. 在文件中修改主机名
3. 重启

方法二：使用`hostnamectl set-hostname 主机名`命令

# 3.添加ip与主机的映射

1. `vim /etc/hosts`

2. 格式：`ip 主机名`

    ```
    192.168.10.100 hadoop100
    192.168.10.101 hadoop101
    192.168.10.102 hadoop102
    192.168.10.103 hadoop103
    192.168.10.104 hadoop104
    ```

    添加后就可以直接通过ping 主机名的方式连通其他主机，并且主机的ip修改后只需要修改hosts文件即可，不在需要在代码中一个个修改ip
    
    windows中添加ip与主机映射需要修改文件`C:\Windows\System32\drivers\etc\hosts`,修改后保存副本，然后再用副本替换原来的hosts文件即可

