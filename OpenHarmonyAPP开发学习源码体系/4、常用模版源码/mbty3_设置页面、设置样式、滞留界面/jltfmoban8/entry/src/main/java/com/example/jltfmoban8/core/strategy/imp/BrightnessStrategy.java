package com.example.jltfmoban8.core.strategy.imp;

import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.action.BrightnessAction;
import ohos.agp.render.ColorMatrix;

/**
 * Brightness Strategy
 */
public class BrightnessStrategy extends BaseAdjustStrategy {
    private static final String ADJUST_NAME = "Brightness";
    private static final int MIN_VALUE = -63;
    private static final int MAX_VALUE = 64;

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
        return new BrightnessAction(this);
    }

    /**
     * Get Color Matrix
     *
     * @param currentValue Adjust Value
     * @return Color Matrix
     */
    private float[] getColorMatrix(float currentValue) {
        return new float[]{
                1, 0, 0, 0, currentValue,
                0, 1, 0, 0, currentValue,
                0, 0, 1, 0, currentValue,
                0, 0, 0, 1, 0};
    }

}
