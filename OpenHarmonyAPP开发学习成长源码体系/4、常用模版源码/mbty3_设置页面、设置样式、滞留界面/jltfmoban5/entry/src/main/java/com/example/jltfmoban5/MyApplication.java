package com.example.jltfmoban5;

import ohos.aafwk.ability.AbilityPackage;
import ohos.app.ElementsCallback;
import ohos.global.configuration.Configuration;

/**
 * main AbilityPackage
 */
public class MyApplication extends AbilityPackage {
    private static MyApplication instance;
    private ElementsCallback configurationCallback;

    private final ElementsCallback elementsCallback = new ElementsCallback() {
        @Override
        public void onMemoryLevel(int i) {
        }

        @Override
        public void onConfigurationUpdated(Configuration configuration) {
            configurationCallback.onConfigurationUpdated(configuration);
        }
    };

    static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    /**
     * onResult
     *
     * @param callback the callback when configuration updated
     */
    void registerCallback(ElementsCallback callback) {
        this.configurationCallback = callback;
    }

    @Override
    public void onInitialize() {
        instance = this;
        registerCallbacks(null, elementsCallback);
        super.onInitialize();
    }
}
