package com.example.jltf_yingshi.utils;

import java.io.IOException;

import ohos.agp.components.ComponentContainer;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * common utils
 */
public class AppUtils {
    /**
     * getOrientation
     *
     * @param context context
     * @return int
     */
    public static int getDirection(Context context) {
        return context.getResourceManager().getConfiguration().direction;
    }

    /**
     * getStringResource
     *
     * @param layout layout
     * @param id id
     * @return String
     */
    public static String getStringResource(ComponentContainer layout, int id) {
        try {
            return layout.getResourceManager().getElement(id).getString();
        } catch (IOException e) {
            HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0, "debug"), "IOException");
        } catch (NotExistException e) {
            HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0, "debug"), "NotExistException");
        } catch (WrongTypeException e) {
            HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0, "debug"), "WrongTypeException");
        }
        return null;
    }
}
