package com.example.rdbdemo.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LogUtil {
    static HiLogLabel hiLogLabel = new HiLogLabel(HiLog.LOG_APP,0x11,"mynote111");
    public static void inof(String content){
        HiLog.info(hiLogLabel,content);
    }

}
