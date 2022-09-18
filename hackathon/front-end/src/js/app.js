
import { handleCreateNewContract } from './create-new';
import { handleReviewContract } from './review';
import { handleInfo } from './info';

function App() {
    const $infoPage = document.getElementById('info-page');
    const $reviewContract = document.getElementById('review-contract');
    const $createNewContract = document.getElementById('create-new-contract-page');

    if ($infoPage) {
        handleInfo();
    }

    if ($createNewContract) {
        handleCreateNewContract();
    }

    if ($reviewContract) {
        handleReviewContract();
    }

    const $dropdowns = document.querySelectorAll('.dropdown');
    $dropdowns.forEach($dropdown => {
        const $btn = $dropdown.querySelector('.btn');
        const $dropdownList = $dropdown.querySelector('.dropdown-menu');
        $btn.addEventListener('click', () => {
            $dropdownList.classList.toggle('show');
        });
    });
}

export default App;