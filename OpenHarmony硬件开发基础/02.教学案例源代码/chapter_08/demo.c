#include <stdio.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifi_device.h"
#include "lwip/netifapi.h"
#include "lwip/api_shell.h"
#include "wifi_utils.h"
#include "mqtt_test.h"
#include <at.h>
#include <hi_at.h>

static void mqtt_test_thread(void *arg)
{
    (void)arg;
    connect_wifi();//连接AP，
    printf("begin mqtt  demo\n");

    mqtt_test();//运行mqtt测试程序
}

//AT指令回调函数，在函数中创建新线程
hi_u32 at_exe_mqtt_test_cmd(void)
{
    osThreadAttr_t attr;

    attr.name = "wifi_config_thread";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 4096;
    attr.priority = 36;
    //在新线程中执行函数mqtt_test_thread
    if (osThreadNew((osThreadFunc_t)mqtt_test_thread, NULL, &attr) == NULL)
    {
        printf("[LedExample] Falied to create LedTask!\n");
    }

    AT_RESPONSE_OK;
    return HI_ERR_SUCCESS;
}

const at_cmd_func g_at_mqtt_func_tbl[] = {
    {"+MQTTTEST", 9, HI_NULL, HI_NULL, HI_NULL, (at_call_back_func)at_exe_mqtt_test_cmd},
};

//注册AT指令
void AtMqttTestEntry(void)
{
    hi_at_register_cmd(g_at_mqtt_func_tbl, sizeof(g_at_mqtt_func_tbl) / sizeof(g_at_mqtt_func_tbl[0]));
}
SYS_RUN(AtMqttTestEntry);