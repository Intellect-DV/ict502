const formProfile = document.querySelector(".form_profile");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    formProfile.addEventListener("submit", event => {
        event.preventDefault();

        const url = "/worker";
        const formData = new FormData(formProfile);
        const params = new URLSearchParams();

        params.append("action", "updateprofile");
        for(let key of formData.keys()) {
            params.append(key, String(formData.get(key)));
        }

        axios.post(url, params)
            .then(response => {
                const {message} = response.data;

                if(message === "Profile updated") {
                    modalContent.innerText = "Profile successfully updated!";
                    modalCard.className = "modal__card success";
                    modalInfo.className = "modal__info active";
                }
            })
            .catch(err => {
                const {error} = err.response.data;

                if(error === "Username duplicated") {
                    modalContent.innerText = "Cannot update profile! Please use another username.";
                } else {
                    modalContent.innerText = error;
                }
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
            })
    })
})

modalClose.addEventListener("click", () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
})