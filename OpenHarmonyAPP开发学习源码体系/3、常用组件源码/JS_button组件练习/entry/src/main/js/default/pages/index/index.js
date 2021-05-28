// xxx.js 
export default {
    data: {
        progress: 5,
        downloadText: "Download"
    },
    setProgress(e) {
        this.progress += 10;
        this.downloadText = this.progress + "%";
        this.$element('download-btn').setProgress({ progress: this.progress });
        if (this.progress >= 100) {
            this.downloadText = "Done";
        }
    }
}