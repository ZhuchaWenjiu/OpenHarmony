export default {
    data: {
        likeImage: '/common/unLike.png',
        isPressed: false,
        total: 20,
    },
    likeClick() {
        var temp;
        if (!this.isPressed) {
            temp = this.total + 1;
            this.likeImage = '/common/like.png';
        } else {
            temp = this.total - 1;
            this.likeImage = '/common/unLike.png';
        }
        this.total = temp;
        this.isPressed = !this.isPressed;
    },
}