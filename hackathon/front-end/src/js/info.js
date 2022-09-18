import flatpickr from "flatpickr";
import 'flatpickr/dist/flatpickr.min.css';

function handleInfo() {
    const validObj = {
        birthday: false,
        dayRelease: false,
        businessName: false,
        address: false,
        fullName: false,
        businessCode: false,
        taxCode: false,
        phoneNumber: false,
        cmnd: false
    };

    function initPicker() {
        const $birthday = document.querySelector('#info-page .birth-day');
        const $birthdayInput = $birthday.querySelector('input');
        const $dayRelease = document.querySelector('#info-page .day-release');
        const $dayReleaseInput = $dayRelease.querySelector('input');

        flatpickr($birthdayInput, {
            onChange: function handleOnChange() {
                if (!$birthdayInput.classList.contains('is-valid')) {
                    $birthdayInput.classList.add('is-valid');
                    validObj.birthday = true;
                    validSubmit();
                }
            }
        });

        flatpickr($dayReleaseInput, {
            onChange: function handleOnChange($el) {
                if (!$dayReleaseInput.classList.contains('is-valid')) {
                    $dayReleaseInput.classList.add('is-valid');
                    validObj.dayRelease = true;
                    validSubmit();
                }
            }
        });
    }

    function initInputs() {
        const $businessName = document.querySelector('#info-page .business-name input');
        const $address = document.querySelector('#info-page .address input');
        const $fullName = document.querySelector('#info-page .full-name input');

        function handleInputChange(evt, key) {
            const $el = evt.currentTarget;
            const value = $el.value.trim();
            $el.classList.remove('is-valid');
            $el.classList.remove('is-invalid');

            if (value) {
                $el.classList.add('is-valid');
                validObj[key] = true;
            } else {
                $el.classList.add('is-invalid');
                validObj[key] = false;
            }

            validSubmit();
        }

        function handleInputNumberChange(evt, number, label, key) {
            const $el = evt.currentTarget;
            const $valid = $el.nextElementSibling;
            const value = $el.value.trim();
            $el.classList.remove('is-valid');
            $el.classList.remove('is-invalid');

            if (!value) {
                $valid.textContent = `Vui lòng nhập ${label}`;
                $el.classList.add('is-invalid');
                validObj[key] = false;
            } else {
                if (value.length !== number) {
                    $valid.textContent = `Vui lòng nhập đẩy đủ ${number} ${label}`;
                    $el.classList.add('is-invalid');
                    validObj[key] = false;
                } else {
                    $el.classList.add('is-valid');
                    validObj[key] = true;
                }
            }

            validSubmit();
        }

        $businessName.addEventListener('change', evt => handleInputChange(evt, 'businessName'));
        $address.addEventListener('change', evt => handleInputChange(evt, 'address'));
        $fullName.addEventListener('change', evt => handleInputChange(evt, 'fullName'));

        const $businessCode = document.querySelector('#info-page .business-code input');
        const $taxCode = document.querySelector('#info-page .tax-code input');
        const $phoneNumber = document.querySelector('#info-page .phone-number input');
        const $cmnd = document.querySelector('#info-page .cmnd input');

        $businessCode.addEventListener('change', evt => handleInputNumberChange(evt, 10, 'mã số doanh nghiệp', 'businessCode'));
        $taxCode.addEventListener('change', evt => handleInputNumberChange(evt, 10, 'mã số thuế', 'taxCode'));
        $phoneNumber.addEventListener('change', evt => handleInputNumberChange(evt, 10, 'số điện thoại', 'phoneNumber'));
        $cmnd.addEventListener('change', evt => handleInputNumberChange(evt, 9, 'số CMND', 'cmnd'));
    }

    function validSubmit() {
        const values = Object.values(validObj);
        const arr = values.filter(item => !Boolean(item));
        if (!arr.length) {
            const $btnRegister = document.querySelector('.btn-register');
            $btnRegister.classList.remove('disabled');
        }
    }

    initPicker();
    initInputs();
}

export {
    handleInfo
};