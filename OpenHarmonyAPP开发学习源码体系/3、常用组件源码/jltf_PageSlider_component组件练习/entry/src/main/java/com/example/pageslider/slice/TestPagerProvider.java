package com.example.pageslider.slice;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import java.util.List;
public class TestPagerProvider extends PageSliderProvider {
    // 数据源，每个页面对应list中的一项
    private List<DataItem> list;
    public TestPagerProvider(List<DataItem> list) {
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        final DataItem data = list.get(i);
        Text label = new Text(null);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setLayoutConfig(
                new StackLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT,
                        ComponentContainer.LayoutConfig.MATCH_PARENT
                ));
        label.setText(data.mText);
        label.setTextColor(Color.BLACK);
        label.setTextSize(50);
        ShapeElement element = new ShapeElement();
        element.setRgbColor(RgbColor.fromArgbInt(Color.getIntColor("#AFEEEE")));
        label.setBackground(element);
        componentContainer.addComponent(label);
        return label;
    }
    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        componentContainer.removeComponent((Component) o);
    }
    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        return true;
    }
    //数据实体类
    public static class DataItem{
        String mText;
        public DataItem(String txt) {
            mText = txt;
        }
    }
}