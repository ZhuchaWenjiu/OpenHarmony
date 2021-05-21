# 快速环境搭建

本文介绍如何使用VMware镜像文件快速完成鸿蒙开发环境搭建

## 一、硬件环境

- 一台windows电脑：

  - 安装好VMware

    VMware版本：[VMware-workstation-full-16.1.0-17198959](https://www.vmware.com/products/workstation-pro/workstation-pro-evaluation.html)

## 二、资料下载

- VMware镜像下载
  - https://pan.baidu.com/s/1uiiCpbJqViGb7Qs6HdCb8g 提取码: ddab

## 三、环境搭建

1、将下载好的`Ubuntu20.4`镜像`Pegasus_ubuntu20.4.rar`文件解压到某个目录下。

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_pegasus_ubuntu20.4.png)

2、打开VMware Workstation工具，将ovf文件导入到工程中，然后开启此虚拟机

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm.png)

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/import_vm_ovf.png)



3、进入登录页面后，登录用户为：`Pegasus`；密码为：`pegasus`

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_login.png)

4、登录之后，打开终端界面，进入代码目录`cd harmony/openharmony`；开始编译：`./build.py wifiiot`

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_compile.png)

5、查看编译结果

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_compile_success.png)

6、windows主机访问linux主机

该环境安装了该环境安装了samba，配置内容可以查看`/etc/samba/smb.conf`。账号与密码均为pegasus；

(1). 查询虚拟机的地址:打开终端界面`ifconfig`

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_ifconfig.png)

(2). 确保windows主机可以ping通虚拟机

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_ping.png)

(3). ping通主机后，可以将虚拟机的指定目录映射到网络驱动器

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_map_network_drive.png)

用户与密码均为：`pegasus`

映射成功后，打开网络驱动器，既可以直接获取虚拟机指定目录下的内容

![hihope_illustration](https://gitee.com/hihopeorg/hispark-hm-pegasus/raw/master/docs/figures/vm_file.png)

6、镜像烧录

请参考[使用HiBurn烧录固件到开发板](https://gitee.com/hihopeorg/HarmonyOS-IoT-Application-Development/blob/master/01_envsetup/hos_use_hiburn_download_firmware.md)















































