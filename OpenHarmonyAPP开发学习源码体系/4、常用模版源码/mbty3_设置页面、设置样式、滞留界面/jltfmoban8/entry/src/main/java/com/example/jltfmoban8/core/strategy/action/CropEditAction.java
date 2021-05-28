package com.example.jltfmoban8.core.strategy.action;

import com.example.jltfmoban8.core.strategy.EditParams;
import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.exceptions.EditActionException;
import com.example.jltfmoban8.core.exceptions.HandleStrategyException;
import com.example.jltfmoban8.core.strategy.imp.CropEditStrategy;
import ohos.media.image.PixelMap;

import static com.example.jltfmoban8.core.strategy.IEditAction.ActionType.ACT_EDIT;

/**
 * Crop edit action
 */
public class CropEditAction implements IEditAction {
    private static final String TAG = "CropEditAction";
    private EditParams<?> editParams;
    private CropEditStrategy cropStrategy;

    /**
     * CropAction Constructor
     *
     * @param strategy CropStrategy Instance
     */
    public CropEditAction(CropEditStrategy strategy) {
        cropStrategy = strategy;
    }

    @Override
    public PixelMap execute(PixelMap pixelMap) throws EditActionException {
        try {
            return cropStrategy.handle(pixelMap, editParams);
        } catch (HandleStrategyException e) {
            throw new EditActionException(e.getMessage());
        }
    }

    @Override
    public ActionType getActionType() {
        return ACT_EDIT;
    }

    @Override
    public <T> void setParams(T params) {
        editParams = new EditParams<>(params);
    }

}
