package com.example.jltfmoban2.viewholder;

import com.example.jltfmoban2.ResourceTable;
import com.example.jltfmoban2.datamodel.DefaultDoubleLineListItemInfo;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.app.Context;

/**
 * DefaultDoubleLineList View holder
 */
public class DefaultDoubleLineList extends ViewHolder {
    private Text textPrimary;
    private Text textSecondary;
    private Image imagePrimary;
    private Image imageSecondary;

    /**
     * Constructor
     *
     * @param itemComponent itemComponent
     */
    public DefaultDoubleLineList(Component itemComponent) {
        textPrimary = (Text) itemComponent.findComponentById(ResourceTable.Id_doubleLineList_text_primary);
        textSecondary = (Text) itemComponent.findComponentById(ResourceTable.Id_doubleLineList_text_secondary);
        imagePrimary = (Image) itemComponent.findComponentById(ResourceTable.Id_doubleLineList_right_img1);
        imageSecondary = (Image) itemComponent.findComponentById(ResourceTable.Id_doubleLineList_right_img2);
    }

    /**
     * Set up Double-line list item components
     *
     * @param model   item model
     * @param context context
     */
    @Override
    public <T> void setUpComponent(T model, Context context) {
        if (model instanceof DefaultDoubleLineListItemInfo) {
            setUpComponent((DefaultDoubleLineListItemInfo) model, context);
        }
    }

    private void setUpComponent(DefaultDoubleLineListItemInfo model, Context context) {
        textPrimary.setText(model.getFirstLineText());
        textSecondary.setText(model.getSecondLineText());
        imagePrimary.setImageElement(model.getPrimaryImage());
        imageSecondary.setImageElement(model.getSecondaryImage());
    }
}
