const navItem = document.querySelector(".navigation__item[href='./view-order.jsp']");
const filterItems = document.querySelectorAll(".filter__item > button");

window.addEventListener("DOMContentLoaded", () => {
    navItem.classList.add("active");

    for(let filterItem of filterItems) {
        filterItem.addEventListener("click", (event) => {
            filterHandler(event);
        })
    }
})

const filterHandler = (event) => {
    const currentBtn = event.target;

    for(let filterItem of filterItems) {
        filterItem.className = "";
    }

    currentBtn.classList.add("active");
}