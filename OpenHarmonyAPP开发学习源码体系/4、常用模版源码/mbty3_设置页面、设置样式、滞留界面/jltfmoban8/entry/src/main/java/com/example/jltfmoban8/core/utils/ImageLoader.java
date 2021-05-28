package com.example.jltfmoban8.core.utils;

import com.example.jltfmoban8.MyApplication;
import com.example.jltfmoban8.ResourceTable;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.PixelMap.InitializationOptions;
import ohos.media.image.common.Size;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ImageFile Loader
 */
public class ImageLoader {
    private static final HiLogLabel IMAGE_LOADER = new HiLogLabel(1, 1, "Image Loader Exception ");
    private static final int DEFAULT_TEST_IMAGE_WIDTH = 1080;
    private static final int DEFAULT_TEST_IMAGE_HEIGHT = 2340;

    /**
     * Obtains PixelMap instance From Image File
     *
     * @param context  Context
     * @param fileName File Name
     * @return PixelMap
     */
    public static PixelMap getPixelMap(Context context, String fileName) {
        ImageSource imageSource = null;
        try {
            imageSource = ImageSource.create(getPictureFD(context, fileName), null);
        } catch (IllegalArgumentException | FileNotFoundException fileNotFoundException) {
            HiLog.error(IMAGE_LOADER, "Image File Not Found, Using Test Image Instead ", "");
            PixelMap.InitializationOptions options = new InitializationOptions();
            options.size = new Size(DEFAULT_TEST_IMAGE_WIDTH, DEFAULT_TEST_IMAGE_HEIGHT);
            options.editable = true;
            PixelMap defaultPixelMap = PixelMap.create(options);
            defaultPixelMap.writePixels(Color.WHITE.getValue());
            return defaultPixelMap;
        }
        return imageSource.createPixelmap(null);
    }

    private static FileDescriptor getPictureFD(Context context, String name) throws FileNotFoundException {
        DataAbilityPredicates predicates = new DataAbilityPredicates("_display_name = '" + name + "'");
        DataAbilityHelper helper = DataAbilityHelper.creator(context);
        String[] projection = new String[]{AVStorage.Images.Media.ID,
                AVStorage.Images.Media.DISPLAY_NAME, AVStorage.Images.Media.DATA};
        ResultSet result;
        try {
            result = helper.query(AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, projection, predicates);
            if (result != null && result.goToNextRow()) {
                int mediaId = result.getInt(result.getColumnIndexForName(AVStorage.Images.Media.ID));
                Uri contentUri = Uri.appendEncodedPathToUri(
                        AVStorage.Images.Media.EXTERNAL_DATA_ABILITY_URI, "" + mediaId);
                return helper.openFile(contentUri, "r");
            }
        } catch (DataAbilityRemoteException dataAbilityRemoteException) {
            HiLog.error(IMAGE_LOADER, "Image Load Failed ", "");
        }
        return null;
    }
}
