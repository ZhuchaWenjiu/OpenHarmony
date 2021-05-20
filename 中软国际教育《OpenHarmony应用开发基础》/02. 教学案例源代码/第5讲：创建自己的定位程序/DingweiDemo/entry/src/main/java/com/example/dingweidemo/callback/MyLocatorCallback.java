package com.example.dingweidemo.callback;

import com.example.dingweidemo.utils.LogUtil;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.location.Location;
import ohos.location.LocatorCallback;

public class MyLocatorCallback implements LocatorCallback {
    @Override
    public void onLocationReport(Location location) {
        LogUtil.hiLogInfo("-----------------111onLocationReport-----------------");
        LogUtil.hiLogInfo("Altitude ========= "+location.getAltitude()); // 海拔
        LogUtil.hiLogInfo("Direction ========= "+location.getDirection()); // 方向
        LogUtil.hiLogInfo("Accuracy ========= "+location.getAccuracy()); // 精度
        LogUtil.hiLogInfo("Latitude ========= "+location.getLatitude()); // 纬度
        LogUtil.hiLogInfo("Longitude ========= "+location.getLongitude()); // 经度
    }

    @Override
    public void onStatusChanged(int type) {
        LogUtil.hiLogInfo("-----------------222onStatusChanged-----------------");
        LogUtil.hiLogInfo("-----------------type-----------------" + type);
    }

    @Override
    public void onErrorReport(int type) {
        LogUtil.hiLogInfo("-----------------333onErrorReport-----------------");
    }
}