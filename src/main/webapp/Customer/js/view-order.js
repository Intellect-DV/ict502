const orderTable = document.querySelector(".order__table");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    getOrders();
})

const getOrders = () => {
    const url = "/order?action=retrieveorderforcust";

    axios.get(url)
        .then(res => {
            const {message, orders} = res.data;

            if(!(message === undefined)) {
                modalContent.innerText = message;
                modalCard.className = "modal__card alert";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            } else {
                generateOrdersHtml(orders);
            }
        })
        .catch(err => {
            const {error} = err.response.data;

            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 3000);
        })
}

const generateOrdersHtml = (orders) => {
    let content = orders.map(order => {
        const {order_menus, order_date} = order;
        const date = new Date(order_date);

        let odr = `<div class="order__item">
                       <div class="order__detail">
                            <span class="order__id">Order ID: ${order.order_id}</span>
                            <span class="order__date">${date.toLocaleDateString()}</span>
                            <span class="order__status${order.order_status === 'ongoing' ? ' yellow' : ' green'}">${order.order_status}</span>
                            <span class="order__total_price">${"RM " + parseFloat(order.order_total_price).toFixed(2)}</span>
                       </div>
                       <div class="order__all_menus">`

        let menu = order_menus.map(menu => {
            return  `<div class="menu">
                        <div class="menu__pic">
                                <img src="${menu.item_pic}" alt="${menu.item_name}">
                        </div>
                        <div class="menu__name">
                            ${menu.item_name}
                        </div>
                    </div>`;
        }).join("");

        odr += menu;

        odr += `</div></div>`;

        return odr;
    }).join("");

    orderTable.innerHTML = content;
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())