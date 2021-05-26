package com.example.jltfmoban8.view;

import com.example.jltfmoban8.core.exceptions.CropPickerException;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Path;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;

/**
 * Image Picker
 */
public abstract class ImagePicker extends Component {
    /**
     * BaseLine Rect
     */
    protected RectFloat baseLineRect;

    /**
     * OnPick Action Listener
     */
    protected OnPickActionListener onPickActionListener;

    /**
     * Image Picker Constructor
     *
     * @param context context
     */
    public ImagePicker(Context context) {
        super(context);
    }

    /**
     * Image Picker Constructor
     *
     * @param context context
     * @param attrSet attribute set
     */
    public ImagePicker(Context context, AttrSet attrSet) {
        super(context, attrSet);
    }

    /**
     * Image Picker Constructor
     *
     * @param context context
     * @param attrSet attribute set
     * @param resId   Resource ID
     */
    public ImagePicker(Context context, AttrSet attrSet, int resId) {
        super(context, attrSet, resId);
    }

    /**
     * Obtains BaselineRect
     *
     * @return BaselineRect
     */
    public RectFloat getBaselineRect() {
        return baseLineRect;
    }

    /**
     * Obtains PickRatioRect
     *
     * @return PickRatioRect
     */
    public abstract RectFloat getPickRatioRect();

    /**
     * Set Aspect Ratio
     *
     * @param ratio      Aspect Ratio
     * @param boundRatio BoundRatio, can be null
     * @throws CropPickerException CropPickerException
     */
    public abstract void setAspectRatio(float ratio, RectFloat boundRatio) throws CropPickerException;

    /**
     * Set OnPickActionListener
     *
     * @param listener OnPickActionListener
     */
    public void setOnPickActionListener(OnPickActionListener listener) {
        onPickActionListener = listener;
    }

    /**
     * CropPicker Update
     *
     * @param baseLineRect baseLineRect
     * @throws CropPickerException CropPickerException
     */
    public abstract void update(Rect baseLineRect) throws CropPickerException;

    /**
     * OnPick Action Listener Interface
     */
    public interface OnPickActionListener {
        void onPickUpdate(RectFloat cropRect, Path closedPath);
    }

}
