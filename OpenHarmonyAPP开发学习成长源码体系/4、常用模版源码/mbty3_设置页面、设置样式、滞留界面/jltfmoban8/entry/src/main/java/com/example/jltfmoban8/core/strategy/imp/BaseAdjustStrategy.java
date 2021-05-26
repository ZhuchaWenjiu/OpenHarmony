package com.example.jltfmoban8.core.strategy.imp;

import com.example.jltfmoban8.core.exceptions.HandleStrategyException;
import com.example.jltfmoban8.core.strategy.EditParams;
import com.example.jltfmoban8.core.strategy.IEditStrategy;
import com.example.jltfmoban8.core.strategy.bean.AdjustStrategyParams;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Texture;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.render.ColorMatrix;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

/**
 * Base Adjust Strategy
 * Common part of different adjustment operations
 */
public abstract class BaseAdjustStrategy implements IEditStrategy {
    /**
     * Adjust Strategy Handle method
     *
     * @param origin Source PixelMap
     * @param params Image editing algorithm parameters.
     * @param <T>    AdjustStrategy params
     * @return PixelMap after adjust
     * @throws HandleStrategyException HandleStrategyException
     */
    @Override
    public <T> PixelMap handle(PixelMap origin, EditParams<T> params) throws HandleStrategyException {
        if (!(params.getParams() instanceof AdjustStrategyParams)) {
            throw new HandleStrategyException("invalid params");
        }
        // Get params
        AdjustStrategyParams strategyParams = (AdjustStrategyParams) params.getParams();
        PixelMap srcBmp = strategyParams.getSrcPixelMap();
        PixelMap optionBmp = srcBmp == null ? origin : srcBmp;

        // Set canvas
        PixelMap newPm = PixelMap.create(getPixelMapOptions(optionBmp));
        Canvas canvas = new Canvas(new Texture(newPm));

        // Set paint
        Paint editPaint = getEditPaint(convertProgress(strategyParams.getProgress()));

        // Draw
        PixelMapHolder pixelMapHolder = new PixelMapHolder(optionBmp);
        canvas.drawPixelMapHolder(pixelMapHolder, 0, 0, editPaint);
        return newPm;
    }

    /**
     * Get min value
     *
     * @return MIN Value of Adjust
     */
    protected abstract float getMinValue();

    /**
     * Get max value
     *
     * @return MAX Value of Adjust
     */
    protected abstract float getMaxValue();

    /**
     * Set paint colorMatrix
     *
     * @param value Set Color Matrix According to this Value
     * @return ColorMatrix
     */
    protected abstract ColorMatrix setColorMatrix(float value);

    private PixelMap.InitializationOptions getPixelMapOptions(PixelMap origin) {
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.size = new Size(origin.getImageInfo().size.width, origin.getImageInfo().size.height);
        options.pixelFormat = PixelFormat.ARGB_8888;
        return options;
    }

    private Paint getEditPaint(float value) {
        Paint originalPaint = new Paint();
        originalPaint.setAntiAlias(true);
        originalPaint.setColorMatrix(setColorMatrix(value));
        return originalPaint;
    }

    private float convertProgress(float progress) {
        float minValue = getMinValue();
        float maxValue = getMaxValue();
        if (minValue >= maxValue) {
            new HandleStrategyException("invalid adjust strategy value").printStackTrace();
        }
        float currentValue = (maxValue - minValue) * progress + minValue;
        return currentValue > maxValue ? maxValue : (Math.max(currentValue, minValue));
    }
}