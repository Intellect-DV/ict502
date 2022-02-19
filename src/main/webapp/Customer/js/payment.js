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

    paymentForm.addEventListener("submit", (event) => {
        event.preventDefault();
        makePayment();
    })

    modalBtnYes.addEventListener("click", () => {
        window.location.href = "./view-order.jsp";
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

const makePayment = () => {
    const url = "/payment";
    const params = new URLSearchParams({"action": "makepayment"});

    axios.post(url, params)
        .then(res => {
            const {message} = res.data;

            if(message === "Payment created") {
                modalBackdrop.className = "modal__backdrop";
            }
        })
        .catch(err => {
            const {error} = err.response.data;

            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 1500);
        })
}

function closePopup() {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => {
    closePopup();
})