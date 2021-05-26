package com.example.jltfmoban5;

import com.huawei.ailifeability.DeviceMgrAbility;
import ohos.ace.ability.AceAbility;
import ohos.aafwk.content.Intent;

/**
 * main Ability
 */
public class MainAbility extends AceAbility {
    private static final String FA_DEVICE_ID = "feature_ability_device_id";
    private static final String FA_DEVICE_PRODUCT_ID = "feature_ability_product_id";

    @Override
    public void onStart(Intent intent) {
        DeviceMgrAbility.register(this);
        String deviceId = intent.getStringParam(FA_DEVICE_ID);
        String productId = intent.getStringParam(FA_DEVICE_PRODUCT_ID);
        DataHandlerAbility.register(this, deviceId, productId);
        super.onStart(intent);
    }

    @Override
    public void onStop() {
        DeviceMgrAbility.deregister();
        DataHandlerAbility.deregister();
        super.onStop();
    }
}
