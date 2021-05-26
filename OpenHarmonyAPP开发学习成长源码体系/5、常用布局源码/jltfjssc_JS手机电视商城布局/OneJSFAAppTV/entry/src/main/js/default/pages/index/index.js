export default {
    data: {
        cartText:'添加至购物车',
        cartStyle:'cart-text',
        isCartEmpty:true,
        descriptionFirstParagraph:'在中国传统文化教育中的阴阳五行哲学思想、道家理学观念，儒家伦理道德观念，还有文化艺术成就、饮食审美风尚、民族性格特征诸多因素的影响下，创造出彪炳史册的中国烹饪技艺，形成博大精深的中国饮食文化。',
        imageList:['/common/tupan1.jpg','/common/tupian2.jpg','/common/tupian3.jpg','/common/tupian4.jpg']
    },

    swiperToIndex(index){
        this.$element('swiperImage').swiperTo({index:index});
    },

    addCart(){
        if(this.isCartEmpty){
            this.cartText="添加购物车成功";
            this.cartStyle="add-cart-text";
            this.isCartEmpty=false;
        }
    },

    getFocus() {
        if (this.isCartEmpty) {
            this.cartStyle = "cart-text-focus";
        }
    },

    lostFocus(){
        if(this.isCartEmpty){
            this.cartStyle="cart-text";
        }
    }
}
