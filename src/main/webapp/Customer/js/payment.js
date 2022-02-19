const paymentContent = document.querySelector(".payment__content");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const modalBackdrop = document.querySelector(".modal__backdrop");
const modalBtnYes = document.querySelector(".action > .btn-confirm.green");

window.addEventListener("DOMContentLoaded", () => {
    getCreditDebitForm()
})

const getCreditDebitForm = () => {
   axios.get("component/payment__credit_debit.html")
       .then(res => {
           paymentContent.innerHTML = res.data;
        })
       .catch(err => {
            console.log(err.response.data);
       })
}