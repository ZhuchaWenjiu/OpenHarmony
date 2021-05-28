package com.example.jltfmoban8.core.strategy;

import com.example.jltfmoban8.core.exceptions.HandleStrategyException;
import ohos.media.image.PixelMap;

/**
 * ImageEditStrategy Interface
 */
public interface IEditStrategy {
    /**
     * Image processing method
     *
     * @param <T>    Specifies the parameter type, which is defined by the image editing algorithm.
     * @param origin Source PixelMap
     * @param params Image editing algorithm parameters.
     * @return PixelMap after edit
     * @throws HandleStrategyException Handle exception
     */
    <T> PixelMap handle(final PixelMap origin, EditParams<T> params) throws HandleStrategyException;

    /**
     * Obtains the strategy name.
     *
     * @return Strategy name
     */
    String getName();

    /**
     * Create an corresponding action instance
     *
     * @return An empty action instance
     */
    IEditAction createAction();
}
