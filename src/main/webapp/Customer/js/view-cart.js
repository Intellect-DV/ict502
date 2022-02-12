const tbody = document.querySelector("#tbody");

const modalInfo = document.querySelector(".modal__info");
const modalCard= document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    getCart();
})

const getCart = () => {
    const url = "/cart?action=retrieveforcust";

    axios.get(url)
        .then(res => {
            const {message} = res.data;

            if(!(message === undefined)) {
                // todo - make proper message
                console.log("Message",message);
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
                            <a href="#" class="btn_remove_cart">
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

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())