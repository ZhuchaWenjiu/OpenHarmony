package com.example.harmonyos_jltf.slice;

import com.example.harmonyos_jltf.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;

import java.util.ResourceBundle;

public class LoginAbilitySlice extends AbilitySlice {
    private Button button_zhuce;
    private TextField name_zhuce,password_zhuce,password_queren;
    private Context context;
    private ResourceBundle bundle;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_zhuce);
        context = getContext();

        name_zhuce = (TextField) findComponentById(ResourceTable.Id_name_zhuce);
        password_zhuce = (TextField) findComponentById(ResourceTable.Id_password_zhuce);

        button_zhuce = (Button) findComponentById(ResourceTable.Id_login_btn2);

        password_queren = (TextField) findComponentById(ResourceTable.Id_password_queren);

        zhuce_login();
    }

    private void zhuce_login(){

            // 为按钮设置点击回调
            button_zhuce.setClickedListener(new Component.ClickedListener() {
                @Override
                public void onClick(Component component) {
                    
                    String name =name_zhuce.getText().toString().trim();
                    String password = password_zhuce.getText().toString().trim();
                    String queren = password_queren.getText().toString().trim();
                    
                    if(!name.equals(null) && !name.equals("") && !password.equals(queren)){
                        new ToastDialog(context).setText("密码不一致，请重新尝试！").show();
                         }else if (button_zhuce!=null && !name.equals(null)&& !name.equals("") && !password.equals(null) && !password.equals("") &&  password.equals(queren)){
                                new ToastDialog(context).setText("注册成功！").show();
                                present(new MainAbilitySlice(),new Intent());
                             }else{
                        new ToastDialog(context).setText("请输入完整信息！").show();
                    }
                }
            });
    };
}
