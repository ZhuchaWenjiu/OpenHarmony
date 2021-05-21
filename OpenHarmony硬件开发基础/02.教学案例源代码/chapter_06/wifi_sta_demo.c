#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifi_device.h"
#include "lwip/netifapi.h"
#include "lwip/api_shell.h"



static void WifiStaTask(void *arg)
{
    (void)arg;
    WifiErrorCode errCode;
    WifiDeviceConfig apConfig = {};
    int netId = -1;

    // 设置将要连接的AP属性（wifi名称、密码、加密方式），
    // strcpy(apConfig.ssid, "TP-LINK_A673");//
    // strcpy(apConfig.preSharedKey, "aaaaaaaaaa");//密码
    strcpy(apConfig.ssid, "TP-LINK_D49F");//
    strcpy(apConfig.preSharedKey, "wyz315ao");//密码
    apConfig.securityType = WIFI_SEC_TYPE_PSK;//加密类型

    errCode = EnableWifi();//使能wifi功能
    errCode = AddDeviceConfig(&apConfig, &netId);//生成netId
    printf("AddDeviceConfig: %d\r\n", errCode);

    errCode = ConnectTo(netId);//连接到与指定的网络ID匹配的热点
    printf("ConnectTo(%d): %d\r\n", netId, errCode);
    usleep(3000 * 1000);//等待连接
    // 联网业务开始
    // 这里是网络业务代码...
    struct netif *iface = netifapi_netif_find("wlan0");
    if (iface)
    {
        err_t ret = netifapi_dhcp_start(iface);
        printf("netifapi_dhcp_start: %d\r\n", ret);

        usleep(2000 * 1000);
        ; // wait DHCP server give me IP
        ret = netifapi_netif_common(iface, dhcp_clients_info_show, NULL);
        printf("netifapi_netif_common: %d\r\n", ret);
    }
    usleep(5000*1000);
    // 联网业务结束
    Disconnect();        // disconnect with your AP
    RemoveDevice(netId); // remove AP config
    errCode = DisableWifi();
    printf("DisableWifi: %d\r\n", errCode);
}

static void WifiStaDemo(void)
{
    osThreadAttr_t attr;

    attr.name = "WifiStaTask";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 10240;
    attr.priority = osPriorityNormal;

    if (osThreadNew(WifiStaTask, NULL, &attr) == NULL)
    {
        printf("[WifiConnectDemo] Falied to create WifiConnectTask!\n");
    }
}

APP_FEATURE_INIT(WifiStaDemo);
