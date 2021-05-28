package com.example.jltfmoban9.holder;

import static com.example.jltfmoban9.adapter.ListViewItemProvider.fixNullString;

import com.example.jltfmoban9.ResourceTable;
import com.example.jltfmoban9.model.Item;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;

/**
 * multi-item-style listview
 */
public class ItemViewHolder extends BaseItemHolder {
    private Text itemName;
    private Text rightName;
    private Image rightCheckBox;

    /**
     * ItemViewHolder
     *
     * @param itemComponent itemComponent
     * @param item          item
     */
    public ItemViewHolder(Component itemComponent, Item item) {
        itemName = (Text) itemComponent.findComponentById(ResourceTable.Id_item_content_text);
        rightName = (Text) itemComponent.findComponentById(ResourceTable.Id_right_text);
        rightCheckBox = (Image) itemComponent.findComponentById(ResourceTable.Id_item_right_tab);
    }

    @Override
    public void processItem(Item viewItem) {
        itemName.setText(fixNullString(viewItem.getItemName(), "null"));
        rightName.setText(fixNullString("right text", "null"));
        rightName.setTextColor(Color.GRAY);
        rightCheckBox.setImageAndDecodeBounds(ResourceTable.Media_right_grey);
    }

    @Override
    public void initItemListener() {
        itemName.setClickedListener(component -> {
            new ToastDialog(component.getContext()).setText("itemName click test").show();
        });
    }
}