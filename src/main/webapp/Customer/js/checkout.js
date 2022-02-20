const btnPayProceed = document.querySelector(".payment_action__proceed_btn");
const menuDetailHtml = document.querySelector(".menu__details");
const displayTotal = document.querySelector(".details__total > .display_total");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    btnPayProceed.addEventListener("click", () => {
        window.location.href = "./payment.jsp";
    })
    getMenus();
})

const getMenus = () => {
    const url = "/cart?action=retrieveforcust";

    axios.get(url)
        .then(res => {
            const {message, carts, grand_total} = res.data;
            displayTotal.innerText = "RM " + parseFloat(grand_total).toFixed(2);

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
            const {error} = err.response.data;

            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 1500);
        });
}

function closePopup() {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => {
    closePopup();
})