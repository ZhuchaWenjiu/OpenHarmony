package com.example.jltfmoban5;

import java.util.Map;

/**
 * This abstract class is used for device data interaction
 */
public abstract class BaseDeviceDataHandler {
    // used to send data
    private final DeviceDataCallback deviceDataCallback;
    // the id of device
    String deviceId;

    BaseDeviceDataHandler(String deviceId, DeviceDataCallback deviceDataCallback) {
        this.deviceId = deviceId;
        this.deviceDataCallback = deviceDataCallback;
    }

    /**
     * init profile data when first connecting device
     *
     * @param errorCode    errorCode
     * @param errorMessage errorMessage
     * @param dataMap      the data to send
     */
    final void initProfileData(int errorCode, String errorMessage, Map<String, Object> dataMap) {
        this.deviceDataCallback.onResult(errorCode, errorMessage, dataMap);
    }

    /**
     * modify device property from UI
     *
     * @param key   key of data
     * @param value value of data
     */
    public abstract void modifyDeviceProperty(String key, Object value);

    /**
     * data changed from device
     *
     * @param dataMap the data to send
     */
    void onDeviceDataChange(Map<String, Object> dataMap) {
        this.deviceDataCallback.onResult(0, "", dataMap);
    }
}
