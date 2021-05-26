import router from '@system.router';
export default {
    data: {
       image:'/image/zhishi.jpg'
    },
    button_image(){
        router.push({
            uri:'pages/index/index'
        })
    }
}
