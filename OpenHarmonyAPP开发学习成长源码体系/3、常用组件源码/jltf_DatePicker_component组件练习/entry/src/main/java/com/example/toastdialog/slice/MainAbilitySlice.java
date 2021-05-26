package com.example.toastdialog.slice;

import com.example.toastdialog.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.DatePicker;
import ohos.agp.components.Text;

public class MainAbilitySlice extends AbilitySlice {
    private DatePicker datePicker;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        date();

    }

    //获取当前的年月日
    public void date(){
        // 获取DatePicker实例
        datePicker = (DatePicker) findComponentById(ResourceTable.Id_date_pick);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

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
