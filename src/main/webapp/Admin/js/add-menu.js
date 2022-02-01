const formAddMenu = document.querySelector("#form_add_menu");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    formAddMenu.addEventListener("submit", event => {
        event.preventDefault();

        const formData = new FormData(formAddMenu);
        const {enctype} = formAddMenu;
        const url = "/menu?action=createmenu"

        const config = {
            method: 'post',
            url: url,
            headers: {
                "Content-Type": enctype
            },
            data : formData
        };

        axios(config)
            .then(response => {
                // success message
                const {message} = response.data;

                modalContent.innerText = message;
                modalCard.className = "modal__card success";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            })
            .catch(err => {
                // error message
                const {error} = err.response.data;

                modalContent.innerText = error;
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            })
    })
})

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())