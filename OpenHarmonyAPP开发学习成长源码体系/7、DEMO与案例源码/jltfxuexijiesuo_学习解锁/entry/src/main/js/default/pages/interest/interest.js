import router from '@system.router';

export default {
    data: {
        title_interest: "按兴趣参与",
        neirong_interest:["分布式、全场景、基于未来，","内核、驱动、子系统、组件、移植、","设备、应用开发等；","让你尽情施展才能与个性！"],
        know_interest:"术语学习"
    },
    page_three(){
        router.push({
            uri:'pages/study/study'
        })
    }
}
