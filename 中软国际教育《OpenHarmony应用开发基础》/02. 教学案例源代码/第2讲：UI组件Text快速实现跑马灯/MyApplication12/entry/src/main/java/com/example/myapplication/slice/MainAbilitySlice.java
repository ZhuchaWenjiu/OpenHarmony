package com.example.myapplication.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;

public class MainAbilitySlice extends AbilitySlice {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);

        //1、创建一个布局组件  相当于一个容器，用来存放UI组件
        DirectionalLayout myLayout = new DirectionalLayout(this);//表示这个容器属于那个ability,  cnotext上下文

        //设置排列方向
        myLayout.setOrientation(DirectionalLayout.VERTICAL);//设置排列方向为垂直方向

        //MATCH_PARENT 适配父组件
        //MATCH_CONTENT  根据自己的内容决定大小
        LayoutConfig config = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_PARENT);

        myLayout.setLayoutConfig(config); //布局容器设置 大小
        ShapeElement element = new ShapeElement();//创建一个样式
        element.setRgbColor(new RgbColor(255, 255, 255));//设置颜色为白色
        myLayout.setBackground(element);//给布局容器设置样式

        //                                                  宽度                        高度（实际内容的高度）
        LayoutConfig textConfig = new LayoutConfig(LayoutConfig.MATCH_CONTENT, LayoutConfig.MATCH_CONTENT);

        //2、创建一个Text UI组件
        Text text = new Text(this);//Text组件 就是用来显示文本内容
        text.setLayoutConfig(textConfig);
        text.setText("Hello World，Hellow 鸿蒙，我们来了，我们陪伴你成长");//内容不够长，内容超过了屏幕宽度
        text.setTextColor(new Color(0xFF000000));// FF表示不透明度  00红色  00绿色  00蓝色
         text.setTextSize(50);//设置字体大小
        text.setTextAlignment(TextAlignment.CENTER);//设置文本内容在Text中显示的位置




        //2、创建两个Text UI组件
        Text text2 = new Text(this);//Text组件 就是用来显示文本内容
        text2.setLayoutConfig(textConfig);//宽和高都是父组件的大小
        text2.setText("Hello World，Hellow 鸿蒙，我们来了，我们陪伴你成长");//内容不够长，内容超过了屏幕宽度
        text2.setTextColor(new Color(0xFF000000));// FF表示不透明度  00红色  00绿色  00蓝色
        text2.setTextSize(50);//设置字体大小
        text2.setTextAlignment(TextAlignment.CENTER);//设置文本内容在Text中显示的位置

        Text text3 = new Text(this);//Text组件 就是用来显示文本内容
        text3.setLayoutConfig(textConfig);
        text3.setText("Hello World，Hellow 鸿蒙，我们来了，我们陪伴你成长");//内容不够长，内容超过了屏幕宽度
        text3.setTextColor(new Color(0xFF000000));// FF表示不透明度  00红色  00绿色  00蓝色
        text3.setTextSize(50);//设置字体大小
        text3.setTextAlignment(TextAlignment.CENTER);//设置文本内容在Text中显示的位置


        //3、把组件添加到布局容器里
        myLayout.addComponent(text);
        myLayout.addComponent(text2);//添加第2个组件
        myLayout.addComponent(text3);//....

        //4、加载显示容器
        super.setUIContent(myLayout);

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
