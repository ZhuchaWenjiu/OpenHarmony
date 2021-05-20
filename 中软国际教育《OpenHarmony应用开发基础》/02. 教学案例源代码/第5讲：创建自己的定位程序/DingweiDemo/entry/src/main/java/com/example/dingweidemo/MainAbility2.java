package com.example.dingweidemo;

import com.example.dingweidemo.callback.MyLocatorCallback;
import com.example.dingweidemo.slice.MainAbility2Slice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.location.Locator;
import ohos.location.RequestParam;

public class MainAbility2 extends Ability {
    final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbility2Slice.class.getName());
        Locator locator = new Locator(this);
        RequestParam requestParam = new RequestParam(RequestParam.SCENE_NAVIGATION);
//        RequestParam requestParam = new RequestParam(RequestParam.PRIORITY_ACCURACY,0,0);
        MyLocatorCallback locatorCallback = new MyLocatorCallback();
        locator.startLocating(requestParam, locatorCallback);
        if (verifySelfPermission("ohos.permission.LOCATION") != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限
            if (canRequestPermission("ohos.permission.LOCATION")) {
                // 是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
                requestPermissionsFromUser(
                        new String[] { "ohos.permission.LOCATION" } , MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
            }
        } else {
            // 权限已被授予
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                // 匹配requestPermissions的requestCode
                if (grantResults.length > 0
                        && grantResults[0] == IBundleManager.PERMISSION_GRANTED) {
                    // 权限被授予
                    // 注意：因时间差导致接口权限检查时有无权限，所以对那些因无权限而抛异常的接口进行异常捕获处理
                } else {
                    // 权限被拒绝
                }
                return;
            }
        }
    }
}
