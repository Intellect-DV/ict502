const filterBtns = document.querySelectorAll(".filter__box > button");
const menuList = document.querySelector("div.menu_list");
let menusStored = [];

const modalInfo = document.querySelector(".modal__info");
const modalCard= document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    getAllMenus();
    for(let btn of filterBtns) {
        btn.addEventListener("click", filterBtnHandler);
    }
})

const filterBtnHandler = (event) => {
    const currentBtn = event.target;
    const {menuType} = currentBtn.dataset;
    for(let temp of filterBtns) {
        temp.removeAttribute("class");
    }
    currentBtn.className = "active";

    if(menusStored.length === 0) return;

    switch (menuType) {
        case "all":
            filterMenusAll();
            break;
        case "maincourse":
            filterMenusMainCourse();
            break;
        case "beverage":
            filterMenusBeverage();
            break;
        case "dessert":
            filterMenusDessert();
            break;
    }
}

const filterMenusAll = () => {
    generateHTMLMenus(menusStored);
}

const filterMenusMainCourse = () => {
    generateHTMLMenus(menusStored.filter(menu => menu.itemType === "maincourse"));
}

const filterMenusBeverage = () => {
    generateHTMLMenus(menusStored.filter(menu => menu.itemType === "beverage"));
}

const filterMenusDessert = () => {
    generateHTMLMenus(menusStored.filter(menu => menu.itemType === "dessert"));
}

const generateHTMLMenus = (data) => {
    menuList.innerHTML = data.map(menu => {
        return `<div class="menu_list__item">
                    <div class="menu_list__image">
                        <img src="${menu.itemPicUrl}" alt="${menu.itemName}">
                    </div>
                    <div class="menu_list__name">
                        ${menu.itemName}
                    </div>
                    <div class="menu_list__price">
                        ${menu.itemPriceToCurrency}
                    </div>
                    <div class="menu_list__desc">
                        ${menu.itemDescription}
                    </div>
                    <div class="menu_list__hoverbox">
                        <button data-menu-id="${menu.itemId}" onclick="addToCart(event)">Add to Cart</button>
                    </div>
                </div>`
    }).join("");
}

const getAllMenus = () => {
    const url = "/menu?action=getmenus";

    axios.get(url)
        .then(res => {
            const {total, menus} = res.data;

            if(total > 0) {
                menusStored = menus;
                filterMenusAll();
            }
        })
        .catch(err => {
            console.log(err.response.data);
        })
}

const addToCart = (event) => {
    const {menuId} = event.target.dataset;
    const url = "/cart";
    const params = new URLSearchParams();

    params.append("action", "add");
    params.append("menuId", menuId);

    axios.post(url, params)
        .then(res => {
            const {message} = res.data;

            modalCard.className = "modal__card success";
            modalInfo.className = "modal__info active";

            if(message === "Added to Cart") {
                modalContent.innerText = "Menu has been added!";
            } else {
                modalContent.innerText = message;
            }

            setTimeout(closePopup, 1000);
        })
        .catch(err => {
            const {error} = err.response.data;

            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            modalContent.innerText = error;
            setTimeout(closePopup, 1000);
        })
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())