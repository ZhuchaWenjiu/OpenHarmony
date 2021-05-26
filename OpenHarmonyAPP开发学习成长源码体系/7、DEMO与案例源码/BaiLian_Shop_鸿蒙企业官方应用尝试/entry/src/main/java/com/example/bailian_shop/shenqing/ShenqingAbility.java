package com.example.bailian_shop.shenqing;

import com.example.bailian_shop.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class ShenqingAbility extends AbilitySlice {
    private Button button_shenqing;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_layout_shenqing);
        button_shenqing = (Button) findComponentById(ResourceTable.Id_button_shenqing);

        shenqing();
    }

    //跳转到首页面
    public void shenqing(){
        // 为按钮设置点击回调
        button_shenqing.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if(button_shenqing!=null){
                    present(new TijiaoAbility(),new Intent());
                }
            }
        });
    }
}
