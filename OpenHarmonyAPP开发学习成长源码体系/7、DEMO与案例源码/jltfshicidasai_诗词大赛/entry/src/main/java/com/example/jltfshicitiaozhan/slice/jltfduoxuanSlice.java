package com.example.jltfshicitiaozhan.slice;

import com.example.jltfshicitiaozhan.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;

import java.util.HashSet;
import java.util.Set;

public class jltfduoxuanSlice extends AbilitySlice {

    // 保存最终选中的结果
    private Set<String> selectedSet = new HashSet<>();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_jltf_duoxuan);

        initCheckbox();
        elementButtonInit();
        showAnswer();

        initCheckbox1();
        elementButtonInit1();
        showAnswer1();

        Button button = (Button) findComponentById(ResourceTable.Id_jltf_button1);
        button.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new jltfduoxuan1Slice(),new Intent());
            }
        });
    }
        // 初始化Checkbox
        private void initCheckbox() {
            Checkbox checkbox1 = (Checkbox) findComponentById(ResourceTable.Id_check_box11);
            checkbox1.setButtonElement(elementButtonInit());
            checkbox1.setCheckedStateChangedListener((component, state) -> {
                if (state) {
                    selectedSet.add("A");
                } else {
                    selectedSet.remove("A");
                }
                showAnswer();
            });

            Checkbox checkbox2 = (Checkbox) findComponentById(ResourceTable.Id_check_box22);
            checkbox2.setButtonElement(elementButtonInit());
            checkbox2.setCheckedStateChangedListener((component, state) -> {
                if (state) {
                    selectedSet.add("B");
                } else {
                    selectedSet.remove("B");
                }
                showAnswer();
            });

           Checkbox checkbox3 = (Checkbox) findComponentById(ResourceTable.Id_check_box33);
            checkbox3.setButtonElement(elementButtonInit());
            checkbox3.setCheckedStateChangedListener((component, state) -> {
                if (state) {
                    selectedSet.add("C");
                } else {
                    selectedSet.remove("C");
                }
                showAnswer();
            });

            Checkbox checkbox4 = (Checkbox) findComponentById(ResourceTable.Id_check_box44);
            checkbox4.setButtonElement(elementButtonInit());
            checkbox4.setCheckedStateChangedListener((component, state) -> {
                if (state) {
                    selectedSet.add("D");
                } else {
                    selectedSet.remove("D");
                }
                showAnswer();
            });
        }
        // 设置Checkbox背景
        private StateElement elementButtonInit() {
            ShapeElement elementButtonOn = new ShapeElement();
            elementButtonOn.setRgbColor(RgbPalette.RED);
            elementButtonOn.setShape(ShapeElement.OVAL);

            ShapeElement elementButtonOff = new ShapeElement();
            elementButtonOff.setRgbColor(RgbPalette.BLACK);
            elementButtonOff.setShape(ShapeElement.OVAL);

            StateElement checkElement = new StateElement();
            checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, elementButtonOn);
            checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, elementButtonOff);

            return checkElement;
        }
        // 显示结果
        private void showAnswer() {
            Text answerText = (Text) findComponentById(ResourceTable.Id_text_answer3);
            String answer = selectedSet.toString();
            answerText.setText(answer);
        }

    // 初始化Checkbox
    private void initCheckbox1() {
        Checkbox checkbox1 = (Checkbox) findComponentById(ResourceTable.Id_check1_box);
        checkbox1.setButtonElement(elementButtonInit1());
        checkbox1.setCheckedStateChangedListener((component, state) -> {
            if (state) {
                selectedSet.add("A");
            } else {
                selectedSet.remove("A");
            }
            showAnswer1();
        });

        Checkbox checkbox2 = (Checkbox) findComponentById(ResourceTable.Id_check2_box);
        checkbox2.setButtonElement(elementButtonInit1());
        checkbox2.setCheckedStateChangedListener((component, state) -> {
            if (state) {
                selectedSet.add("B");
            } else {
                selectedSet.remove("B");
            }
            showAnswer1();
        });

        Checkbox checkbox3 = (Checkbox) findComponentById(ResourceTable.Id_check3_box);
        checkbox3.setButtonElement(elementButtonInit1());
        checkbox3.setCheckedStateChangedListener((component, state) -> {
            if (state) {
                selectedSet.add("C");
            } else {
                selectedSet.remove("C");
            }
            showAnswer1();
        });

        Checkbox checkbox4 = (Checkbox) findComponentById(ResourceTable.Id_check4_box);
        checkbox4.setButtonElement(elementButtonInit1());
        checkbox4.setCheckedStateChangedListener((component, state) -> {
            if (state) {
                selectedSet.add("D");
            } else {
                selectedSet.remove("D");
            }
            showAnswer1();
        });
    }
    // 设置Checkbox背景
    private StateElement elementButtonInit1() {
        ShapeElement elementButtonOn = new ShapeElement();
        elementButtonOn.setRgbColor(RgbPalette.RED);
        elementButtonOn.setShape(ShapeElement.OVAL);

        ShapeElement elementButtonOff = new ShapeElement();
        elementButtonOff.setRgbColor(RgbPalette.BLACK);
        elementButtonOff.setShape(ShapeElement.OVAL);

        StateElement checkElement = new StateElement();
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_CHECKED}, elementButtonOn);
        checkElement.addState(new int[]{ComponentState.COMPONENT_STATE_EMPTY}, elementButtonOff);

        return checkElement;
    }
    // 显示结果
    private void showAnswer1() {
        Text answerText = (Text) findComponentById(ResourceTable.Id_text_answer5);
        String answer = selectedSet.toString();
        answerText.setText(answer);
    }





    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
