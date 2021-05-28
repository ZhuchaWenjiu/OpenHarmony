package com.example.jltfmoban7.component;

import com.example.jltfmoban7.provider.GridAdapter;

import ohos.agp.components.AttrSet;
import ohos.agp.components.TableLayout;
import ohos.app.Context;

/**
 * The GridView
 */
public class GridView extends TableLayout {
    public GridView(Context context) {
        super(context);
    }

    public GridView(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    public GridView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
    }

    /**
     * The setAdapter
     *
     * @param adapter             adapter
     * @param longClickedListener longClickedListener
     */
    void setAdapter(GridAdapter adapter, LongClickedListener longClickedListener) {
        removeAllComponents();
        for (int i = 0; i < adapter.getComponentList().size(); i++) {
            adapter.getComponentList().get(i).setLongClickedListener(longClickedListener);
            addComponent(adapter.getComponentList().get(i));
        }
    }
}
