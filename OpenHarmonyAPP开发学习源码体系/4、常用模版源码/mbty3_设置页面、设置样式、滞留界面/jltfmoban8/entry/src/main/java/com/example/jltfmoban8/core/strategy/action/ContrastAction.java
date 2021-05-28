package com.example.jltfmoban8.core.strategy.action;

import com.example.jltfmoban8.core.exceptions.EditActionException;
import com.example.jltfmoban8.core.exceptions.HandleStrategyException;
import com.example.jltfmoban8.core.strategy.EditParams;
import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.imp.ContrastStrategy;
import ohos.media.image.PixelMap;

import static com.example.jltfmoban8.core.strategy.IEditAction.ActionType.ACT_EDIT;

/**
 * Contrast edit action
 */
public class ContrastAction implements IEditAction {
    private EditParams<?> editParams;
    private ContrastStrategy strategy;

    /**
     * ContrastAction Constructor
     *
     * @param strategy ContrastStrategy Instance
     */
    public ContrastAction(ContrastStrategy strategy) {
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
