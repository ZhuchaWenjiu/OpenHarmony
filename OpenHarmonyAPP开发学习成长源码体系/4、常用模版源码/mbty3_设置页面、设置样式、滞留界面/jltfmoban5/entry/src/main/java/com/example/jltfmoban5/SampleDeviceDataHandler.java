package com.example.jltfmoban5;

import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.global.icu.text.SimpleDateFormat;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SampleDeviceDataHandler used for example
 */
public class SampleDeviceDataHandler extends BaseDeviceDataHandler {
    static final String EXAMPLE_TEMPLATE_FILE_DIR = "resources/rawfile";
    static final String EXAMPLE_RESOURCE_DIR = "/common";
    static final String EXAMPLE_DEVICE_ID = "0001";
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.DEBUG, 0, "SampleDeviceDataHandler");
    private static final String LAMP = "LAMP";
    static final String EXAMPLE_PRODUCT_ID = LAMP;
    private static final String SOY_MILK_MACHINE = "SOYMILKMACHINE";
    private static final String TOOTH_BRUSH = "TOOTHBRUSH";
    private static final int FULL_POWER = 100;
    private static final int BRUSH_SCORE = 56;
    private static final int BRUSH_DAY = 26;
    private static final int BRUSH_STATUS = 1;
    private static final int WORK_STATUS_STANDBY = 1;
    private static final int WORK_STATUS_PREPARING = 2;
    private static final int WORK_STATUS_WORKING = 3;
    private static final int WORK_STATUS_COMPLETE = 4;
    private static final int WORK_STATUS_RESERVATION = 5;
    private static final int UNSUPPORTED_TIME = -1;
    private final DeviceDataModel dataModel;
    private final EventHandler mainHandler;
    private final DeviceDataModel lampDataModel = new DeviceDataModel() {
        @Override
        public Map<String, Object> recoverData() {
            // Suppose we already read the data from lamp.
            Map<String, Object> sampleDataMap = new HashMap<>();
            sampleDataMap.put("switch/on", 0);
            sampleDataMap.put("lightMode/mode", 0);
            sampleDataMap.put("brightness/brightness", 0);
            return sampleDataMap;
        }

        @Override
        public Map<String, Object> modifyProperty(String key, Object value) {
            // Suppose we already change the lamp's status and feedback the value to refresh UI.
            Map<String, Object> sampleDataMap = new HashMap<>();
            sampleDataMap.put(key, value);
            return sampleDataMap;
        }
    };
    private final DeviceDataModel toothBrushDataModel = new DeviceDataModel() {
        @Override
        public Map<String, Object> recoverData() {
            // Suppose we already read the data from toothbrush.
            Map<String, Object> sampleDataMap = new HashMap<>();
            sampleDataMap.put("bluetoothConnectStatus/bluetoothConnectStatus", BRUSH_STATUS);
            sampleDataMap.put("infoDisplay/battery", FULL_POWER);
            sampleDataMap.put("brushingHistory/score", BRUSH_SCORE);
            sampleDataMap.put("brushDay/brushDay", BRUSH_DAY);
            return sampleDataMap;
        }

        @Override
        public Map<String, Object> modifyProperty(String key, Object value) {
            // Toothbrush do not support modifying property.
            return null;
        }
    };
    private final DeviceDataModel soyMilkMachineDataModel = new DeviceDataModel() {
        private final List<String> capacity = Arrays.asList("300ml", "600ml", "900ml", "1200ml");
        private final List<String> temperature = Arrays.asList("40", "50", "60", "70", "80", "90", "100");
        private final Map<String, Object> sampleDataMap = new HashMap<>();
        private Runnable runTask;
        private long taskDelay;
        private boolean selected = false;

        @Override
        public Map<String, Object> recoverData() {
            // Suppose we already read the data from soy milk machine.
            resetData();
            return sampleDataMap;
        }

        @Override
        public Map<String, Object> modifyProperty(String key, Object value) {
            if ("workingStatus/action".equals(key)) {
                handleWorkingStatus((int) value);
            } else {
                sampleDataMap.remove("remainingTime/time");
                if ("drinkMode/mode".equals(key)) {
                    handleDrinkMode(value);
                } else if ("steamMode/mode".equals(key)) {
                    handleStreamMode(value);
                } else if ("advancedMode/mode".equals(key)) {
                    handleAdvancedMode(value);
                } else if ("reservationMode/mode".equals(key)) {
                    handleReservationMode(value);
                }
            }
            return sampleDataMap;
        }

        private void resetData() {
            sampleDataMap.clear();
            sampleDataMap.put("workingStatus/status", WORK_STATUS_STANDBY);
            sampleDataMap.put("workingStatus/action", WORK_STATUS_STANDBY);
            sampleDataMap.put("drinkMode/mode", 0);
            sampleDataMap.put("drinkMode/description", "");
            sampleDataMap.put("steamMode/mode", 0);
            sampleDataMap.put("steamMode/description", "");
            sampleDataMap.put("advancedMode/mode", 0);
            sampleDataMap.put("advancedMode/description", "");
            sampleDataMap.put("reservationMode/mode", 0);
            sampleDataMap.put("reservationMode/description", "");
            sampleDataMap.put("remainingTime/time", UNSUPPORTED_TIME);
            taskDelay = UNSUPPORTED_TIME;
            if (runTask != null) {
                mainHandler.removeTask(runTask);
            }
            runTask = null;
            selected = false;
        }

        private void handleWorkingStatus(int value) {
            if (selected && value == WORK_STATUS_WORKING) {
                sampleDataMap.put("workingStatus/action", WORK_STATUS_WORKING);
                sampleDataMap.put("workingStatus/status", WORK_STATUS_WORKING);
                sampleDataMap.put("remainingTime/time", taskDelay);
                if (taskDelay >= 0) {
                    runTask = () -> {
                        sampleDataMap.put("workingStatus/action", WORK_STATUS_COMPLETE);
                        sampleDataMap.put("workingStatus/status", WORK_STATUS_COMPLETE);
                        sampleDataMap.put("remainingTime/time", 0);
                        onDeviceDataChange(sampleDataMap);
                    };
                    mainHandler.postTask(runTask, taskDelay * 1000);
                }
            } else {
                resetData();
            }
        }

        private void handleDrinkMode(Object value) {
            selected = true;
            ZSONObject zsonObj = ZSONObject.stringToZSON(value.toString());
            if (zsonObj.containsKey("duration")) {
                taskDelay = (int) zsonObj.getZSONArray("duration").get(0) * 60 +
                        (int) zsonObj.getZSONArray("duration").get(1);
            }
            if (taskDelay <= 0) {
                resetData();
                sampleDataMap.put("error", "time can't set to zero");
                return;
            } else {
                sampleDataMap.remove("error");
            }
            sampleDataMap.put("drinkMode/mode", (zsonObj).getInteger("drinkKind"));
            String res = (capacity.get((zsonObj).getInteger("capacity"))) + ("/");
            res += temperature.get(((zsonObj).getInteger("temperature"))) + ("°C");
            sampleDataMap.put("drinkMode/description", res);
            sampleDataMap.put("workingStatus/action", WORK_STATUS_PREPARING);
            sampleDataMap.put("workingStatus/status", WORK_STATUS_PREPARING);
        }

        private void handleStreamMode(Object value) {
            selected = true;
            ZSONObject zsonObj = ZSONObject.stringToZSON(value.toString());
            if (zsonObj.containsKey("duration")) {
                taskDelay = (int) zsonObj.getZSONArray("duration").get(0) * 60 +
                        (int) zsonObj.getZSONArray("duration").get(1);
            }
            if (taskDelay <= 0) {
                resetData();
                sampleDataMap.put("error", "time can't set to zero");
                return;
            } else {
                sampleDataMap.remove("error");
            }
            sampleDataMap.put("steamMode/mode", (zsonObj).getInteger("steamKind"));
            String res = temperature.get(((zsonObj).getInteger("temperature"))) + ("°C");
            sampleDataMap.put("steamMode/description", res);
            sampleDataMap.put("workingStatus/action", WORK_STATUS_PREPARING);
            sampleDataMap.put("workingStatus/status", WORK_STATUS_PREPARING);
        }

        private void handleAdvancedMode(Object value) {
            selected = true;
            ZSONObject zsonObj = ZSONObject.stringToZSON(value.toString());
            taskDelay = UNSUPPORTED_TIME;
            sampleDataMap.put("advancedMode/mode", (zsonObj).getInteger("advancedKind"));
            sampleDataMap.put("workingStatus/action", WORK_STATUS_PREPARING);
            sampleDataMap.put("workingStatus/status", WORK_STATUS_PREPARING);
        }

        private void handleReservationMode(Object value) {
            selected = true;
            ZSONObject zsonObj = ZSONObject.stringToZSON(value.toString());
            sampleDataMap.put("reservationMode/mode", (zsonObj).getInteger("reservationKind"));
            String res = ((zsonObj).getString("time"));
            sampleDataMap.put("reservationMode/description", res);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            res = year + "-" + month + "-" + day + " " + res;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long countDown = UNSUPPORTED_TIME;
            try {
                countDown = (int) ((sdf.parse(res).getTime() -
                        (new Date().getTime())) / 1000);
                if (countDown < 0) {
                    countDown += 24 * 60 * 60;
                }
            } catch (ParseException e) {
                HiLog.error(LABEL_LOG, "convert time fail");
            }
            runTask = () -> {
                resetData();
                onDeviceDataChange(sampleDataMap);
            };
            taskDelay = countDown;
            mainHandler.postTask(runTask, taskDelay * 1000);
            sampleDataMap.put("remainingTime/time", countDown);
            sampleDataMap.put("workingStatus/action", WORK_STATUS_RESERVATION);
            sampleDataMap.put("workingStatus/status", WORK_STATUS_RESERVATION);
        }
    };

    SampleDeviceDataHandler(String deviceId, DeviceDataCallback deviceDataCallback) {
        super(deviceId, deviceDataCallback);
        if (LAMP.equals(EXAMPLE_PRODUCT_ID)) {
            dataModel = lampDataModel;
        } else if (SOY_MILK_MACHINE.equals(EXAMPLE_PRODUCT_ID)) {
            dataModel = soyMilkMachineDataModel;
        } else {
            dataModel = toothBrushDataModel;
        }
        mainHandler = new EventHandler(EventRunner.getMainEventRunner());
        mainHandler.postTask(() -> initProfileData(0, "", dataModel.recoverData()));
    }

    @Override
    public void modifyDeviceProperty(String key, Object value) {
        mainHandler.postTask(() -> onDeviceDataChange(dataModel.modifyProperty(key, value)));
    }

    private interface DeviceDataModel {
        Map<String, Object> recoverData();

        Map<String, Object> modifyProperty(String key, Object value);
    }
}
