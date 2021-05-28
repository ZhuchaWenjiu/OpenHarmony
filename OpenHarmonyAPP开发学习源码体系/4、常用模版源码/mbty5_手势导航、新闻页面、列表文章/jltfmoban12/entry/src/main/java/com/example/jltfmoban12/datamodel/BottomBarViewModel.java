package com.example.jltfmoban12.datamodel;

import com.example.jltfmoban12.customdata.BottomBarItems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Bottom bar data adapter to regularize original data.
 */
public class BottomBarViewModel {
    private static final int ITEM_COUNT = 5;

    private List<BottomBarItemInfo> barItemList = new ArrayList<>();

    /**
     * Constructor for this class.
     */
    public BottomBarViewModel() {
        BottomBarItems.initDataSet(ITEM_COUNT);
        String[] barTexts = BottomBarItems.getBarTexts();
        int[] barOriImageIds = BottomBarItems.getBarOriImageIds();
        int[] barActivatedImageIds = BottomBarItems.getBarActivatedImageIds();

        IntStream.range(0, barTexts.length)
                .forEach(index -> barItemList
                        .add(new BottomBarItemInfo(barTexts[index], barOriImageIds[index], barActivatedImageIds[index])));
    }

    public List<BottomBarItemInfo> getBarItemList() {
        return barItemList;
    }
}
