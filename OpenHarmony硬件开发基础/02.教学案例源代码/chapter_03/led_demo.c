#include <stdio.h>
#include "ohos_init.h"
#include <unistd.h>
#include "wifiiot_gpio.h"
#include "wifiiot_gpio_ex.h"
#include "ohos_types.h"
void leddemo(void)
{
    GpioInit(); //初始化GPIO设备，使用GPIO管脚前调用

    //设置GPIO_9功能为IO
    IoSetFunc(WIFI_IOT_IO_NAME_GPIO_9, WIFI_IOT_IO_FUNC_GPIO_9_GPIO);
    //设置GPIO_9的为输出
    GpioSetDir(WIFI_IOT_IO_NAME_GPIO_9, WIFI_IOT_GPIO_DIR_OUT);
    //设置GPIO_9输出值为低电平。值为0
    GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_9, WIFI_IOT_GPIO_EDGE_FALL_LEVEL_LOW);

    printf("led 点亮\n");//在终端打印LED状态

    usleep(4000000);//等待4秒

    //设置GPIO_9输出值为高电平，LED熄灭
    GpioSetOutputVal(WIFI_IOT_IO_NAME_GPIO_9, WIFI_IOT_GPIO_EDGE_RISE_LEVEL_HIGH);
    printf("led 熄灭\n");
   
}
SYS_RUN(leddemo);//注册leddemo函数在启动阶段运行