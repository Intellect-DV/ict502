const navItem = document.querySelector(".navigation__item[href='./']");
const dashboard = document.querySelector("div.dashboard");

window.addEventListener("DOMContentLoaded", () => {
    navItem.classList.add("active");

    sendRequest();
})

const sendRequest = () => {
    Promise.all([getOrderSummary(), getMenuCount(), getWorkerCount(), getCustomerCount()])
        .then( results => {
            console.log(results);
            const {order_ongoing, order_complete, total_sales} = results[0].data;
            const {menu_count} = results[1].data;
            const {worker_count} = results[2].data;
            const {customer_count} = results[3].data;

            let content =   `<div class="item">
                                <div class="title">Total Sales</div>
                                <div class="content">${"RM " + parseFloat(total_sales).toFixed(2)}</div>
                            </div>
                            <div class="item">
                                <div class="title">Ongoing Orders</div>
                                <div class="content">${order_ongoing}</div>
                            </div>
                            <div class="item">
                                <div class="title">Complete Orders</div>
                                <div class="content">${order_complete}</div>
                            </div>
                            <div class="item">
                                <div class="title">Total Menu</div>
                                <div class="content">${menu_count}</div>
                            </div>
                            <div class="item">
                                <div class="title">Total Worker</div>
                                <div class="content">${worker_count}</div>
                            </div>
                            <div class="item">
                                <div class="title">Total Customer</div>
                                <div class="content">${customer_count}</div>
                            </div>`;

            dashboard.innerHTML = content;
        });
}

const getOrderSummary = () => {
    return axios.get("/order?action=getordersummary");
}

const getMenuCount = () => {
    return axios.get("/menu?action=getmenucount");
}

const getWorkerCount = () => {
    return axios.get("/worker?action=getworkercount");
}

const getCustomerCount = () => {
    return axios.get("/customer?action=getcustomercount");
}