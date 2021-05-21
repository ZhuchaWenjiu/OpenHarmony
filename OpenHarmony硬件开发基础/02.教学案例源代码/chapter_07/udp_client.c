#include <stdio.h>
#include <unistd.h>
#include "ohos_init.h"
#include "cmsis_os2.h"
#include "hi_wifi_api.h"
#include "lwip/ip_addr.h"
#include "lwip/netifapi.h"
#include "lwip/sockets.h"

static char request[] = "Hello. I am from chinasoft";
static char response[128] = "";

void send_to_udp_server(const char* host, unsigned short port)
{
    ssize_t retval = 0;
    int sockfd = socket(AF_INET, SOCK_DGRAM, 0); // UDP socket

    struct sockaddr_in toAddr = {0};
    toAddr.sin_family = AF_INET;
    toAddr.sin_port = htons(port); // 端口号，从主机字节序转为网络字节序
    if (inet_pton(AF_INET, host, &toAddr.sin_addr) <= 0) { // 将主机IP地址从“点分十进制”字符串 转化为 标准格式（32位整数）
        printf("inet_pton failed!\r\n");
        goto do_cleanup;
    }

    // UDP socket 是 “无连接的” ，因此每次发送都必须先指定目标主机和端口，主机可以是多播地址
    retval = sendto(sockfd, request, sizeof(request), 0, (struct sockaddr *)&toAddr, sizeof(toAddr));
    if (retval < 0) {
        printf("sendto failed!\r\n");
        goto do_cleanup;
    }
    printf("send UDP message {%s} %ld done!\r\n", request, retval);

    struct sockaddr_in fromAddr = {0};
    socklen_t fromLen = sizeof(fromAddr);

    // UDP socket 是 “无连接的” ，因此每次接收时前并不知道消息来自何处，通过 fromAddr 参数可以得到发送方的信息（主机、端口号）
    retval = recvfrom(sockfd, &response, sizeof(response), 0, (struct sockaddr *)&fromAddr, &fromLen);
    if (retval <= 0) {
        printf("recvfrom failed or abort, %ld, %d!\r\n", retval, errno);
        goto do_cleanup;
    }
    response[retval] = '\0';
    printf("recv UDP message {%s} %ld done!\r\n", response, retval);
    printf("peer info: ipaddr = %s, port = %d\r\n", inet_ntoa(fromAddr.sin_addr), ntohs(fromAddr.sin_port));

do_cleanup:
    printf("do_cleanup...\r\n");
    lwip_close(sockfd);
}

