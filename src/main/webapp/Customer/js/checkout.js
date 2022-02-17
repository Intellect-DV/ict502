const btnPayMethod = document.querySelectorAll(".payment_method__btn");
const btnPayAction = document.querySelector(".payment_action__btn");
const menuDetailHtml = document.querySelector(".menu__details");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const modalBackdrop = document.querySelector(".modal__backdrop");
const modalBtnYes = document.querySelector(".action > .btn-confirm.green");

window.addEventListener("DOMContentLoaded", () => {
    for(let btn of btnPayMethod) {
        btn.addEventListener("click", btnMethodHandler);
    }

    btnPayAction.addEventListener("click", () => {
        makePayment();
    })

    modalBtnYes.addEventListener("click", () => {
        window.location.href = "./view-order.jsp";
    })

    getMenus();
})

const btnMethodHandler = (event) => {
    const current = event.target;
    const {value} = current.dataset;

    for(let btn of btnPayMethod) {
        btn.classList = "payment_method__btn";
    }

    current.classList.add("active");
    btnPayAction.dataset.value = value;
}

const getMenus = () => {
    const url = "/cart?action=retrieveforcust";

    axios.get(url)
        .then(res => {
            const {message, carts, grand_total} = res.data;

            if(message === undefined) {
                let content = carts.map(cart => {
                    return `<div class="menu__item">
                                <span>${cart.menu_name}</span>
                                <i class="fa-solid fa-xmark"></i>
                                <span>${cart.menu_quantity}</span>
                            </div>`;
                }).join("");

                content += `<div class="menu__item result">
                                <span>Total: </span>
                                <span>${"RM " + parseFloat(grand_total).toFixed(2)}</span>
                            </div>`;

                menuDetailHtml.innerHTML = content;
            }
        })
        .catch(err => {
            console.log(err.response);
        });
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
        })
}