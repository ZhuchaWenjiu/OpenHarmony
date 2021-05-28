package com.example.jltf_picker.slice;

import com.example.jltf_picker.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Picker;

public class MainAbilitySlice extends AbilitySlice {
    Picker picker;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        picker_demo1();
        picker_demo2();
        picker_demo3();
        picker_demo4();
        picker_demo5();
        picker_demo6();
    }
    //创建选择器
    public void picker_demo1(){
        picker = (Picker) findComponentById(ResourceTable.Id_test_picker);
        picker.setMinValue(1); // 设置选择器中的最小值
        picker.setMaxValue(7); // 设置选择器中的最大值
    }

    public void  picker_demo2(){
        picker = (Picker) findComponentById(ResourceTable.Id_test_xingqi);

        picker.setFormatter(i -> {
            String value="";
            switch (i) {
                case 0:
                    value = "Mon";
                    break;
                case 1:
                    value = "Tue";
                    break;
                case 2:
                    value = "Wed";
                    break;
                case 3:
                    value = "Thu";
                    break;
                case 4:
                    value = "Fri";
                    break;
                case 5:
                    value = "Sat";
                    break;
                case 6:
                    value = "Sun";
                    break;
                default:
                    value.equals("");
            }
            return value;
        });

    }

    public void picker_demo3(){
        picker = (Picker) findComponentById(ResourceTable.Id_test_activity);
        picker.setDisplayedData(new String[]{"吃饭", "睡觉", "打豆豆", "学习", "听歌", "锻炼", "上班", "敲代码", "约会", "游戏"});
    }

    //创建选择器
    public void picker_demo4(){
        picker = (Picker) findComponentById(ResourceTable.Id_test_bakcolor);
        picker.setMinValue(1); // 设置选择器中的最小值
        picker.setMaxValue(7); // 设置选择器中的最大值
    }

    public void picker_demo5(){
        picker = (Picker) findComponentById(ResourceTable.Id_test_style);
        picker.setDisplayedData(new String[]{"Ability", "AbilitySlice", "ANS", "CES", "DV", "FA", "HAP", "HDF", "IDN", "MSDP"});

    }
    public void picker_demo6(){
        picker = (Picker) findComponentById(ResourceTable.Id_test_xunhuan);
        picker.setDisplayedData(new String[]{"Ability", "AbilitySlice", "ANS", "CES", "DV", "FA", "HAP", "HDF", "IDN", "MSDP"});
        boolean isWheel = picker.isWheelModeEnabled(); // 获取当前是否是选择轮模式
        picker.setWheelModeEnabled(!isWheel);
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
