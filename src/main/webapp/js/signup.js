const formSignUp = document.querySelector(".form-signup");
const modalInfo =  document.querySelector(".modal__info");
const modalContent = document.querySelector(".modal__content");

window.addEventListener("DOMContentLoaded",(event) => {
    formSignUp.addEventListener("submit", event => {
        event.preventDefault();

        const url = "/customer";
        const params = new URLSearchParams();
        const formData = new FormData(formSignUp);

        for(let key of formData.keys()) {
            params.append(key, String(formData.get(key)));
        }

        params.append("action", "signup");

        axios.post(url, params)
            .then(response => {

            })
            .catch(error => {

            })
    })
})