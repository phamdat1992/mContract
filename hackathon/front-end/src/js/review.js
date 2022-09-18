function handleReviewContract() {
    const $modalBackdrop = document.querySelector('.modal-otp-backdrop');
    const $modalOtp = document.querySelector('.modal-otp');
    const $btnSignContract = document.querySelector('.btn-sign');
    const $btnCancelSign = document.querySelector('.btn-cancel-sign');
    const $btnConfirmSign = document.querySelector('.btn-confirm-sign');

    function openModal() {
        $modalBackdrop.classList.add('show', 'fade');
        $modalOtp.classList.add('show', 'fade');
        document.body.classList.add('modal-open');
    }

    function closeModal() {
        $modalBackdrop.classList.remove('show', 'fade');
        $modalOtp.classList.remove('show', 'fade');
        document.body.classList.remove('modal-open');
    }

    function onBtnConfirmClick() {
        const $otpInput = document.querySelector('.form-group #otp');
        const value = $otpInput.value.trim();
        if (!value) {
            $otpInput.classList.add('is-invalid');
            return;
        }

        let url = './dashboard-signed.html';
        if (document.body.classList.contains('is-b-part')) {
            url = './b-dashboard-signed.html'
        }

        window.location.href = url;
    }

    $btnSignContract.addEventListener('click', openModal);
    $btnCancelSign.addEventListener('click', closeModal);
    $btnConfirmSign.addEventListener('click', onBtnConfirmClick);
}

export {
    handleReviewContract
};