package com.example.jltfmoban8.core.strategy.action;

import com.example.jltfmoban8.core.exceptions.EditActionException;
import com.example.jltfmoban8.core.exceptions.HandleStrategyException;
import com.example.jltfmoban8.core.strategy.EditParams;
import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.imp.SaturationStrategy;
import ohos.media.image.PixelMap;

import static com.example.jltfmoban8.core.strategy.IEditAction.ActionType.ACT_EDIT;

/**
 * Saturation edit action
 */
public class SaturationAction implements IEditAction {
    private EditParams<?> editParams;
    private SaturationStrategy strategy;

    /**
     * SaturationAction Constructor
     *
     * @param strategy SaturationStrategy Instance
     */
    public SaturationAction(SaturationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public PixelMap execute(PixelMap pixelMap) throws EditActionException {
        try {
            return strategy.handle(pixelMap, editParams);
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
