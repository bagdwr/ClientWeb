let cancelBtn = document.querySelector('.cancel-btn');
cancelBtn.addEventListener('click', () => {
    console.log('x5H2Y2BB82')
    location.reload();
})

const numericInput = document.querySelectorAll('.numeric-input');
numericInput.forEach((x) => {
    x.addEventListener('keydown', function (event) {
        // Allow: backspace, delete, tab, escape, and enter
        if (event.keyCode === 46 || event.keyCode === 8 || event.keyCode === 9 || event.keyCode === 27 || event.keyCode === 13 ||
            // Allow: Ctrl+A
            (event.keyCode === 65 && event.ctrlKey === true) ||
            // Allow: Ctrl+C
            (event.keyCode === 67 && event.ctrlKey === true) ||
            // Allow: Ctrl+V
            (event.keyCode === 86 && event.ctrlKey === true) ||
            // Allow: Ctrl+X
            (event.keyCode === 88 && event.ctrlKey === true) ||
            // Allow: home, end, left, right
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            return;
        }
        // Ensure that it is a number and stop the keypress
        if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105)) {
            event.preventDefault();
        }
    });
})

function navigateByUrl(url) {
    window.location.href = url;
}


function showPopup(name, filesAmount, size, created_at) {
    let cont = document.querySelector('.content')
    cont.classList.toggle('hide-content')
    cont.innerHTML = name + '<br>' + 'количество файлов:' + filesAmount + '<br>' + 'размер папки:' + size + 'мб<br>' + 'создано:' + created_at;
}

function clearPopup() {
    let cont = document.querySelector('.content')
    cont.textContent = '';
    cont.classList.toggle('hide-content')

}
