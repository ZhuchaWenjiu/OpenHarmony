package com.example.jltfmoban8.core.strategy.imp;

import com.example.jltfmoban8.core.strategy.EditParams;
import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.IEditStrategy;
import com.example.jltfmoban8.core.exceptions.HandleStrategyException;

import com.example.jltfmoban8.core.strategy.action.CropEditAction;
import ohos.media.image.PixelMap;

import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

/**
 * Crop EditStrategy
 */
public class CropEditStrategy implements IEditStrategy {
    private static final String STRATEGY_NAME = "CropImage";

    private static final float IGNORABLE_PRECISION_DIFFERENCE = 0.003f;

    /**
     * Crop strategy
     *
     * @param origin Original bitmap
     * @param params Crop parameters
     * @param <T>    Parameter type
     * @return Cropped bitmap
     * @throws HandleStrategyException Strategy execute exception
     */
    @Override
    public <T> PixelMap handle(final PixelMap origin, EditParams<T> params) throws HandleStrategyException {
        Rect operatingRect = handleParameters(origin, params);
        PixelMap.InitializationOptions options = new PixelMap.InitializationOptions();
        options.pixelFormat = PixelFormat.ARGB_8888;
        options.size = new Size(operatingRect.width, operatingRect.height);

        return PixelMap.create(origin, operatingRect, options);
    }

    private <T> Rect handleParameters(final PixelMap origin, EditParams<T> options) throws HandleStrategyException {
        validateParameter(origin, options);
        Rect cropRect = options.getProperty(Rect.class, "cropRect");
        cropRect.width = cropRect.width - cropRect.minX;
        cropRect.height = cropRect.height - cropRect.minY;
        return getFinalCropRect(cropRect, origin);
    }

    private <T> void validateParameter(final PixelMap origin, EditParams<T> options) throws HandleStrategyException {
        if (origin == null) {
            throw new HandleStrategyException("The origin is null!");
        }
        if (options == null) {
            throw new HandleStrategyException("The options is null!");
        }
        Rect cropRect = options.getProperty(Rect.class, "cropRect");
        if (cropRect == null) {
            throw new HandleStrategyException("The options don't contain a field named cropRect!");
        }
        if (!isCropRectWithinOrigin(cropRect, origin)) {
            throw new HandleStrategyException("The cropRect is not completely within the origin range!");
        }
    }

    private boolean isCropRectWithinOrigin(Rect cropRect, PixelMap origin) {
        return cropRect.minX >= 0
                && cropRect.minY >= 0
                && cropRect.width <= origin.getImageInfo().size.width
                && cropRect.height <= origin.getImageInfo().size.height;
    }

    private Rect getFinalCropRect(Rect cropRect, PixelMap origin) {
        if (isNeedPrecisionCompensation(cropRect, origin)) {
            return new Rect(0, 0, origin.getImageInfo().size.width, origin.getImageInfo().size.height);
        } else {
            return cropRect;
        }
    }

    private boolean isNeedPrecisionCompensation(Rect cropRect, PixelMap origin) {
        Rect originRect = new Rect(0, 0, origin.getImageInfo().size.width,
                origin.getImageInfo().size.height);
        boolean isLeftDiffIgnorable = isdiffIgnorable(cropRect.minX,
                originRect.minX, origin.getImageInfo().size.width);
        boolean isTopDiffIgnorable = isdiffIgnorable(cropRect.minY,
                originRect.minY, origin.getImageInfo().size.height);
        boolean isRightDiffIgnorable = isdiffIgnorable(cropRect.width,
                originRect.width, origin.getImageInfo().size.width);
        boolean isBottomIgnorable = isdiffIgnorable(cropRect.height,
                originRect.height, origin.getImageInfo().size.height);

        return isLeftDiffIgnorable && isRightDiffIgnorable && isTopDiffIgnorable && isBottomIgnorable;
    }

    private boolean isdiffIgnorable(int value1, int value2, int scale) {
        return Math.abs(value1 - value2) < getIgnorableRange(scale);
    }

    private float getIgnorableRange(int scale) {
        return scale * IGNORABLE_PRECISION_DIFFERENCE;
    }

    @Override
    public String getName() {
        return STRATEGY_NAME;
    }

    @Override
    public IEditAction createAction() {
        return new CropEditAction(this);
    }
}
