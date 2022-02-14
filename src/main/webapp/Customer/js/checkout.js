const btnPayMethod = document.querySelectorAll(".payment_method__btn");
const btnPayAction = document.querySelector(".payment_action__btn");
const menuDetailHtml = document.querySelector(".menu__details");

window.addEventListener("DOMContentLoaded", () => {
    for(let btn of btnPayMethod) {
        btn.addEventListener("click", btnMethodHandler);
    }

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