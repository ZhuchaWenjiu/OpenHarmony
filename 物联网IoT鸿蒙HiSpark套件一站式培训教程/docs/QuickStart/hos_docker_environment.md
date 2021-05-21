## 1. 使用存储库安装

在新主机上首次安装Docker Engine之前，需要设置Docker存储库。之后，您可以从存储库安装和更新Docker。

#### 1.1 设置存储库

1. 更新`apt`软件包索引并安装软件包以允许`apt`通过HTTPS使用存储库：

```
sudo apt-get update

sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common
```

2. 添加Docker的官方GPG密钥：

```
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

`9DC8 5822 9FC7 DD38 854A E2D8 8D81 803C 0EBF CD88`通过搜索指纹的后8个字符，验证您现在是否拥有带有指纹的密钥。

```
$ sudo apt-key fingerprint 0EBFCD88

pub   rsa4096 2017-02-22 [SCEA]
      9DC8 5822 9FC7 DD38 854A  E2D8 8D81 803C 0EBF CD88
uid           [ unknown] Docker Release (CE deb) <docker@docker.com>
sub   rsa4096 2017-02-22 [S]
```

3. 使用以下命令来设置**稳定的**存储库。要添加 **每晚**或**测试**存储库，请在以下命令中的单词后面添加`nightly`或`test`（或两者）`stable`。[了解**每晚**和**测试**频道](https://docs.docker.com/engine/install/)。

> **注意**：下面的`lsb_release -cs`子命令返回Ubuntu发行版的名称，例如`xenial`。有时，在Linux Mint等发行版中，您可能需要更改`$(lsb_release -cs)` 为父Ubuntu发行版。例如，如果您使用 `Linux Mint Tessa`，则可以使用`bionic`。Docker对未经测试和不受支持的Ubuntu发行版不提供任何保证。

> 

```
$ sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
```

#### 1.2 安装DOCKER引擎

1. 更新`apt`程序包索引，并安装*最新版本*的Docker Engine和容器，或转到下一步以安装特定版本：

   ```sh
   sudo apt-get update
   sudo apt-get install docker-ce docker-ce-cli containerd.io
   ```

   > 有多个Docker存储库吗？
   >
   > 如果启用了多个Docker存储库，则在未在`apt-get install`or `apt-get update`命令中指定版本的情况下进行安装或更新将始终安装可能的最高版本，这可能不适合您的稳定性需求。

2. 要安装*特定版本*的Docker Engine，请在存储库中列出可用版本，然后选择并安装：

   一种。列出您的仓库中可用的版本：

   ```
   $ apt-cache madison docker-ce
   
   docker-ce | 5:20.10.2~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:20.10.1~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:20.10.0~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:19.03.14~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:19.03.13~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:19.03.12~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:19.03.11~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:19.03.10~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
    docker-ce | 5:19.03.9~3-0~ubuntu-focal | https://download.docker.com/linux/ubuntu focal/stable amd64 Packages
     ...
   ```

   b。使用第二列中的版本字符串安装特定版本，例如`5:18.09.1~3-0~ubuntu-xenial`。

   ```sh
   例： sudo apt-get install docker-ce=<VERSION_STRING> docker-ce-cli=<VERSION_STRING> containerd.io
   
   $ sudo apt-get install docker-ce=<5:20.10.0~3-0~ubuntu-focal> docker-ce-cli=<5:20.10.0~3-0~ubuntu-focal> containerd.io
   ```

3. 通过运行`hello-world` 映像来验证是否正确安装了Docker Engine 。

   ```
   $ sudo docker run hello-world
   ```

   此命令下载测试图像并在容器中运行它。容器运行时，它会打印参考消息并退出。

**docker 安装完成后测试hello-world出现问题（Unable to find image 'hello-world:latest' locally）**

新建/etc/docker/daemon.json

```sh
vim /etc/docker/daemon.json
chmod 777 /etc/docker/daemon.json
#添加下面内容
#docker在本地没有找到hello-world镜像，也没有从docker仓库中拉取镜像，出项这个问题的原因：是应为#docker服务器再国外，我们在国内无法正常拉取镜像，所以就需要我们为docker设置国内阿里云的镜像加速器；
{ 
"registry-mirrors": ["https://alzgoonw.mirror.aliyuncs.com"] 
}

sudo systemctl restart docker
sudo systemctl status  docker
```

Docker Engine已安装并正在运行。该`docker`组已创建，但未添加任何用户。您需要使用`sudo`来运行Docker命令。继续进行[Linux后安装，](https://docs.docker.com/engine/install/linux-postinstall/)以允许非特权用户运行Docker命令以及其他可选配置步骤。

```
# 创建docker组
$ sudo groupadd docker

# 将您的用户添加到该docker组
$ sudo usermod -aG docker lxy

# 注销并重新登录，以便重新评估您的组成员身份。
$ newgrp docker 

# 验证您是否可以运行docker不带命令的命令sudo
$ docker run hello-world

```



## 2. 配置Docker以在启动时启动

当前大多数Linux发行版（RHEL，CentOS，Fedora，Debian，Ubuntu 16.04及更高版本）都[`systemd`](https://docs.docker.com/engine/install/linux-postinstall/#systemd)用于管理系统启动时启动的服务。在Debian和Ubuntu上，默认情况下，将Docker服务配置为在引导时启动。要在启动时自动启动其他发行版的Docker和Containerd，请使用以下命令：

```
$ sudo systemctl enable docker.service
$ sudo systemctl enable containerd.service
```

若要禁用此行为，请`disable`改用。

```
$ sudo systemctl disable docker.service
$ sudo systemctl disable containerd.service
```

如果需要添加HTTP代理，为Docker运行时文件设置不同的目录或分区，或进行其他自定义，请参阅 [自定义系统的Docker守护程序选项](https://docs.docker.com/config/daemon/systemd/)。



## 3. 镜像压缩包获取

云盘资料链接: https://pan.baidu.com/s/1uiiCpbJqViGb7Qs6HdCb8g 
提取码: ddab
“Pegasus物联网套件开发环境-docker镜像” 目录下面的 “wifiiot.tar”

## 4. Docker使用

```sh

# 接收到 tar 包之后，怎么使用 tar 包
sudo docker load  <  /(下载的tar包所在ubuntu服务器中的路径)/wifiiot.tar

sudo docker images #查看镜像
sudo docker run -dit --name=xxx --net=host 镜像的ID  /bin/bash

sudo docker ps -a #查看容器的ID
# 启动容器
sudo docker start 容器ID
sudo docker attach 容器ID
```

