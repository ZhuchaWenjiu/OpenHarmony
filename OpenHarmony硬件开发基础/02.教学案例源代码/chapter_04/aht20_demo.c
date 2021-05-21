#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <unistd.h>

#include "ohos_init.h"
#include "cmsis_os2.h"
#include "wifiiot_i2c.h"
#include "wifiiot_gpio.h"
#include "wifiiot_gpio_ex.h"
#include "wifiiot_errno.h"
#include "aht20.h"

#define AHT20_BAUDRATE 400 * 1000
#define AHT20_I2C_IDX WIFI_IOT_I2C_IDX_0

void init(void)
{
    GpioInit();
    IoSetFunc(WIFI_IOT_IO_NAME_GPIO_13, WIFI_IOT_IO_FUNC_GPIO_13_I2C0_SDA);
    IoSetFunc(WIFI_IOT_IO_NAME_GPIO_14, WIFI_IOT_IO_FUNC_GPIO_14_I2C0_SCL);
    I2cInit(AHT20_I2C_IDX, AHT20_BAUDRATE);
}

static void AhtDemoTask(void *arg){
    (void)arg;
    init();//初始化IIC
    uint32_t retval = 0;
    float humidity = 0.0f;
    float temperature = 0.0f;
        // 发送初始化校准命令
    while (WIFI_IOT_SUCCESS != AHT20_Calibrate()){
        printf("AHT20 sensor init failed!\r\n");
        usleep(1000);
    }
    while (1){
        // 发送 触发测量 命令，开始测量
        retval = AHT20_StartMeasure();
        if (retval != WIFI_IOT_SUCCESS){
            printf("trigger measure failed!\r\n");
        }else {
            // 接收测量结果，拼接转换为标准值
            retval = AHT20_GetMeasureResult(&temperature, &humidity);
            printf("temp: %.2f,  humi: %.2f\n", temperature, humidity);
        }
        sleep(1);
    }
}

static void AhtDemo(void){
    osThreadAttr_t attr;
    
    attr.name = "AhtDemoTask";
    attr.attr_bits = 0U;
    attr.cb_mem = NULL;
    attr.cb_size = 0U;
    attr.stack_mem = NULL;
    attr.stack_size = 4096;
    attr.priority = osPriorityNormal;

    if (osThreadNew(AhtDemoTask, NULL, &attr) == NULL)
    {
        printf("[EnvironmentDemo] Falied to create AhtDemoTask!\n");
    }
}

APP_FEATURE_INIT(AhtDemo);