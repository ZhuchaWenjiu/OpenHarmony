package com.example.jltflianxi.slice;

import com.example.jltflianxi.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.common.Source;
import ohos.media.player.Player;

import java.io.*;
import java.text.Format;
import java.util.jar.Manifest;

public class MainAbilitySlice extends AbilitySlice {

    static final HiLogLabel logLabel=new HiLogLabel(HiLog.LOG_APP,0x001010,"视频测试");

    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);


       /*Button button = (Button) findComponentById(ResourceTable.Id_jltf_btn);*/
       Image image = (Image) findComponentById(ResourceTable.Id_jltf_image1);
       if (image !=null){
           HiLog.info(logLabel,"按键存在");
           image.setClickedListener(new Component.ClickedListener() {
               @Override
               public void onClick(Component component) {
                   HiLog.info(logLabel,"跳转开始");
                   Intent intent = new Intent();
                   Operation operation = (Operation) new Intent.OperationBuilder()
                                  .withAction("action.vedio_layout")
                                  .build();
                   intent.setOperation(operation);
                   startAbility(intent);
                   HiLog.info(logLabel,"跳转结束");
               }
           });
       }

       /* Button button1 = (Button) findComponentById(ResourceTable.Id_jltf_btn1);*/
        Image image1 = (Image) findComponentById(ResourceTable.Id_jltf_image2);
        if (image1 !=null){
            HiLog.info(logLabel,"按键存在");
            image1.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    HiLog.info(logLabel,"跳转开始");
                    Intent intent = new Intent();
                    Operation operation = (Operation) new Intent.OperationBuilder()
                            .withAction("action.vedio_layout1")
                            .build();
                    intent.setOperation(operation);
                    startAbility(intent);
                    HiLog.info(logLabel,"跳转结束");
                }
            });
        }
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
