import prompt from '@system.prompt';

export default {
    showDialog(e) {
        this.$element('simpledialog').show()
    },
    cancelDialog(e) {
        prompt.showToast({
            message: '按钮取消了'
        })
    },
    cancelSchedule(e) {
        this.$element('simpledialog').close()
        prompt.showToast({
            message: 'Successfully cancelled'
        })
    },
    setSchedule(e) {
        this.$element('simpledialog').close()
        prompt.showToast({
            message: 'Successfully confirmed'
        })
    }
}