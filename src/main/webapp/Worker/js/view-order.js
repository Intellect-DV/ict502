const navItem = document.querySelector(".navigation__item[href='./view-order.jsp']");
const filterItems = document.querySelectorAll(".filter__item > button");
const orderContent = document.querySelector(".orders__content");
let storedOrders = {};
let currentOrder = {};

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");


window.addEventListener("DOMContentLoaded", () => {
    navItem.classList.add("active");

    for(let filterItem of filterItems) {
        filterItem.addEventListener("click", (event) => {
            filterHandler(event);
        })
    }

    retrieveOrders();
})

const filterHandler = (event) => {
    const currentBtn = event.target;
    const {orderStatus} = currentBtn.dataset;

    for(let filterItem of filterItems) {
        filterItem.className = "";
    }

    currentBtn.classList.add("active");

    if(orderStatus === "ongoing") {
        filterOngoingOrders();
    } else {
        filterCompleteOrders();
    }
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())

const retrieveOrders = () => {
    const url = "/order?action=retrieveorderforworker";

    axios.get(url)
        .then(res => {
            const {message, orders} = res.data;

            if(!(message === undefined)) {
                modalContent.innerText = message;

                modalCard.className = "modal__card alert";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            } else {
                storedOrders = orders;
                filterOngoingOrders();
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

const filterOngoingOrders = () => {
    currentOrder = storedOrders.filter(order => order.order_status === "ongoing");
    generateHtml();
}

const filterCompleteOrders = () => {
    currentOrder = storedOrders.filter(order => order.order_status === "complete");
    generateHtml();
}

const generateHtml = () => {
    orderContent.innerHTML = currentOrder.map(order => {
        const {order_menus, order_date} = order;
        const date = new Date(order_date);

        let output = `<div class="order__item">
                        <div class="order__cust_details">
                            <div class="cust_username">
                                ${order.cust_username}
                            </div>
                            <div class="cust_email">
                                ${order.cust_email}
                            </div>
                        </div>
                        <div class="order__order_details">
                            <div class="order_id">Order Id: ${order.order_id}</div>
                            <div class="order_date">${date.toLocaleDateString()}</div>
                            <div class="order_total_price">${"RM " + parseFloat(order.order_total_price).toFixed(2)}</div>
                            <div class="order_status${order.order_status === 'ongoing' ? ' orange' : ' green'}">${order.order_status}</div>
                        </div>
                        <div class="order__menu_details">`;

        output += order_menus.map(menu => {
            return `<div class="menu_item">
                        <div class="menu_name">${menu.item_name}</div>
                        <div class="menu_quantity">${menu.item_quantity}</div>
                    </div>`;
        }).join("");

        output += `</div>`;

        if (order.order_status === 'ongoing') {
            output += `<div class="order__action">
                            <button class="btn_set" data-order-id="${order.order_id}" onclick="changeStatus(event)">
                                Set Status to Complete
                            </button>
                        </div>`;
        }

        output += `</div>`;

        return output;
    }).join("");
}

const changeStatus = (event) => {
    const currentBtn = event.target;
    const {orderId} = currentBtn.dataset;
    const url = "/order";
    const params = new URLSearchParams({
        "action": "changestatus",
        "orderId": orderId,
        "orderStatus": "complete"
    })

    axios.post(url,params)
        .then(res => {
            const {message} = res.data;

            if(message === "Order status updated") {
                modalContent.innerText = message;

                modalCard.className = "modal__card success";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);

                retrieveOrders();
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