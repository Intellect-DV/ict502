const form = document.querySelector("#form-signin");
const toggleBtn = document.querySelector("#toggle__ch");

window.addEventListener("DOMContentLoaded", () => {
    toggleBtn.addEventListener("change", event => {
        if(toggleBtn.checked) {
            form.action = "/worker/"; // todo - worker servlet
        } else {
            form.action = "/customer/"; // todo - customer servlet
        }
    })
})