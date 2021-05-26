
import prompt from '@system.prompt'
export default {
    change(e){
        prompt.showToast({
            message: "value: " + e.value,
            duration: 3000,
        });
    },
    enterkeyClick(e){
        prompt.showToast({
            message: "enterkey clicked",
            duration: 3000,
        });
    },
    buttonClick(e){
        this.$element("input").showError({
            error: 'error text'
        });
    },
    checkboxOnChange_one(e) {
        prompt.showToast({
            message:'足球为: ' + e.checked,
            duration: 3000,
        });
    },
    checkboxOnChange_two(e) {
        prompt.showToast({
            message:'篮球为: ' + e.checked,
            duration: 3000,
        });
    },
    checkboxOnChange_three(e) {
        prompt.showToast({
            message:'羽毛球为: ' + e.checked,
            duration: 3000,
        });
    },
    onRadioChange(inputValue, e) {
        if (inputValue === e.value) {
            prompt.showToast({
                message: '当前按钮为:' + e.value,
                duration: 3000,
            });
        }
    }
}