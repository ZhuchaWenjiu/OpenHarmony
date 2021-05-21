
#include "hi_wifi_api.h"
#include "lwip/ip_addr.h"
#include "lwip/netifapi.h"
#include <stdio.h>

#include <unistd.h>
#include "ohos_init.h"
#include "cmsis_os2.h"

#define APP_INIT_VAP_NUM 2
#define APP_INIT_USR_NUM 2

static struct netif *g_lwip_netif = NULL;

static void WifiAPTask(void *arg)
{
    arg = arg;
    int ret;
    errno_t rc;
    char ifname[WIFI_IFNAME_MAX_SIZE + 1] = {0}; //存放ssid
    int len = sizeof(ifname);
    hi_wifi_softap_config hapd_conf = {0};
    ip4_addr_t st_gw;
    ip4_addr_t st_ipaddr;
    ip4_addr_t st_netmask;

    rc = memcpy_s(hapd_conf.ssid, HI_WIFI_MAX_SSID_LEN + 1, "Hispark-WiFi-IoT", 16); 
    if (rc != EOK)
    {
        return;
    }

    //gyz add 2020.11.04 设置密码
    rc = memcpy_s(hapd_conf.key, HI_WIFI_MAX_KEY_LEN + 1, "12345678", 8); 
    if (rc != EOK)
    {
        return;
    }
    // hapd_conf.authmode = HI_WIFI_SECURITY_OPEN;
    //gyz add 2020.11.04 设置加密模式
    hapd_conf.authmode = HI_WIFI_SECURITY_WPA2PSK; //
    hapd_conf.channel_num = 1;

    ret = hi_wifi_softap_start(&hapd_conf, ifname, &len);
    if (ret != HISI_OK)
    {
        printf("hi_wifi_softap_start\n");
        return;
    }

    /* acquire netif for IP operation */
    g_lwip_netif = netifapi_netif_find(ifname);
    if (g_lwip_netif == NULL)
    {
        printf("%s: get netif failed\n", __FUNCTION__);
        return;
    }

    IP4_ADDR(&st_gw, 192, 168, 10, 1);       /* input your IP for example: 192.168.1.1 */
    IP4_ADDR(&st_ipaddr, 192, 168, 10, 1);   /* input your netmask for example: 192.168.1.1 */
    IP4_ADDR(&st_netmask, 255, 255, 255, 0); /* input your gateway for example: 255.255.255.0 */
    netifapi_netif_set_addr(g_lwip_netif, &st_ipaddr, &st_netmask, &st_gw);

    netifapi_dhcps_start(g_lwip_netif, 0, 0);
}

static void WifiApDemo(void)
{
    osThreadAttr_t attr;

    attr.name = "WifiAPTask";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 10240;
    attr.priority = osPriorityNormal;

    if (osThreadNew(WifiAPTask, NULL, &attr) == NULL)
    {
        printf("[WifiAPTask] Falied to create WifiConnectTask!\n");
    }
}

APP_FEATURE_INIT(WifiApDemo);