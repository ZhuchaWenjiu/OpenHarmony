#include <stdio.h>
#include <unistd.h>
#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifiiot_gpio.h"
#include "wifiiot_gpio_ex.h"
#include "oled_ssd1306.h"

//该函数对GPIO管脚及OLED进行初始化
void init(void)
{
    GpioInit();
    OledInit();
}

//业务函数，完成数据在OLED上显示
static void OledTask(void *arg)
{
    (void)arg;
    init(); //初始化
    OledFillScreen(0x00);//清屏，
    //在左上角位置显示字符串Hello, HarmonyOS
    OledShowString(0, 0, "Hello, HarmonyOS", 1);
    sleep(1);//等待1秒

    //循环3次显示
    for (int i = 0; i < 3; i++)
    {
        OledFillScreen(0x00);//清屏
        for (int y = 0; y < 8; y++)
        {//设置每行显示的字符串
            static const char text[] = "ABCDEFGHIJKLMNOP"; // QRSTUVWXYZ
            OledShowString(0, y, text, 1);//在(0,y)位置显示字符串
        }
        sleep(1);//等待1秒，
    }
}

//创建新线程运行OledTask函数
static void OledDemo(void)
{
    osThreadAttr_t attr;
    attr.name = "OledTask";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 4096;
    attr.priority = osPriorityNormal;
    if (osThreadNew(OledTask, NULL, &attr) == NULL)
    {
        printf("[OledDemo] Falied to create OledTask!\n");
    }
}
APP_FEATURE_INIT(OledDemo);
