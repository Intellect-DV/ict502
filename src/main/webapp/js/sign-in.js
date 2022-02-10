const formSignIn = document.querySelector("#form-signin");
const toggleBtn = document.querySelector("#toggle__ch");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    toggleBtn.addEventListener("change", event => {
        if(toggleBtn.checked) {
            formSignIn.dataset.action = "worker";
        } else {
            formSignIn.dataset.action = "customer";
        }
    })

    formSignIn.addEventListener("submit", event => {
        event.preventDefault();
        const {action} = formSignIn.dataset;

        if(action == null) return;

        const url = action === "worker" ? "/worker" : "/customer";
        const formData = new FormData(formSignIn);
        const params = new URLSearchParams();

        params.append("action", "login")
        for(let key of formData.keys()) {
            params.append(key,String(formData.get(key)))
        }

        axios.post(url, params)
            .then(response => {
                const {message, type} = response.data;
                if(message === "Login success!") {
                    let url = "/Customer/";
                    switch (type.toLowerCase()) {
                        case "admin":
                            url = "/Admin/";
                            break;
                        case "worker":
                            url = "/Worker/";
                            break;
                    }

                    window.location.assign(url);
                }
            })
            .catch(err => {
                const {error} = err.response.data;

                modalContent.innerText = error;
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            })
    })
    toggleBtn.checked = false;
})

modalClose.addEventListener("click", () => {
    closePopup();
})

function closePopup() {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}
