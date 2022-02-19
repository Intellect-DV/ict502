const paymentForm = document.querySelector(".payment__form");
const paymentOptions = document.querySelectorAll(".payment__tab > .option");
let creditDebitHtml;
let fpxHtml;

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const modalBackdrop = document.querySelector(".modal__backdrop");
const modalBtnYes = document.querySelector(".action > .btn-confirm.green");

window.addEventListener("DOMContentLoaded", () => {
    paymentOptions.forEach((option) => {
        option.addEventListener("click", paymentOptionHandler)
    })

    getHtmlForm();
})

const paymentOptionHandler = (event) => {
    const current = event.target;
    const {option} = current.dataset;

    paymentOptions.forEach((option) => {
        option.className = "option";
    })

    current.classList.add("active");

    if(option === "fpx") {
        paymentForm.innerHTML = fpxHtml;
        return;
    }

    paymentForm.innerHTML = creditDebitHtml;
}

const getHtmlForm = () => {
    Promise.all([getCreditDebitForm(), getFpxForm()])
        .then(contents => {
            creditDebitHtml = contents[0].data;
            fpxHtml = contents[1].data;

            paymentForm.innerHTML = creditDebitHtml;
        })
}

const getCreditDebitForm = () => {
   return axios.get("component/payment__credit_debit.html");
}

const getFpxForm = () => {
    return axios.get("component/payment__fpx.html");
}