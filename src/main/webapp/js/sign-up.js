const formSignUp = document.querySelector(".form-signup");
const inputPass = document.querySelector("input[name='password']");
const inputPassConf = document.querySelector("input[name='password-confirm']");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded",(event) => {
    formSignUp.addEventListener("submit", event => {
        event.preventDefault();

        if(!(inputPass.value === inputPassConf.value)) {
            modalContent.innerText = "Password did not match!";
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            return;
        }

        const url = "/customer";
        const params = new URLSearchParams();
        const formData = new FormData(formSignUp);

        for(let key of formData.keys()) {
            params.append(key, String(formData.get(key)));
        }

        params.append("action", "signup");

        axios.post(url, params)
            .then(response => {
                const {message} = response.data;
                if(message === "New user added") {
                    modalContent.innerText = "Successfully registered!";
                    modalCard.className = "modal__card success";
                    modalInfo.className = "modal__info active";
                }
            })
            .catch(err => {
                const {error} = err.response.data;

                if(error === "Username duplicated") {
                    modalContent.innerText = "Register failed! Please use another username.";
                    modalCard.className = "modal__card failed";
                    modalInfo.className = "modal__info active";
                }
            })
    })

    modalClose.addEventListener("click", () => {
        if(modalInfo.className === "modal__info active") {
            modalInfo.className = "modal__info";
        }
    })
})