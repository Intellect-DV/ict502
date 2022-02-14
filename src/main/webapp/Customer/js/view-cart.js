const tbody = document.querySelector("#tbody");
const rootAction = document.querySelector("#root-action");
let currentOrderId = null;

const modalInfo = document.querySelector(".modal__info");
const modalCard= document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const modalBackdrop = document.querySelector(".modal__backdrop");
const modalBtnYes = document.querySelector(".action > .btn-confirm.red");
const modalBtnNo = document.querySelector(".action > .btn-confirm.grey");

window.addEventListener("DOMContentLoaded", () => {
    getCart();
})

const getCart = () => {
    const url = "/cart?action=retrieveforcust";

    axios.get(url)
        .then(res => {
            const {message} = res.data;

            if(!(message === undefined)) {
                tbody.innerHTML = `<tr><td colspan="5">Nothing added in cart</td></tr>`;
                rootAction.innerHTML = ``;
            } else {
                generateTableCart(res.data);
            }
        })
        .catch(err => {
            // make proper message
            const {error} = err.response.data;

            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup,2000);
        })
}

const generateTableCart = (data) => {
    const {carts, grand_total, order_id} = data;
    currentOrderId = order_id;

    let tableCarts = carts.map((cart, index) => {
       return `<tr>
                    <td>${index +1}</td>
                    <td>
                        <div class="cart__flex">
                            <div class="cart__menu_img">
                                <img src="${cart.image_url}" alt="${cart.menu_name}"/>
                            </div>
                            <div class="cart__menu_detail">
                                ${cart.menu_name}
                            </div>
                        </div>    
                    </td>
                    <td>
                        <div class="cart__price">
                            ${"RM " + parseFloat(cart.menu_price).toFixed(2)}
                        </div>
                    </td>
                    <td>
                        <div class="cart__quantity">
                            <button class="btn_quantity minus" onclick="minusQuantity(${cart.menu_id})">
                                <i class="fa-solid fa-minus"></i>
                            </button>
                            <span class="quantity">
                                ${cart.menu_quantity}
                            </span>
                            <button class="btn_quantity plus" onclick="addQuantity(${cart.menu_id})">
                                <i class="fa-solid fa-plus"></i>
                            </button>
                            <a class="btn_remove_cart" onclick="deleteCart(${cart.menu_id})">
                                <i class="fa-solid fa-trash-can"></i>
                            </a>
                        </div>
                    </td>
                    <td>
                        <div class="cart__total_price">
                            ${"RM " + parseFloat(cart.total_price).toFixed(2)}
                        </div>
                    </td>
               </tr>`
    }).join("");

    tableCarts += `<tr>
                    <td colspan="4" class="cart__grandtotal_display">
                        <div>
                            Total Amount
                        </div>
                    </td>
                    <td class="cart__grandtotal">
                        <span>
                            ${"RM " + parseFloat(grand_total).toFixed(2)}
                        </span>
                    </td>
                </tr>`;

    tbody.innerHTML = tableCarts;
    rootAction.innerHTML = `<a href="checkout.jsp">Checkout</a>`;
}

const addQuantity = (menuId) => {
    const url = "/cart";
    const params = new URLSearchParams();

    params.append("action", "add");
    params.append("menuId", menuId);

    axios.post(url, params)
        .then(res => {
            const {message} = res.data;

            if(message === "Added to Cart") {
                getCart();
            }
        })
        .catch(err => {
            const {error} = err.response.data;
            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup,2000);

            modalBackdrop.className = "modal__backdrop hide"
        })
}

const minusQuantity = (menuId) => {
    const url = "/cart";
    const params = new URLSearchParams({
        "action": "remove",
        "menuId": menuId
    });

    axios.post(url, params)
        .then(res => {
            const {message} = res.data;

            if(message === "Removed from cart") {
                getCart();
            }
        })
        .catch(err => {
            const {error} = err.response.data;
            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup,2000);

            modalBackdrop.className = "modal__backdrop hide"
        })
}

const deleteCart = (menuId) => {
    modalBtnYes.dataset.menuId = menuId;
    modalBackdrop.className = "modal__backdrop";
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())

modalBtnNo.addEventListener("click", () => modalBackdrop.className = "modal__backdrop hide")

modalBtnYes.addEventListener("click", () => {
    // delete menu
    const {menuId} = modalBtnYes.dataset;
    const url = "/cart";
    const params = new URLSearchParams({
        "action": "delete",
        "menuId": menuId
    });

    axios.post(url, params)
        .then(res => {
            const {message} = res.data;

            if(message === "Cart item deleted") {
                modalContent.innerText = message;
                modalCard.className = "modal__card success";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup,2000);

                getCart();
                modalBackdrop.className = "modal__backdrop hide"
            }
        })
        .catch(err => {
            const {error} = err.response.data;
            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup,2000);

            modalBackdrop.className = "modal__backdrop hide"
        });

})