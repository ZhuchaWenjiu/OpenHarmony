package com.example.jltfmoban8.core.strategy.bean;

import ohos.media.image.PixelMap;

/**
 * Adjust Strategy Params
 * Used to construct Adjust strategy EditParams
 */
public class AdjustStrategyParams {
    private float progress;
    private PixelMap srcPixelMap;

    /**
     * AdjustStrategyParams Constructor
     *
     * @param progress SlideBar progress
     */
    public AdjustStrategyParams(float progress) {
        this.progress = progress;
    }

    /**
     * Get Progress
     *
     * @return Adjust SlideBar Progress
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Get Source PixelMap
     *
     * @return Source PixelMap
     */
    public PixelMap getSrcPixelMap() {
        return srcPixelMap;
    }

    /**
     * Set Source PixelMap
     *
     * @param srcPixelMap Source PixelMap
     */
    public void setSrcPixelMap(PixelMap srcPixelMap) {
        this.srcPixelMap = srcPixelMap;
    }
}
