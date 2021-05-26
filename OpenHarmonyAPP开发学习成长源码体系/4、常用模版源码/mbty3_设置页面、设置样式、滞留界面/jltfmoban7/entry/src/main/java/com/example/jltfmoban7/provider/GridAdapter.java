package com.example.jltfmoban7.provider;

import com.example.jltfmoban7.ResourceTable;
import com.example.jltfmoban7.model.GridItemInfo;
import com.example.jltfmoban7.utils.AppUtils;

import ohos.agp.components.AttrHelper;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The GridAdapter
 */
public class GridAdapter {
    private static final int GRID_LAYOUT_BORDER_MARGIN = 24;
    private static final int GRID_ITEM_RIGHT_MARGIN = 8;

    private final List<Component> componentList = new ArrayList<>();

    public GridAdapter(Context context, List<GridItemInfo> itemInfos) {
        int itemPx = getItemWidthByScreen(context);
        for (GridItemInfo item : itemInfos) {
            Component gridItem = LayoutScatter.getInstance(context).parse(ResourceTable.Layout_grid_item, null, false);
            gridItem.setTag(item.getTag());
            if (gridItem.findComponentById(ResourceTable.Id_grid_item_image) instanceof Image) {
                Image imageItem = (Image) gridItem.findComponentById(ResourceTable.Id_grid_item_image);
                imageItem.setPixelMap(item.getIconId());
            }
            if (gridItem.findComponentById(ResourceTable.Id_grid_item_text) instanceof Text) {
                Text textItem = (Text) gridItem.findComponentById(ResourceTable.Id_grid_item_text);
                textItem.setText(item.getItemText());
            }
            gridItem.setWidth(itemPx);
            gridItem.setHeight(itemPx);
            gridItem.setMarginRight(AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context));
            componentList.add(gridItem);
        }
    }

    /**
     * method for get componentList
     *
     * @return componentList
     */
    public List<Component> getComponentList() {
        return componentList;
    }

    private int getItemWidthByScreen(Context context) {
        int screenWidth = AppUtils.getScreenInfo(context).getPointXToInt();

        return (screenWidth
                - AttrHelper.vp2px(GRID_LAYOUT_BORDER_MARGIN, context) * 2
                - AttrHelper.vp2px(GRID_ITEM_RIGHT_MARGIN, context) * 3)
                / AppUtils.getIntResource(context, ResourceTable.Integer_column_count);
    }
}
