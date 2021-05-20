package com.example.dingweidemo.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LogUtil {
    static HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x00101, "DINGWEI_TAG");
    public static void hiLogInfo(String content){
        HiLog.info(label,content);
    }

}
