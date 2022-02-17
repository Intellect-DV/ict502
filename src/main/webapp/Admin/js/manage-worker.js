const navItem = document.querySelector(".navigation__item[href='./manage-worker.jsp']");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const form = document.querySelector(".form__add_worker");

window.addEventListener("DOMContentLoaded", () => {
    navItem.classList.add("active");
    form.addEventListener("submit", event => {
        event.preventDefault();

        const formData = new FormData(form);
        const params = new URLSearchParams();

        params.append("action","add");
        for(let key of formData.keys()) {
            params.append(key, String(formData.get(key)));
        }

        axios.post("/worker", params)
            .then(response => {
                const {message} = response.data;

                modalContent.innerText = message;
                if(message === "New worker added") {
                    modalContent.innerText = "Worker successfully registered";
                    modalCard.className = "modal__card success";
                    modalInfo.className = "modal__info active";
                    setTimeout(closePopup, 3000);
                }
            })
            .catch(err => {
                const {error} = err.response.data;

                modalContent.innerText = error;
                if(error === "Username duplicated") {
                    modalContent.innerText = "Username duplicated! Please use another username.";
                }
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            })
    })
})

function closePopup() {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => {
    closePopup();
})