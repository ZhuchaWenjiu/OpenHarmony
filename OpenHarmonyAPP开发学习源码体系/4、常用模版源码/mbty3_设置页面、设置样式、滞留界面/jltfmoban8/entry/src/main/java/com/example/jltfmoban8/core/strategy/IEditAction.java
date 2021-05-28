package com.example.jltfmoban8.core.strategy;

import com.example.jltfmoban8.core.exceptions.EditActionException;
import ohos.media.image.PixelMap;

/**
 * ImageEditAction Interface
 */
public interface IEditAction {
    /**
     * Execute an edit-action against the PixelMap of which the content will be modified
     *
     * @param bmp the PixelMap to be modified
     * @return the output PixelMap after process
     * @throws EditActionException execution failed, the PixelMap should not be dropped because of dirty data
     */
    PixelMap execute(PixelMap bmp) throws EditActionException;

    ActionType getActionType();

    <T> void setParams(T params);

    enum ActionType {
        ACT_EDIT(0x01), ACT_UNDO(0x11), ACT_REDO(0x12), ACT_DROP(0x14), ACT_EXPORT(0x15);

        private int value = 0;

        ActionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
