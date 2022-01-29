const formProfile = document.querySelector(".form_profile");
const newPassword = document.querySelector("#new-password");
const confirmPassword = document.querySelector("#confirm-password");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    formProfile.addEventListener("submit", event => {
        event.preventDefault();

        if(newPassword != null && confirmPassword != null) {
            if(!(newPassword.value === confirmPassword.value)) {
                modalContent.innerText = "Password did not match!";
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 5000);
                return;
            }
        }

        const url = "/worker";
        const formData = new FormData(formProfile);
        const params = new URLSearchParams();
        const {action} = formProfile.dataset;

        params.append("action", action);
        for(let key of formData.keys()) {
            params.append(key, String(formData.get(key)));
        }

        axios.post(url, params)
            .then(response => {
                const {message} = response.data;

                if(message === "Profile updated") {
                    modalContent.innerText = "Profile successfully updated!";
                    document.querySelector(".navbar__user").innerText = params.get("username");
                } else if(message === "Password updated"){
                    modalContent.innerText = "Password successfully updated!";
                }
                modalCard.className = "modal__card success";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 5000);
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
                setTimeout(closePopup, 5000);
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