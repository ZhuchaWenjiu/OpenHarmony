package com.chinasofti.timer3.slice;

import com.chinasofti.timer3.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

import ohos.agp.components.Button;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.DirectionalLayout.LayoutConfig;
import ohos.agp.components.Text;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;

public class MainAbilitySlice extends AbilitySlice {

    Text tTitle;
    Text tTime;
    Button bButton;


    //记录时间
    int time = 0;
    MyEventHandler myEventHandler = null;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_timer);

        //1、通过id打开UI组件
        tTitle = (Text) findComponentById(ResourceTable.Id_t_title);
        tTime = (Text) findComponentById(ResourceTable.Id_t_time);
        bButton = (Button) findComponentById(ResourceTable.Id_b_button);

        //2、配置
        tTitle.setTruncationMode(Text.TruncationMode.AUTO_SCROLLING);
        tTitle.setAutoScrollingCount(-1);//表示无限次

        //启动
        tTitle.startAutoScrolling();


        EventRunner runner = EventRunner.getMainEventRunner();//2)获取线程Runner(),
        //3）创建EventHandler子类 对象
        myEventHandler = new MyEventHandler(runner);

        bButton.setClickedListener(component -> {
            if ("开始".equals(bButton.getText())) {
                bButton.setText("停止");
                //开始计时
                //4)调用myEventHandler,发起事件
                myEventHandler.sendEvent(1, 1000);
            } else {
                bButton.setText("开始");
                myEventHandler.removeAllEvent();//把事件从队列删除
            }
        });

    }


    //1）创建 EventHandler子类
    class MyEventHandler extends EventHandler {

        //这个runner就是要运行这个对象的线程，
        public MyEventHandler(EventRunner runner) throws IllegalArgumentException {
            super(runner);
        }

        //在当前烦躁 完成我们的功能

        @Override
        protected void processEvent(InnerEvent event) {
            super.processEvent(event);
            //完成更新时间，及UI
            time++;
            tTime.setText(getTimeStrin(time));//time int类型，
            //5)继续调用myEventHandler,继续计时
            myEventHandler.sendEvent(1, 1000);

        }
    }

    String getTimeStrin(int t) {
        return String.format("%02d:%02d:%02d", t / 60 / 60, t / 60 % 60, t % 60);
    }
}
