package com.example.jltfmoban8.core.strategy.action;

import com.example.jltfmoban8.core.exceptions.EditActionException;
import com.example.jltfmoban8.core.strategy.IEditAction;
import ohos.media.image.PixelMap;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Image edit batch action
 * <p>
 * Provides serial batch processing streams for image editing. Different image editing steps can be combined
 * into one image editing operation in series, The output of each step is the input of the next step.
 */
public class BatchEditAction implements IEditAction {
    private final List<IEditAction> actionPipeline = new LinkedList<>();

    @Override
    public PixelMap execute(PixelMap pixelMap) throws EditActionException {
        PixelMap input = pixelMap;
        PixelMap output = null;
        for (IEditAction action : actionPipeline) {
            output = action.execute(input);
            input = output;
        }
        return output;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACT_EDIT;
    }

    @Override
    public <T> void setParams(T params) {
    }

    /**
     * Add Action to the Queue
     *
     * @param action Edit Action
     */
    public void appendEditAction(IEditAction action) {
        actionPipeline.add(action);
    }
}
