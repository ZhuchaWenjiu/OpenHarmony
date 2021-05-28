import prompt from '@system.prompt';

export default {
    data: {
        direction: 'column',
        list: []
    },
    onInit() {
        this.list = []
        this.listAdd = []
        for (var i = 1; i <= 3; i++) {
            var dataItem = {
                value: '组' + i,
            };
            this.list.push(dataItem);
        }
    },
    collapseOne(e) {
        this.$element('mylist').collapseGroup({
            groupid: '组1'
        })
    },
    expandOne(e) {
        this.$element('mylist').expandGroup({
            groupid: '组1'
        })
    },
    collapseAll(e) {
        this.$element('mylist').collapseGroup()
    },
    expandAll(e) {
        this.$element('mylist').expandGroup()
    },
    collapse(e) {
        prompt.showToast({
            message: '关闭 ' + e.groupid
        })
    },
    expand(e) {
        prompt.showToast({
            message: '打开 ' + e.groupid
        })
    }
}
