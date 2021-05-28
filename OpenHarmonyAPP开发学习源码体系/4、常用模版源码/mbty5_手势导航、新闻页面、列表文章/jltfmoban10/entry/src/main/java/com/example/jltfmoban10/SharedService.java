package com.example.jltfmoban10;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.rpc.IRemoteObject;

import com.example.jltfmoban10.utils.LogUtil;
import com.example.jltfmoban10.manager.NewsDemoIDLStub;

/**
 * Shared Service
 */
public class SharedService extends Ability {
    private static final String DESCRIPTOR = "com.example.jltfmoban10.idl.INewsDemoIDL";

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        return new MyBinder(DESCRIPTOR);
    }

    /**
     * MyBinder demo
     */
    private class MyBinder extends NewsDemoIDLStub {
        MyBinder(String descriptor) {
            super(descriptor);
        }

        @Override
        public void tranShare(String deviceId, String shareUrl, String shareTitle,
                              String shareAbstract, String shareImg) {
            LogUtil.info("SharedService", "tranShare is called");
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withBundleName(getBundleName())
                    .withAbilityName("com.example.jltfmoban10.MainAbility")
                    .withAction("action.detail")
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
    }
}
