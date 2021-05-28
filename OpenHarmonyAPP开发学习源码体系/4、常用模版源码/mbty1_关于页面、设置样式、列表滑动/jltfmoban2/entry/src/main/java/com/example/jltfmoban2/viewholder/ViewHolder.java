package com.example.jltfmoban2.viewholder;

import ohos.app.Context;

/**
 * ViewHolder interface
 * Implement this interface to support custom view holders
 */
public abstract class ViewHolder {
    /**
     * Implement this method to set up item components
     *
     * @param model   Item model
     * @param context context
     */
    public abstract <T> void setUpComponent(T model, Context context);
}
