#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifi_device.h"
#include "lwip/netifapi.h"
#include "lwip/api_shell.h"
#include "wifi_utils.h"
#include "tcp_server.h"
#include "tcp_client.h"
#include "udp_server.h"
#include "udp_client.h"

static void NetWorkTask(void *arg){
    (void)arg;
    connect_wifi();
    printf("begin demo\n");

    //  unsigned short port = 5001;
    //  tcp_server(port);

    // unsigned short port = 5002;
    // conent_tcp_server("192.168.0.104",port);

    // unsigned short port = 5001;
    // udp_server(port);

    unsigned short port = 5002;
    send_to_udp_server("192.168.0.104", port);
}

static void NetWorkDemo(void)
{
    osThreadAttr_t attr;

    attr.name = "NetWorkTask";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 10240;
    attr.priority = osPriorityNormal;

    if (osThreadNew(NetWorkTask, NULL, &attr) == NULL)
    {
        printf("[NetWorkDemo] Falied to create WifiConnectTask!\n");
    }
}

APP_FEATURE_INIT(NetWorkDemo);