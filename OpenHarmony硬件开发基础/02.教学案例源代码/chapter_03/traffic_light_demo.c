#include <stdio.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifiiot_gpio.h"
#include "wifiiot_gpio_ex.h"

//对GPIO管脚进行初始化
void init(void)
{
    GpioInit();
    IoSetFunc(WIFI_IOT_IO_NAME_GPIO_10, WIFI_IOT_IO_FUNC_GPIO_10_GPIO);
    GpioSetDir(WIFI_IOT_IO_NAME_GPIO_10, WIFI_IOT_GPIO_DIR_OUT);

    IoSetFunc(WIFI_IOT_IO_NAME_GPIO_11, WIFI_IOT_IO_FUNC_GPIO_11_GPIO);
    GpioSetDir(WIFI_IOT_IO_NAME_GPIO_11, WIFI_IOT_GPIO_DIR_OUT);

    IoSetFunc(WIFI_IOT_IO_NAME_GPIO_12, WIFI_IOT_IO_FUNC_GPIO_12_GPIO);
    GpioSetDir(WIFI_IOT_IO_NAME_GPIO_12, WIFI_IOT_GPIO_DIR_OUT);
}

static void StartTrafficLightTask(void)
{
    init();
    while (1)
    {
        //绿灯亮
        GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_11, WIFI_IOT_GPIO_VALUE1);
        usleep(3000 * 1000);
        //绿灯灭、黄灯亮
        GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_11, WIFI_IOT_GPIO_VALUE0);
        GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_12, WIFI_IOT_GPIO_VALUE1);
        usleep(1000 * 1000);
        //黄灯灭、红灯这
        GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_12, WIFI_IOT_GPIO_VALUE0);
        GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_10, WIFI_IOT_GPIO_VALUE1);
        usleep(3000 * 1000);//10红灯
        GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_10, WIFI_IOT_GPIO_VALUE0);
    }
}

APP_FEATURE_INIT(StartTrafficLightTask);  //注册StartTrafficLightTask函数为系统启动后运行