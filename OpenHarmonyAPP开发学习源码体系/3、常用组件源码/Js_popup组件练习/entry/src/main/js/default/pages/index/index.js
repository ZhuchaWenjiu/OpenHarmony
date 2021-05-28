// xxx.js
import prompt from '@system.prompt';

export default {
    showDialog(e) {
        this.$element('simpledialog').show()
    },
    cancelDialog(e) {
        prompt.showToast({
            message: '取消对话框'
        })
    },
    cancelSchedule(e) {
        this.$element('simpledialog').close()
        prompt.showToast({
            message: '不同意'
        })
    },
    setSchedule(e) {
        this.$element('simpledialog').close()
        prompt.showToast({
            message: '已同意'
        })
    }
}