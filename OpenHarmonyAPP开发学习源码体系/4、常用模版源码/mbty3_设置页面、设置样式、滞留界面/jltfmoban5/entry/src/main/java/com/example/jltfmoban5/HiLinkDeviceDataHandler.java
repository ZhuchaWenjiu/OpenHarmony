package com.example.jltfmoban5;

import com.huawei.ailifeability.devicemgr.callback.BaseCallback;
import com.huawei.ailifeability.devicemgr.callback.EventListener;
import com.huawei.ailifeability.devicemgr.DeviceMgrApi;
import com.huawei.ailifeability.devicemgr.entity.HiLinkDeviceEntity;
import com.huawei.ailifeability.devicemgr.entity.ServiceEntity;
import ohos.utils.zson.ZSONArray;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * HiLinkDeviceDataHandler device data handle of HiLink
 * it depends on ailifeability.har, it can interact with the device data of the HiLink protocol.
 */
public class HiLinkDeviceDataHandler extends BaseDeviceDataHandler {
    private final BaseCallback<String> modifyDevicePropertyCallback = (code, Message, data) -> {
    };

    HiLinkDeviceDataHandler(String deviceId, DeviceDataCallback deviceDataCallback) {
        super(deviceId, deviceDataCallback);
        BaseCallback<HiLinkDeviceEntity> getProfileDataCallback = (errorCode, errorMessage, hiLinkDeviceEntity) -> {
            List<ServiceEntity> serviceEntity;
            if (errorCode != 0) {
                return;
            } else {
                serviceEntity = hiLinkDeviceEntity.getServices();
            }
            Map<String, Object> dataMap = new HashMap<>();
            for (ServiceEntity entity : serviceEntity) {
                ZSONObject zsonObj = ZSONObject.stringToZSON(entity.getData());
                if (zsonObj == null || zsonObj.isEmpty()) {
                    continue;
                }
                for (Map.Entry<String, Object> obj : zsonObj.entrySet()) {
                    dataMap.put(entity.getSid() + "/" + obj.getKey(), obj.getValue());
                }
            }
            initProfileData(errorCode, errorMessage, dataMap);
        };
        DeviceMgrApi.getInstance().getProfileData(deviceId, getProfileDataCallback);
        EventListener eventlistener = (message, data) -> {
            ZSONObject zsonObj = ZSONObject.stringToZSON(data);
            ZSONArray sids = zsonObj.getZSONObject("body").getZSONArray("services");
            Map<String, Object> dataMap = new HashMap<>();
            for (int i = 0; i < sids.size(); i++) {
                if (((ZSONObject) sids.get(i)).containsKey("data")) {
                    ZSONObject res1 = ((ZSONObject) sids.get(i)).getZSONObject("data");
                    for (Map.Entry<String, Object> entry : res1.entrySet()) {
                        dataMap.put(((ZSONObject) sids.get(i)).getString("sid") + "/" + entry.getKey(),
                                entry.getValue());
                    }
                }
            }
            onDeviceDataChange(dataMap);
        };
        List<String> events = new ArrayList<>();
        events.add("deviceDataChanged");
        DeviceMgrApi.getInstance().registerEventCallback("deviceDataChanged", events, eventlistener);
    }

    @Override
    public void modifyDeviceProperty(String key, Object value) {
        List<String> splits = Arrays.asList(key.split("/"));
        String data = "{\"" + splits.get(1) + "\":" + value + "}";
        DeviceMgrApi.getInstance().modifyDeviceProperty(deviceId, key, data, modifyDevicePropertyCallback);
    }
}
