#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifiiot_i2c.h"
#include "wifiiot_gpio.h"
#include "wifiiot_gpio_ex.h"
#include "wifiiot_adc.h"
#include "wifiiot_errno.h"

#include "aht20.h"
#include "oled_ssd1306.h"

#define GAS_SENSOR_CHAN_NAME WIFI_IOT_ADC_CHANNEL_5
#define AHT20_BAUDRATE 400 * 1000
#define AHT20_I2C_IDX WIFI_IOT_I2C_IDX_0
#define ADC_RESOLUTION 2048

uint32_t retval = 0;
float humidity = 0.0f;  //湿度
float temperature = 0.0f;   //温度
unsigned short data = 0;   
static char line[32] = {0};   

//初始化函数
void init(void)
{
    GpioInit();
    OledInit();
    OledFillScreen(0);
    I2cInit(AHT20_I2C_IDX, AHT20_BAUDRATE);

    while (WIFI_IOT_SUCCESS != AHT20_Calibrate())
    {
        printf("AHT20 sensor init failed!\r\n");
        usleep(1000);
    }
}

//综合业务函数 
static void EnvironmentTask(void *arg)
{
    (void)arg;
    init();
    while (1){
        // 发送 触发测量 命令，开始测量
        if (AHT20_StartMeasure() != WIFI_IOT_SUCCESS){
            printf("trigger measure failed!\r\n");
        }
        // 接收测量结果，拼接转换为标准值
        if (AHT20_GetMeasureResult(&temperature, &humidity) != WIFI_IOT_SUCCESS){
            printf("get humidity data failed!\r\n");
        }
        //读取可燃气体值
        AdcRead(GAS_SENSOR_CHAN_NAME, &data, WIFI_IOT_ADC_EQU_MODEL_4, WIFI_IOT_ADC_CUR_BAIS_DEFAULT, 0);
        //在（0，0）位置显示字符串"Sensor values:",字体为6x8点阵
        OledShowString(0, 0, "Sensor values:", 1);

        //组装显示温度的字符串
        snprintf(line, sizeof(line), "temp: %.2f", temperature);
        OledShowString(0, 1, line, 1);//在（0，1）位置显示组装后的温度字符串

        //组装显示湿度的字符串
        snprintf(line, sizeof(line), "humi: %.2f", humidity);
        OledShowString(0, 2, line, 1);//在（0，2）位置显示组装后的湿度字符串

        //组装显示气体的字符串
        snprintf(line, sizeof(line), "gas: %d", data);
        OledShowString(0, 3, line, 1);//在（0，3）位置显示组装后的气体字符串
        sleep(1);//睡眠1秒
    }
}

//创建新线程运行EnvironmentTask函数
static void EnvironmentDemo(void)
{
    osThreadAttr_t attr;
    attr.name = "EnvironmentTask";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 4096;
    attr.priority = osPriorityNormal;

    if (osThreadNew(EnvironmentTask, NULL, &attr) == NULL)
    {
        printf("[EnvironmentDemo] Falied to create EnvironmentTask!\n");
    }
}

APP_FEATURE_INIT(EnvironmentDemo);//注册为自启动运行