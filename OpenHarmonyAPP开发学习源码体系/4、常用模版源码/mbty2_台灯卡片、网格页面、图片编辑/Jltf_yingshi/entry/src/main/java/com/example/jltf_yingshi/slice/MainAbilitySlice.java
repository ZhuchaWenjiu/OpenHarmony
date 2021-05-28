package com.example.jltf_yingshi.slice;

import com.example.jltf_yingshi.utils.AppUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.bundle.AbilityInfo;
import ohos.global.configuration.Configuration;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * Slice bound with MainAbility
 */
public class MainAbilitySlice extends AbilitySlice {
    private ViewCreateHelper viewCreateHelper;

    @Override
    public void onStart(Intent intent) {
        viewCreateHelper = new ViewCreateHelper(this);
        if (AppUtils.getDirection(this) == Configuration.DIRECTION_HORIZONTAL) {
            setUIContent(viewCreateHelper.createComponentLandscape());
        } else {
            setUIContent(viewCreateHelper.createComponent());
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    protected void onOrientationChanged(AbilityInfo.DisplayOrientation displayOrientation) {
        HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0, "debug"), "onOrientationChanged:" + displayOrientation);
        if (displayOrientation == AbilityInfo.DisplayOrientation.LANDSCAPE) {
            setUIContent(viewCreateHelper.createComponentLandscape());
        } else {
            setUIContent(viewCreateHelper.createComponent());
        }
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
