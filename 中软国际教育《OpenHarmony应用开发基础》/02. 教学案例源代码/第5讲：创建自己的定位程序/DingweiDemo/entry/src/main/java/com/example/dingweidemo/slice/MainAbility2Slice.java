package com.example.dingweidemo.slice;

import com.example.dingweidemo.utils.LogUtil;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.location.GeoAddress;
import ohos.location.GeoConvert;

import java.io.IOException;
import java.util.List;

public class MainAbility2Slice extends AbilitySlice {

    private DirectionalLayout myLayout = new DirectionalLayout(this);

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        LayoutConfig config = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_PARENT);
        myLayout.setLayoutConfig(config);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(new RgbColor(255, 255, 255));
        myLayout.setBackground(element);

        Text text = new Text(this);
        text.setLayoutConfig(config);
        text.setText("Hello bb");
        text.setTextColor(new Color(0xFF000000));
        text.setTextSize(50);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                LogUtil.hiLogInfo("click-------------------");
//                Locator locator = new Locator(MainAbilitySlice.this);
//                Locator locator = new Locator(getContext());
//                RequestParam requestParam = new RequestParam(RequestParam.SCENE_NAVIGATION);
//                MyLocatorCallback locatorCallback = new MyLocatorCallback();
//                locator.startLocating(requestParam, locatorCallback);
//                LogUtil.hiLogInfo("-----------------0000开启定位-----------------");

                GeoConvert geoConvert = new GeoConvert();
                try {
                    List<GeoAddress> list = geoConvert.getAddressFromLocation(30.495866, 114.535705, 1);
                    list.forEach(o->LogUtil.hiLogInfo("GeoAddress----------------" + o.getCountryName() + "-----" + o.getAddressUrl() + "-----" + o.getPlaceName()));
                    List<GeoAddress> list2 = geoConvert.getAddressFromLocationName("北京大兴国际机场", 1);
                    list2.forEach(o->LogUtil.hiLogInfo("GeoAddress----------------" + o.getCountryName() + "-----" + o.getAddressUrl() + "-----" + o.getPlaceName()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        myLayout.addComponent(text);
        super.setUIContent(myLayout);
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
