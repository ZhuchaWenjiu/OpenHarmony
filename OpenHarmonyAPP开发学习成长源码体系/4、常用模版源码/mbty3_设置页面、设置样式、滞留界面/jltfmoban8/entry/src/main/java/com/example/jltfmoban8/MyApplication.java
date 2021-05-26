package com.example.jltfmoban8;

import ohos.aafwk.ability.AbilityPackage;
import ohos.app.Context;

/**
 * MyApplication
 */
public class MyApplication extends AbilityPackage {
    private static Context context;

    /**
     * Obtains Ability Context
     *
     * @return Ability Context
     */
    public static Context getAbilityContext() {
        return context;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        context = getApplicationContext();
    }
}
