import router from '@system.router';
export default {
    data: {
        title: "鸿蒙开发者",
        neirong:["满载使命、","荣誉而又大有前途，","共建我们自己的新操作系统生态！"],
        know:"了解更多"
    },
    page_two(){
        router.push({
            uri:'pages/interest/interest'
        })
    }
}
