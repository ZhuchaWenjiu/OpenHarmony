package com.example.bailian_shop.slice;

import com.example.bailian_shop.ResourceTable;
import com.example.bailian_shop.jihua.JihuaAbility;
import com.example.bailian_shop.my.MyAbility;
import com.example.bailian_shop.shenqing.ShenqingAbility;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {
    static final HiLogLabel logLabel=new HiLogLabel(HiLog.LOG_APP,0x001010,"视频测试");
    private Button button_shenqing,button_jihua,button_shiping,button_my;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        button_shenqing = (Button) findComponentById(ResourceTable.Id_button_shenqing);
        button_jihua = (Button) findComponentById(ResourceTable.Id_button_jihua);
        button_shiping = (Button) findComponentById(ResourceTable.Id_button_shiping);
        button_my = (Button) findComponentById(ResourceTable.Id_button_my);

        shenqing();
        jihua();
        shiping();
        my();
    }

    //跳转到申请页面
    public void shenqing(){
        // 为按钮设置点击回调
        button_shenqing.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shenqing!=null){
                    present(new ShenqingAbility(),new Intent());
                }
            }
        });
    }

    //跳转到鸿蒙计划页面
    public void jihua(){
        // 为按钮设置点击回调
        button_jihua.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_jihua!=null){
                    present(new JihuaAbility(),new Intent());
                }
            }
        });
    }

    //跳转到鸿蒙计划页面
    public void my(){
        // 为按钮设置点击回调
        button_my.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_my!=null){
                    present(new MyAbility(),new Intent());
                }
            }
        });
    }

    public void shiping(){
        if (button_shiping !=null){
            HiLog.info(logLabel,"按键存在");
            button_shiping.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    HiLog.info(logLabel,"跳转开始");
                    Intent intent = new Intent();
                    Operation operation = (Operation) new Intent.OperationBuilder()
                            .withAction("action.layout_shiping")
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
