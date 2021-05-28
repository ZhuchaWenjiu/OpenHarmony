export default {
    data: {
        defaultTime: "",
        time: "",
    },
    onInit() {
        this.defaultTime = this.now();
    },
    handleChange(data) {
        this.time = this.concat(data.hour, data.minute);
    },
    now() {
        const date = new Date();
        const hours = date.getHours();
        const minutes = date.getMinutes();
        return this.concat(hours, minutes);
    },

    fill(value) {
        return (value > 9 ? "" : "0") + value;
    },

    concat(hours, minutes) {
        return `${this.fill(hours)}:${this.fill(minutes)}`;
    },
}
