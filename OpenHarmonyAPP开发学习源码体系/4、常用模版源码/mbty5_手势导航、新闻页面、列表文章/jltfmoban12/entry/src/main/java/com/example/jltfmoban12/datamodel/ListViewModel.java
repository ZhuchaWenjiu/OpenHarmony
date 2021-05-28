package com.example.jltfmoban12.datamodel;

import com.example.jltfmoban12.customdata.ListItems;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * List data adapter to regularize original data.
 */
public class ListViewModel {
    private static final int ITEM_COUNT = 30;

    private List<ListItemInfo> dataItemList = new ArrayList<>();

    /**
     * Constructor for this class.
     */
    public ListViewModel() {
        // Item count set by user.
        ListItems.initDataSet(ITEM_COUNT);
        String[] listMainTexts = ListItems.getListMainTexts();
        String[] listAuxTexts = ListItems.getListAuxTexts();
        String[] listRightTexts = ListItems.getListRightTexts();
        int[] listLeftImageIds = ListItems.getListLeftImageIds();
        int[] listRightImageIds = ListItems.getListRightImageIds();

        IntStream.range(0, listMainTexts.length)
                .forEach(index -> dataItemList.add(new ListItemInfo(listMainTexts[index], listAuxTexts[index],
                        listRightTexts[index], listLeftImageIds[index], listRightImageIds[index])));
    }

    public List<ListItemInfo> getDataItemList() {
        return dataItemList;
    }
}
