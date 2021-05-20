package com.example.myapplication.slice;

import com.example.myapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;

public class MainAbilitySlice2 extends AbilitySlice {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        //加载布局文件
        setUIContent(ResourceTable.Layout_layout);//没有，bug新添加的ID不会更新ResourceTable


        //通过id 找UI组件
        Text text = (Text) findComponentById(ResourceTable.Id_title);
        //1、截断文本，（文本内容的长度要长，大于屏幕宽度
        text.setTruncationMode(Text.TruncationMode.AUTO_SCROLLING);
        //2.开始 滚动。
        text.startAutoScrolling();
        //3.为什么停下来了？，因为滚动了一次，默认是一次
        text.setAutoScrollingCount(-1);//-1表示一直循环

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
