const btnPayMethod = document.querySelectorAll(".payment_method__btn");
const btnPayAction = document.querySelector(".payment_action__btn");

window.addEventListener("DOMContentLoaded", () => {
    for(let btn of btnPayMethod) {
        btn.addEventListener("click", btnMethodHandler);
    }
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