package com.example.jltfmoban12.customdata;

import com.example.jltfmoban12.ResourceTable;

/**
 * Bottom bar data source setting by user.
 */
public class BottomBarItems {
    private static final int MAX_BAR_ITEM_COUNT = 5;

    private static String[] barTexts;

    private static int[] barOriImageIds;

    private static int[] barActivatedImageIds;

    /**
     * Private construction method. Avoid instantiation.
     */
    private BottomBarItems() {
    }

    /**
     * Bottom bar data source setting by user.
     *
     * @param itemCount bottom bar item count.
     */
    public static void initDataSet(int itemCount) {
        int actualItemCount = Math.min(itemCount, MAX_BAR_ITEM_COUNT);

        barTexts = new String[actualItemCount];
        barOriImageIds = new int[actualItemCount];
        barActivatedImageIds = new int[actualItemCount];

        int index = 0;
        while (index < actualItemCount) {
            // Adding 1 means displayed index begins with 1
            barTexts[index] = "Operation " + (index + 1);
            barOriImageIds[index] = ResourceTable.Media_ori_star;
            barActivatedImageIds[index] = ResourceTable.Media_activated_star;
            index++;
        }
    }

    public static String[] getBarTexts() {
        return barTexts;
    }

    public static int[] getBarOriImageIds() {
        return barOriImageIds;
    }

    public static int[] getBarActivatedImageIds() {
        return barActivatedImageIds;
    }
}
