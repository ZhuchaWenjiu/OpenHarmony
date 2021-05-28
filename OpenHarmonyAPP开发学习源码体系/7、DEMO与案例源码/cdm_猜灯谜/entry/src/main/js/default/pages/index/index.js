import router from '@system.router';
export default {
    data: {
        title: ["猜灯谜", "点击按钮解锁答案"],
        arrs: [
                {
                    'id': '一棵树，两只兔。一斜路，在南处。猜一个字。',
                    'value': '答案（少）'
                },
                {
                    'id': '人上，一星亮。一见面，一声汪。猜一个字',
                    'value': '答案（犬）'
                },
                {
                    'id': '戈手心，相公心，您藏心，在疗心。猜四个字。',
                    'value': '答案（我想你了）'
                },
                {
                    'id': '立湍后，许久默，凤冠女，慷心落。猜四个字。',
                    'value': '答案（端午安康）'
                },
                {
                    'id': '一人打虎，一人牵犬。一人趴地，一人哮天。猜一个字。',
                    'value': '答案（伏）'
                }
        ],
        show_text: ''
    },
    show(e) {
        console.info(e)
        var list = this;
        this.arrs.forEach(element => {
            console.info(element.id)
            if (element.id == e) {
                list.show_text = element.value
            }
        });
        console.info(list.show_text);
    }
}
