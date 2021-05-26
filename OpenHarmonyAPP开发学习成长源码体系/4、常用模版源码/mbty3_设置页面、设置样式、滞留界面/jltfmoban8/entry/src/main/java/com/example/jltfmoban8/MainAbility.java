package com.example.jltfmoban8;

import com.example.jltfmoban8.slice.MainAbilitySlice;
import ohos.aafwk.ability.fraction.FractionAbility;
import ohos.aafwk.ability.fraction.FractionScheduler;
import ohos.aafwk.content.Intent;

/**
 * The MainAbility
 */
public class MainAbility extends FractionAbility {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
