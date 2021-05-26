package com.example.jltfmoban5;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * You can refer to this to complete your connection with the server, and you must inherit {@link BaseDeviceDataHandler}
 * 1.Initialize your device data, and pass the server data to the UI through
 * {@link BaseDeviceDataHandler#initProfileData}.
 * 2.When the device data of the server changes, you need to update to the UI through
 * {@link BaseDeviceDataHandler#onDeviceDataChange}.
 * 3.When modifying device data through the UI, the program will call
 * {@link BaseDeviceDataHandler#modifyDeviceProperty}, where you need to send the data to the server.
 */
public class ThirdPartyDeviceDataHandler extends BaseDeviceDataHandler {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.DEBUG, 0, "ThirdPartyDeviceDataHandle");

    ThirdPartyDeviceDataHandler(String deviceId, DeviceDataCallback deviceDataCallback) {
        super(deviceId, deviceDataCallback);
        StringBuilder result = new StringBuilder();
        // urlName should be filled in
        String urlName = "example://";
        try {
            URL realUrl = new URL(urlName);
            HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (IOException e) {
            HiLog.error(LABEL_LOG, "get device data first time error: " + e.getMessage());
        }
        Map<String, Object> dataMap = new HashMap<>();
        ZSONObject zsonObj = ZSONObject.stringToZSON(result.toString());
        for (Map.Entry<String, Object> entry : zsonObj.entrySet()) {
            dataMap.put(entry.getKey(), entry.getValue());
        }
        initProfileData(0, "", dataMap);
        onDeviceChange(deviceId);
    }

    @Override
    public void modifyDeviceProperty(String key, Object value) {
        StringBuilder result = new StringBuilder();
        // urlName should be filled in
        String urlName = "example://";
        try {
            URL realUrl = new URL(urlName);
            HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            try (PrintWriter out = new PrintWriter(conn.getOutputStream())) {
                out.print("{\"" + key + "\":" + value + "}");
                out.flush();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                        StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        result.append(System.getProperty("line.separator")).append(line);
                    }
                }
            }
            // you should deal with result
        } catch (IOException e) {
            HiLog.error(LABEL_LOG, "modify device data error: " + e.getMessage());
        }
    }

    /**
     * onDeviceChange used to monitor the data of device is changed
     *
     * @param deviceId the id of device
     */
    private void onDeviceChange(String deviceId) {
        new Thread(() -> {
            while (true) {
                StringBuilder result = new StringBuilder();
                // urlName should be filled in
                String urlName = "example://" + deviceId;
                try {
                    URL realUrl = new URL(urlName);
                    HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        String line;
                        while ((line = in.readLine()) != null) {
                            result.append(line);
                        }
                    }
                } catch (IOException e) {
                    HiLog.error(LABEL_LOG, "check device data error: " + e.getMessage());
                }
                Map<String, Object> dataMap = new HashMap<>();
                ZSONObject zsonObj = ZSONObject.stringToZSON(result.toString());
                for (Map.Entry<String, Object> entry : zsonObj.entrySet()) {
                    dataMap.put(entry.getKey(), entry.getValue());
                }
                onDeviceDataChange(dataMap);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    HiLog.error(LABEL_LOG, "sleep error");
                }
            }
        }).start();
    }
}
