package com.example.jltf_yingshi.slice;

import com.example.jltf_yingshi.ResourceTable;
import com.example.jltf_yingshi.utils.AppUtils;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.text.RichText;
import ohos.agp.text.RichTextBuilder;
import ohos.agp.text.TextForm;
import ohos.agp.utils.Color;
import ohos.multimodalinput.event.TouchEvent;
import ohos.utils.net.Uri;

/**
 * View Helper
 */
public class ViewCreateHelper implements Component.ClickedListener {
    private static final String TAG = "ViewCreateHelper";

    private final AbilitySlice slice;

    private ComponentContainer rootLayout;

    private final RichText.TouchEventListener listener = new RichText.TouchEventListener() {
        @Override
        public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
            Intent intent = new Intent();
            String url = AppUtils.getStringResource(rootLayout, ResourceTable.String_privacy_url);
            if(url == null) {
                return false;
            }
            Operation operation = new Intent.OperationBuilder()
                    .withUri(Uri.parse(url))
                    .build();
            intent.setOperation(operation);
            slice.startAbility(intent);
            return false;
        }
    };

    /**
     * ViewCreateHelper
     *
     * @param abilitySlice abilitySlice
     *
     */
    public ViewCreateHelper(AbilitySlice abilitySlice) {
        this.slice = abilitySlice;
    }

    /**
     * create Component
     *
     * @return ComponentContainer ComponentContainer
     */
    public ComponentContainer createComponent() {
        Component mainComponent =
            LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_main_ability, null, false);

        if (!(mainComponent instanceof ComponentContainer)) {
            return null;
        }

        rootLayout = (ComponentContainer) mainComponent;
        initLayout(rootLayout);
        return rootLayout;
    }

    /**
     * create Landscape Component
     *
     * @return ComponentContainer ComponentContainer
     */
    public ComponentContainer createComponentLandscape() {
        Component mainComponent =
                LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_main_ability_landscape, null, false);
        if (!(mainComponent instanceof ComponentContainer)) {
            return null;
        }

        rootLayout = (ComponentContainer) mainComponent;
        initLayout(rootLayout);
        return rootLayout;
    }

    private void initLayout(ComponentContainer container) {
        TextForm form = new TextForm();
        form.setTextSize(60);
        form.setTextColor(Color.BLUE.getValue());

        RichTextBuilder builder = new RichTextBuilder(form);
        String tmpString = AppUtils.getStringResource(container, ResourceTable.String_know_more);
        if (tmpString == null) {
            return;
        }
        builder.addText(tmpString);

        RichText richText = builder.build();
        richText.addTouchEventListener(listener, 0, tmpString.length());

        Text text = (Text) container.findComponentById(ResourceTable.Id_know_more);
        text.setRichText(richText);

        Image leftArrow = (Image) rootLayout.findComponentById(ResourceTable.Id_left_arrow);
        leftArrow.setClickedListener(this);
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_left_arrow) {
            slice.terminateAbility();
        }
    }
}
