package com.example.jltfmoban2.itemprovider;

import com.example.jltfmoban2.ResourceTable;
import com.example.jltfmoban2.datamodel.ItemInfo;
import com.example.jltfmoban2.typefactory.ListTypeFactory;
import com.example.jltfmoban2.typefactory.TypeFactory;
import com.example.jltfmoban2.viewholder.ViewHolder;

import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.app.AbilityContext;

import java.util.List;

/**
 * Item Provider
 */
public class ListItemProvider extends BaseItemProvider {
    private List<ItemInfo> itemList;
    private AbilityContext context;
    private TypeFactory typeFactory;

    /**
     * Provider Constructor
     *
     * @param itemList List contains ListItem data
     * @param context  context
     */
    public ListItemProvider(List<ItemInfo> itemList, AbilityContext context) {
        this.itemList = itemList;
        this.context = context;
        this.typeFactory = new ListTypeFactory();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public ItemInfo getItem(int index) {
        return itemList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public int getItemComponentType(int index) {
        return itemList.get(index).getType(typeFactory);
    }

    @Override
    public Component getComponent(int index, Component component, ComponentContainer componentContainer) {
        Component itemComponent = component;
        ViewHolder viewHolder;
        if (itemComponent == null) {
            itemComponent = LayoutScatter.getInstance(componentContainer.getContext())
                    .parse(getItemComponentType(index), componentContainer, false);
        }
        viewHolder = typeFactory.getViewHolder(getItemComponentType(index), itemComponent);
        viewHolder.setUpComponent(getItem(index), context);
        if (index == getCount() - 1) {
            Image mDivider = (Image) itemComponent.findComponentById(ResourceTable.Id_divider);
            if (mDivider != null) {
                mDivider.setVisibility(Component.INVISIBLE);
            }
        }
        return itemComponent;
    }
}
