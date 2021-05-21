

#ifndef AHT20_H
#define AHT20_H

#include <stdint.h>
// 发送初始化校准命令
uint32_t AHT20_Calibrate(void);
// 发送 触发测量 命令，开始测量
uint32_t AHT20_StartMeasure(void);
// 接收测量结果，拼接转换为标准值
uint32_t AHT20_GetMeasureResult(float* temp, float* humi);

#endif  // AHT20_H