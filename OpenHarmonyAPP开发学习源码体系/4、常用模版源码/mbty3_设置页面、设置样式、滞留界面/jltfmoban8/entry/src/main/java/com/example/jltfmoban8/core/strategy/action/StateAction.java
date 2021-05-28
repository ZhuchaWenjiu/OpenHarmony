package com.example.jltfmoban8.core.strategy.action;

import com.example.jltfmoban8.core.strategy.IEditAction;
import com.example.jltfmoban8.core.strategy.IImageEditor;
import com.example.jltfmoban8.core.exceptions.EditActionException;

import ohos.media.image.PixelMap;

/**
 * State edit action
 */
public class StateAction implements IEditAction {
    private ActionType type;
    private IImageEditor imageEditor;

    /**
     * StateAction Constructor
     *
     * @param type EditAction Type
     */
    public StateAction(ActionType type) {
        this.type = type;
    }

    @Override
    public PixelMap execute(PixelMap pixelMap) throws EditActionException {
        if (imageEditor == null) {
            throw new EditActionException("imageEditor not set via setParams yet!");
        }

        if (type == ActionType.ACT_UNDO) {
            imageEditor.undo();
        } else if (type == ActionType.ACT_REDO) {
            imageEditor.redo();
        } else if (type == ActionType.ACT_DROP) {
            imageEditor.drop();
        } else if (type == ActionType.ACT_EXPORT) {
            return imageEditor.export();
        } else {
            throw new EditActionException("unknown EditAction Type");
        }
        return imageEditor.export();
    }

    @Override
    public ActionType getActionType() {
        return this.type;
    }

    @Override
    public <T> void setParams(T params) {
        if (params instanceof IImageEditor) {
            this.imageEditor = (IImageEditor) params;
        }
    }

}
