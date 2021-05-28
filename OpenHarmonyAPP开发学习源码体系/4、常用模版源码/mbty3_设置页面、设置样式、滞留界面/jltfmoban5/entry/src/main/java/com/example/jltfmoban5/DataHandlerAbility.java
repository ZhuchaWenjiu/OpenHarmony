package com.example.jltfmoban5;

import ohos.aafwk.content.Intent;
import ohos.ace.ability.AceInternalAbility;
import ohos.app.AbilityContext;
import ohos.app.ElementsCallback;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.LocaleProfile;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.net.Uri;
import ohos.utils.zson.ZSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * DataHandlerAbility usage:
 * 1.Get deviceId from {@link MainAbility}'s intent called by HiLink service when connect your device by NFC/QR-code.
 * 2.Modify {@link DataHandlerAbility#getTemplate()} to get the configuration file of the real device, this
 * configuration file can be obtained from the HuaWei HiLink online design platform
 * (https://developer.huawei.com/consumer/cn/doc/development/smarthome-Guides/apponlineguide-0000001050993083)，
 * or you can create json file referring to our local configuration.
 * 3.choose your {@link DataHandlerAbility#DEVICE_DATA_MODE} 0:HiLink 1:third_party 2:example_data.
 * if you choose HiLink, {@link HiLinkDeviceDataHandler} will handle the data, the premise is that your device
 * supports the HiLink protocol.
 * if you choose third_party, you need modify {@link ThirdPartyDeviceDataHandler} to complete the connection
 * with the server.
 * if you choose example_data, a sample of device is provided for demonstration, only UI without real device data.
 */
public class DataHandlerAbility extends AceInternalAbility {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.DEBUG, 0, "DataHandlerAbility");
    private static final String BUNDLE_NAME = "com.example.jltfmoban5";
    private static final String ABILITY_NAME = "com.example.jltfmoban5.DataHandlerAbility";
    private static final String CLOUD = "cloud";
    private static final String LOCAL = "local";
    private static final int SUCCESS = 0;
    private static final int DEVICE_DATA_MODE_HILINK = 0;
    private static final int DEVICE_DATA_MODE_THIRD_PARTY = 1;
    private static final int DEVICE_DATA_MODE_SAMPLE = 2;
    private static final int DEVICE_DATA_MODE = DEVICE_DATA_MODE_SAMPLE;
    private static final int ACTION_MESSAGE_CODE_SUBSCRIBE = 1001;
    private static final int ACTION_MESSAGE_CODE_GET_TEMPLATE = 1002;
    private static final int ACTION_MESSAGE_CODE_UNSUBSCRIBE = 1003;
    private static final int ACTION_MESSAGE_CODE_DATA_CHANGED = 1004;
    private static final int ACTION_MESSAGE_CODE_INIT_DEVICE_DATA = 1005;
    private static final int ACTION_MESSAGE_CODE_JUMP_TO_HILINK = 1008;

    private static DataHandlerAbility instance;
    private final String deviceId;
    private final String productId;
    private IRemoteObject remoteObjectHandler;
    private final DeviceDataCallback deviceDataCallback = new DeviceDataCallback() {
        /**
         * onResult
         *
         * @param code errorCode
         * @param msg errorMessage
         * @param map the data to send
         */
        public void onResult(int code, String msg, Map<String, Object> map) {
            HiLog.info(LABEL_LOG, "send device data to js: " + map.toString());
            sendData(map);
        }
    };
    private AbilityContext abilityContext;
    private BaseDeviceDataHandler deviceDataHandler;
    private String language;
    private final ElementsCallback configurationCallback = new ElementsCallback() {
        @Override
        public void onMemoryLevel(int i) {
        }

        @Override
        public void onConfigurationUpdated(Configuration configuration) {
            LocaleProfile localeProfile = configuration.getLocaleProfile();
            Map<String, Object> dataMap = new HashMap<>();
            if (!language.equals(localeProfile.getLocales()[0].getLanguage())) {
                dataMap.put("language", localeProfile.getLocales()[0].getLanguage());
                language = localeProfile.getLocales()[0].getLanguage();
            }
            sendData(dataMap);
        }
    };

    public DataHandlerAbility(String deviceId, String productId) {
        super(BUNDLE_NAME, ABILITY_NAME);
        if (DEVICE_DATA_MODE == DEVICE_DATA_MODE_SAMPLE) {
            this.deviceId = SampleDeviceDataHandler.EXAMPLE_DEVICE_ID;
            this.productId = SampleDeviceDataHandler.EXAMPLE_PRODUCT_ID;
        } else {
            this.deviceId = deviceId;
            this.productId = productId;
        }
        Configuration configuration = new Configuration();
        LocaleProfile localeProfile = configuration.getLocaleProfile();
        language = localeProfile.getLocales()[0].getLanguage();
    }

    /**
     * Internal ability registration.
     *
     * @param abilityContext AbilityContext
     * @param deviceId       device id
     * @param productId      product id
     */
    static void register(AbilityContext abilityContext, String deviceId, String productId) {
        instance = new DataHandlerAbility(deviceId, productId);
        instance.onRegister(abilityContext);
    }

    /**
     * Internal ability deregistration.
     */
    static void deregister() {
        instance.onDeregister();
    }

    private BaseDeviceDataHandler getDeviceDataHandler() {
        /* you should choose your DEVICE_DATA_MODE in this function */
        if (DEVICE_DATA_MODE == DEVICE_DATA_MODE_HILINK) {
            return new HiLinkDeviceDataHandler(deviceId, deviceDataCallback);
        } else if (DEVICE_DATA_MODE == DEVICE_DATA_MODE_THIRD_PARTY) {
            return new ThirdPartyDeviceDataHandler(deviceId, deviceDataCallback);
        } else {
            return new SampleDeviceDataHandler(deviceId, deviceDataCallback);
        }
    }

    private void sendData(Map<String, Object> dataMap) {
        MessageParcel data = MessageParcel.obtain();
        MessageParcel reply = MessageParcel.obtain();
        MessageOption option = new MessageOption();
        data.writeString(ZSONObject.toZSONString(dataMap));
        try {
            remoteObjectHandler.sendRequest(0, data, reply, option);
        } catch (RemoteException e) {
            HiLog.error(LABEL_LOG, "send data to js fail");
        }
        reply.reclaim();
        data.reclaim();
    }

    private ZSONObject getTemplate() {
        // source indicates whether json config file is from the local file or cloud file.
        final String source = "local";
        // configDir indicates the directory path of json config file.
        final String configDir = SampleDeviceDataHandler.EXAMPLE_TEMPLATE_FILE_DIR + "/" + productId;
        // configPath indicates the url path of json config file. You can change it into different url corresponding
        // to your product id or device id. Here we use SampleDeviceDataHandler#EXAMPLE_PRODUCT_ID as our productId.
        final String configPath = configDir + "/" + productId + "_" + language + ".json";
        ZSONObject result = null;
        if (LOCAL.equals(source)) {
            try {
                Resource resource = abilityContext.getResourceManager().getRawFileEntry(configPath).openRawFile();
                byte[] tmp = new byte[1024 * 1024];
                int reads = resource.read(tmp);
                if (reads != -1) {
                    String jsonString = new String(tmp, 0, reads, StandardCharsets.UTF_8);
                    result = ZSONObject.stringToZSON(jsonString);
                }
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, "open template from local fail");
            }
        } else {
            try {
                URL url = new URL(configPath);
                URLConnection urlc = url.openConnection();
                urlc.connect();
                try (InputStream is = urlc.getInputStream();
                     InputStreamReader isReader = new InputStreamReader(is);
                     BufferedReader reader = new BufferedReader(isReader)) {
                    StringBuilder sb = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    result = ZSONObject.stringToZSON(sb.toString());
                }
            } catch (IOException e) {
                HiLog.error(LABEL_LOG, "open template from cloud fail");
            }
        }
        if (result != null) {
            // iconUrl is icon's prefix path. Here we use /common/productId in js module. You can change it into your
            // cloud url. All icons file must be under iconUrl.
            result.put("iconUrl", SampleDeviceDataHandler.EXAMPLE_RESOURCE_DIR + "/" + productId);
            // deviceIcon is the product icon's file name. It must exist under iconUrl.
            result.put("deviceIcon", "/" + productId + ".png");
            // logoIcon is the product logo's file name. It must exist under iconUrl.
            result.put("logoIcon", "/logo.png");
        }
        return result;
    }

    private boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply) {
        switch (code) {
            case ACTION_MESSAGE_CODE_SUBSCRIBE: {
                HiLog.info(LABEL_LOG, "ACTION_MESSAGE_CODE_SUBSCRIBE");
                remoteObjectHandler = data.readRemoteObject();
                break;
            }
            case ACTION_MESSAGE_CODE_INIT_DEVICE_DATA: {
                MyApplication.getInstance().registerCallback(configurationCallback);
                deviceDataHandler = getDeviceDataHandler();
                break;
            }
            case ACTION_MESSAGE_CODE_UNSUBSCRIBE: {
                remoteObjectHandler = null;
                break;
            }
            case ACTION_MESSAGE_CODE_DATA_CHANGED: {
                String zsonStr = data.readString();
                ZSONObject zsonObj = ZSONObject.stringToZSON(zsonStr);
                for (Map.Entry<String, Object> entry : zsonObj.entrySet()) {
                    deviceDataHandler.modifyDeviceProperty(entry.getKey(), entry.getValue());
                }
                break;
            }
            case ACTION_MESSAGE_CODE_GET_TEMPLATE: {
                ZSONObject template = getTemplate();
                if (template == null) {
                    reply.writeString("no template");
                    break;
                }
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("template", template);
                Map<String, Object> zsonResult = new HashMap<>();
                zsonResult.put("code", SUCCESS);
                zsonResult.put("data", ZSONObject.toZSONString(dataMap));
                reply.writeString(ZSONObject.toZSONString(zsonResult));
                break;
            }
            case ACTION_MESSAGE_CODE_JUMP_TO_HILINK: {
                ZSONObject zsonObj = ZSONObject.stringToZSON(data.readString());
                jumpToHiLink(zsonObj);
                break;
            }
            default: {
                reply.writeString("service not defined");
                return false;
            }
        }
        return true;
    }

    /**
     * Open the system browser and switch to the hiLink details page.
     *
     * @param zsonObj request param
     */
    private void jumpToHiLink(ZSONObject zsonObj) {
        abilityContext.getMainTaskDispatcher().asyncDispatch(() -> {
            Uri uri = Uri.parse((String) zsonObj.get("url"));
            Intent intent = new Intent();
            intent.setUri(uri);
            intent.setAction("android.intent.action.VIEW");
            abilityContext.startAbility(intent, 0);
        });
    }

    private void onRegister(AbilityContext abilityContext) {
        this.abilityContext = abilityContext;
        this.setInternalAbilityHandler((code, data, reply, option) -> this.onRemoteRequest(code, data, reply));
    }

    private void onDeregister() {
        abilityContext = null;
        this.setInternalAbilityHandler(null);
    }
}