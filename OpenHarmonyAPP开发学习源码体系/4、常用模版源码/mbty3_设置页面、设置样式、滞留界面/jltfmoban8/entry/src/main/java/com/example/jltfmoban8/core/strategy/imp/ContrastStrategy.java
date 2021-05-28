package com.example.jltfmoban8.core.strategy.imp;

import com.example.jltfmoban8.core.strategy.action.ContrastAction;
import com.example.jltfmoban8.core.strategy.IEditAction;
import ohos.agp.render.ColorMatrix;

/**
 * Contrast Strategy
 */
public class ContrastStrategy extends BaseAdjustStrategy {
    private static final String ADJUST_NAME = "Contrast";
    private static final float MIN_VALUE = 0.5f;
    private static final float MAX_VALUE = 1.5f;

    @Override
    protected float getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected float getMaxValue() {
        return MAX_VALUE;
    }

    @Override
    protected ColorMatrix setColorMatrix(float value) {
        ColorMatrix cm = new ColorMatrix();
        cm.setMatrix(getColorMatrix(value));
        return cm;
    }


    @Override
    public String getName() {
        return ADJUST_NAME;
    }

    @Override
    public IEditAction createAction() {
        return new ContrastAction(this);
    }

    /**
     * Get Color Matrix
     *
     * @param currentValue Adjust Value
     * @return Color Matrix
     */
    private float[] getColorMatrix(float currentValue) {
        return new float[]{
                currentValue, 0, 0, 0, 0,
                0, currentValue, 0, 0, 0,
                0, 0, currentValue, 0, 0,
                0, 0, 0, 1, 0};
    }

}
