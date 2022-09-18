
function handleCreateNewContract() {
    function handleChoseFile(evt) {
        const $files = evt.target;
        const { files } = $files;
        if (files && files[0]) {
            const file = files[0];
            const { name, size } = file;
            const fileSize = (size / 1000000).toFixed(4);

            const $fileName = evt.currentTarget.querySelector('.file-name');
            $fileName.textContent = `${name} - ${fileSize} MB`;
        }
    }

    const $file = document.getElementById('upload-contract');
    $file.addEventListener('change', handleChoseFile);
}

export {
    handleCreateNewContract
};