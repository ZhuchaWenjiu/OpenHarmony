package com.example.jltfmoban12.utils;

import com.example.jltfmoban12.ResourceTable;
import com.example.jltfmoban12.datamodel.ListItemInfo;

import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Text;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Realize recycle items.
 */
public class ListItemProvider extends BaseItemProvider {
    private List<ListItemInfo> dataList;

    /**
     * Constructor for this class.
     *
     * @param dataList data list record all user data.
     */
    public ListItemProvider(List<ListItemInfo> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
        } else {
            this.dataList = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component component, ComponentContainer componentContainer) {
        Component itemComponent = component;

        ItemViewHolder itemViewHolder;
        if (itemComponent == null) {
            Context context = componentContainer.getContext();
            itemComponent = LayoutScatter.getInstance(context)
                    .parse(ResourceTable.Layout_list_item_layout, componentContainer, false);
            itemViewHolder = new ItemViewHolder(itemComponent);
            itemComponent.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) itemComponent.getTag();
        }
        itemViewHolder.textMain.setText(dataList.get(position).getTextMain());
        itemViewHolder.textAux.setText(dataList.get(position).getTextAux());
        itemViewHolder.textRight.setText(dataList.get(position).getTextRight());
        itemViewHolder.leftImage.setPixelMap(dataList.get(position).getImageIdLeft());
        itemViewHolder.rightImage.setPixelMap(dataList.get(position).getImageIdRight());

        return itemComponent;
    }

    /**
     * View holder for each list item.
     */
    private static class ItemViewHolder {
        Text textMain;

        Text textAux;

        Text textRight;

        Image leftImage;

        Image rightImage;

        ItemViewHolder(Component itemComponent) {
            textMain = (Text) itemComponent.findComponentById(ResourceTable.Id_list_main_text);
            textAux = (Text) itemComponent.findComponentById(ResourceTable.Id_list_aux_text);
            textRight = (Text) itemComponent.findComponentById(ResourceTable.Id_list_right_text);
            leftImage = (Image) itemComponent.findComponentById(ResourceTable.Id_list_left_image);
            rightImage = (Image) itemComponent.findComponentById(ResourceTable.Id_list_right_image);
        }
    }
}
