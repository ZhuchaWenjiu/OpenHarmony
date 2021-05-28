export default {
    data: {
        currentIndex:0,
        title: 'World',
        specilties:[
                {"cname":"专卖区","cimg":"common/tu4.png","price":"50"},
                {"cname":"特价区","cimg":"common/niurou.jpg","price":"50"},
                {"cname":"宠物区","cimg":"common/tuzi.jpg","price":"50"},
                {"cname":"烧鸭","cimg":"common/yazi.jpg","price":"50"},
                {"cname":"老腊肉香肠","cimg":"common/larou.jpg","price":"50"},
                {"cname":"蜀绣","cimg":"common/shuxiu.jpg","price":"50"},
                {"cname":"金品好酒","cimg":"common/jiu.jpg","price":"50"},
                {"cname":"泸州老窖","cimg":"common/luzhou.jpg","price":"50"},
                {"cname":"竹叶青","cimg":"common/emeishan.jpg","price":"50"},
                {"cname":"火锅料","cimg":"common/shudaxia.jpg","price":"50"},


        ],
        first:[  {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
        ],
        second:[ {"cname":"什锦牛肉","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
            {"cname":"蛟龙腾飞","cimg":"common/tu4.png","price":"50"},
        ],
        third:[{"cname":"双流老妈兔头","cimg":"common/mao.jpg","price":"50","lname":[{"fname":"川香麻辣","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"五香原味","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"微微辣","icon":"common/zhang3.jpg","icon1":"common/add.png"}]},
            {"cname":"蛟龙腾飞","cimg":"common/mao.jpg","price":"50","lname":[{"fname":"川香麻辣","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"五香原味","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"微微辣","icon":"common/zhang3.jpg","icon1":"common/add.png"}]},
            {"cname":"蛟龙腾飞","cimg":"common/mao.jpg","price":"50","lname":[{"fname":"川香麻辣","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"五香原味","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"微微辣","icon":"common/zhang3.jpg","icon1":"common/add.png"}]},
            {"cname":"蛟龙腾飞","cimg":"common/mao.jpg","price":"50","lname":[{"fname":"川香麻辣","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"五香原味","icon":"common/zhang3.jpg","icon1":"common/add.png"},{"fname":"微微辣","icon":"common/zhang3.jpg","icon1":"common/add.png"}]},

        ],
        forth:[{"cname":"南充杨鸭子","cimg":"common/yazi1.png","price":"50"},
            {"cname":"肖三婆板鸭","cimg":"common/yazhi2.png","price":"50"},
            {"cname":"乐山甜皮鸭","cimg":"common/yazi1.png","price":"50"},
            {"cname":"彭州九尺鸭","cimg":"common/yazhi2.png","price":"50"},
            {"cname":"乐山赵鸭子","cimg":"common/yazi1.png","price":"50"},
        ],
        seventh:[{"cname":"五粮浓香","cimg":"common/wu1.jpg","price":"50"},
            {"cname":"万事如意","cimg":"common/wu11.jpg","price":"50"},
            {"cname":"五粮国宾","cimg":"common/wu14.jpg","price":"50"},
            {"cname":"永不分梨","cimg":"common/wu12.jpg","price":"50"},
            {"cname":"仙林生态","cimg":"common/wu13.jpg","price":"50"}
        ],
        eleventh:[{"cname":"大龙燚","cimg":"common/dalong.jpg","lname":[{"fname":"麻辣牛油","icon":"common/huo9.jpg","icon1":"common/add.png"},{"fname":"菌汤番茄","icon":"common/huo10.jpg","icon1":"common/add.png"},{"fname":"自热锅香油干碟","icon":"common/huo11.jpg","icon1":"common/add.png"}]},
            {"cname":"小龙坎","cimg":"common/xiaolong.jpg","lname":[{"fname":"麻辣牛油","icon":"common/huo5.jpg","icon1":"common/add.png"},{"fname":"菌汤番茄","icon":"common/huo6.jpg","icon1":"common/add.png"},{"fname":"自热锅香油干碟","icon":"common/huo7.jpg","icon1":"common/add.png"}]},
            {"cname":"蜀大侠","cimg":"common/shuda.jpg","lname":[{"fname":"麻辣牛油","icon":"common/huo1.jpg","icon1":"common/add.png"},{"fname":"菌汤番茄","icon":"common/huo2.jpg","icon1":"common/add.png"},{"fname":"调味香油干碟","icon":"common/huo3.jpg","icon1":"common/add.png"}]},
            {"cname":"川西坝子","cimg":"common/chuanxi.jpg","lname":[{"fname":"麻辣牛油","icon":"common/huo14.jpg","icon1":"common/add.png"},{"fname":"菌汤番茄","icon":"common/huo15.jpg","icon1":"common/add.png"},{"fname":"调味香油干碟","icon":"common/huo16.jpg","icon1":"common/add.png"}]}
        ],
        changeview(index){
            this.$element("swiperview").swipeTo({index:index});
            this.currentIndex=index;
        },
        changeswiper(e){
            this.currentIndex=e.index;
        },
        changemenu(){

        }
    }
}
