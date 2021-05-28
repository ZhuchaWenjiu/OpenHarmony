package com.example.jltfmoban7.utils;

import ohos.agp.utils.Point;
import ohos.agp.window.service.Display;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.IOException;
import java.util.Optional;

/**
 * App Utils
 */
public class AppUtils {
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(HiLog.LOG_APP, 0, "AppUtils");

    /**
     * getStringResource from resource
     *
     * @param context context
     * @param id      id
     * @return String
     */
    public static String getStringResource(Context context, int id) {
        try {
            return context.getResourceManager().getElement(id).getString();
        } catch (IOException e) {
            HiLog.info(LOG_LABEL, "IOException");
        } catch (NotExistException e) {
            HiLog.info(LOG_LABEL, "NotExistException");
        } catch (WrongTypeException e) {
            HiLog.info(LOG_LABEL, "WrongTypeException");
        }
        return "";
    }

    /**
     * getStringResource from resource
     *
     * @param context context
     * @param id      id
     * @return Int
     */
    public static int getIntResource(Context context, int id) {
        try {
            return context.getResourceManager().getElement(id).getInteger();
        } catch (IOException e) {
            HiLog.info(LOG_LABEL, "IOException");
        } catch (NotExistException e) {
            HiLog.info(LOG_LABEL, "The resource is not exist");
        } catch (WrongTypeException e) {
            HiLog.info(LOG_LABEL, "WrongTypeException");
        }
        return -1;
    }

    /**
     * get width and height of screen
     *
     * @param context context
     * @return Point
     */
    public static Point getScreenInfo(Context context) {
        DisplayManager displayManager = DisplayManager.getInstance();
        Optional<Display> optDisplay = displayManager.getDefaultDisplay(context);
        Point point = new Point(0, 0);
        optDisplay.ifPresent(display -> display.getSize(point));
        return point;
    }
}
