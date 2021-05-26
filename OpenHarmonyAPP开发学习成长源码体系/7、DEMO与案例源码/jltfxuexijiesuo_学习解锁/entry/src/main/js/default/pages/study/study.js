import router from '@system.router';
export default {
    data: {
        title:["点击按钮","解锁术语解释与学习"],
        arrs:[
            {
                'id':'Ability',
                'value':'应用的重要组成部分，是应用所具备能力的抽象。Ability分为两种类型，Feature Ability和Particle Ability。'
            },
            {
                'id':'AbilitySlice',
                'value':'切片，是单个可视化界面及其交互逻辑的总和，是Feature Ability的组成单元。一个Feature Ability可以包含一组业务关系密切的可视化界面，每一个可视化界面对应一个AbilitySlice。'
            },
            {
                'id':'ANS',
                'value':'Advanced Notification Service，通知增强服务，是HarmonyOS中负责处理通知的订阅、发布和更新等操作的系统服务。'
            },
            {
                'id':'CES',
                'value':'Common Event Service，是HarmonyOS中负责处理公共事件的订阅、发布和退订的系统服务。'
            },
            {
                'id':'DV',
                'value':'Device Virtualization，设备虚拟化，通过虚拟化技术可以实现不同设备的能力和资源融合。'
            },
            {
                'id':'FA',
                'value':'Feature Ability，元程序，代表有界面的Ability，用于与用户进行交互。'
            },
            {
                'id':'HAP',
                'value':'HarmonyOS Ability Package，一个HAP文件包含应用的所有内容，由代码、资源、三方库及应用配置文件组成，其文件后缀名为.hap。'
            },
            {
                'id':'HDF',
                'value':'Hardware Driver Foundation，硬件驱动框架，用于提供统一外设访问能力和驱动开发、管理框架。'
            },
            {
                'id':'IDN',
                'value':'Intelligent Distributed Networking，是HarmonyOS特有的分布式组网能力单元。开发者可以通过IDN获取分布式网络内的设备列表和设备状态信息，以及注册分布式网络内设备的在网状态变化信息。'
            },
            {
                'id':'MSDP',
                'value':'Mobile Sensing Development Platform，移动感知平台。MSDP子系统提供两类核心能力：分布式融合感知和分布式设备虚拟化。'
            },
            {
                'id':'PA',
                'value':'Particle Ability，元服务，代表无界面的Ability，主要为Feature Ability提供支持，例如作为后台服务提供计算能力，或作为数据仓库提供数据访问能力。'
            },
            {
                'id':'Supervirtualdevice',
                'value':'超级虚拟终端亦称超级终端，通过分布式技术将多个终端的能力进行整合，存放在一个虚拟的硬件资源池里，根据业务需要统一管理和调度终端能力，来对外提供服务。'
            }
        ],
        show_text:'',
        know_study:"了解更多"
    },
    show(e){
        console.info(e)
        var list =this;
        this.arrs.forEach(element => {
            console.info(element.id)
            if (element.id==e) {
                list.show_text = element.value
            }
        });
        console.info(list.show_text);
    },
    page_four(){
        router.push({
            uri:'pages/harmonyos/harmonyos'
        })
    }
}
