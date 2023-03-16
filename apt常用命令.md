apt（Advanced Packaging Tool）是一个在 Debian 和 Ubuntu 中的 Shell 前端软件包管理器。

apt 命令提供了查找、安装、升级、删除某一个、一组甚至全部软件包的命令，而且命令简洁而又好记。

apt 命令执行需要超级管理员权限(root)。

# 1.升级、安装

`apt-get update`:更新源文件，并不会做任何安装升级操作
`apt-get upgrade`:升级所有已安装的包
`apt-get install packagename`:安装指定的包
`apt-get install packagename --only-upgrad`:仅升级指定的包
`apt-get install packagename --reinstall`:重新安装包
`apt-get -f install`:修复安装
`apt-get build-dep packagename`:安装相关的编译环境
`apt-get source packagename`:下载该包的源代码
`apt-get dist-upgrade`:升级系统
`apt-get dselect-upgrade`:使用 dselect 升级
# 2.查询、显示
`apt-cache search packagename`：查询指定的包  　　
`apt-cache show packagename`：显示包的相关信息，如说明、大小、版本等 
`apt-cache depends packagename`：了解使用该包依赖哪些包
`apt-cache rdepends packagename`：查看该包被哪些包依赖
`dpkg -l`:查看所有安装的包

# 3.删除

`apt-get remove packagename`：删除包  　　
`apt-get remove packagename -- purge`：删除包，包括删除配置文件等 
`apt-get autoremove packagename --purge`：删除包及其依赖的软件包+配置文件等（只对6.10有效，推荐使用）
# 4.清理、检查

`apt-get clean`：清理无用的包 
`apt-get autoclean`：清理无用的包 
`apt-get check`：检查是否有损坏的依赖

# 5.参数

- -h 		帮助文件。  
- -q 		输出到日志 - 无进展指示  
- -qq 		不输出信息，错误除外  
- -d 		仅下载 - 不安装或解压归档文件  
- -s 		不实际安装。模拟执行命令  
- -y 		在需要确认的场景中回应 yes
- -f 		尝试修正系统依赖损坏处  
- -m 		如果归档无法定位，尝试继续  
- -u 		同时显示更新软件包的列表  
- -b 		获取源码包后编译  
- -V 		显示详细的版本号  
- -c=? 		阅读此配置文件  
- -o=? 		设置自定的配置选项，如 -o dir::cache=/tmp  
