package com.example.jltftiyan5.slice;

import com.example.jltftiyan5.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.TimePicker;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        TimePicker timePicker = (TimePicker) findComponentById(ResourceTable.Id_time_picker);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        int second = timePicker.getSecond();

        timePicker.setHour(19);
        timePicker.setMinute(18);
        timePicker.setSecond(12);
        timePicker.setTimeChangedListener(new TimePicker.TimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute, int second) {

            }
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
