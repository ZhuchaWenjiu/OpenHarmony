package com.example.jltfmoban12.customdata;

import com.example.jltfmoban12.ResourceTable;

/**
 * List data source setting by user.
 */
public class ListItems {
    private static String[] listMainTexts;

    private static String[] listAuxTexts;

    private static String[] listRightTexts;

    private static int[] listLeftImageIds;

    private static int[] listRightImageIds;

    /**
     * Private construction method. Avoid instantiation.
     */
    private ListItems() {
    }

    /**
     * Init list data.
     *
     * @param itemCount list item count.
     */
    public static void initDataSet(int itemCount) {
        listMainTexts = new String[itemCount];
        listAuxTexts = new String[itemCount];
        listRightTexts = new String[itemCount];
        listLeftImageIds = new int[itemCount];
        listRightImageIds = new int[itemCount];

        int index = 0;
        int displayedIndex;
        while (index < itemCount) {
            // Adding 1 means displayed index begins with 1
            displayedIndex = index + 1;
            listMainTexts[index] = "Double-line list item " + displayedIndex;
            listAuxTexts[index] = "Auxiliary text " + displayedIndex;
            listRightTexts[index] = "Right text " + displayedIndex;
            listLeftImageIds[index] = ResourceTable.Media_profile;
            listRightImageIds[index] = ResourceTable.Media_right_arrow;
            index++;
        }
    }

    public static String[] getListMainTexts() {
        return listMainTexts;
    }

    public static String[] getListAuxTexts() {
        return listAuxTexts;
    }

    public static String[] getListRightTexts() {
        return listRightTexts;
    }

    public static int[] getListLeftImageIds() {
        return listLeftImageIds;
    }

    public static int[] getListRightImageIds() {
        return listRightImageIds;
    }
}
