const navItem = document.querySelector(".navigation__item[href='./view-order.jsp']");
const filterItems = document.querySelectorAll(".filter__item > button");
const orderContent = document.querySelector(".orders__content");
let storedOrders = {};
let currentOrder = {};

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

const retrieveOrders = () => {
    const url = "/order?action=retrieveorderforworker";

    axios.get(url)
        .then(res => {
            const {message, orders} = res.data;

            console.log(message, orders);
            if(!(message === undefined)) {

            } else {
                storedOrders = orders;
                filterOngoingOrders();
            }
        })
        .catch(err => {
            // todo - prooper message
            console.log(err);
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
                            <button class="btn_set" data-order-id="${order.order_id}" onclick="changeStatus()">
                                Set Status to Complete
                            </button>
                        </div>`;
        }

        output += `</div>`;

        return output;
    }).join("");
}

const changeStatus = () => {
    // todo - make http request
}