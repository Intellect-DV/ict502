const tbody = document.querySelector("#tbody");
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
            } else {
                generateTableCart(res.data);
            }
        })
        .catch(err => {
            // todo - make propper message
            console.log(err.response.data);
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
                            <button class="btn_quantity minus">
                                <i class="fa-solid fa-minus"></i>
                            </button>
                            <span class="quantity">
                                ${cart.menu_quantity}
                            </span>
                            <button class="btn_quantity plus">
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
}

const addQuantity = (event) => {

}

const minusQuantity = (event) => {

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
    // todo - delete menu
    const {menuId} = modalBtnYes.dataset;
    alert(menuId);

    modalBackdrop.className = "modal__backdrop hide"
})