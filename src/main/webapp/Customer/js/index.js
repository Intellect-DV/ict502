const filterBtns = document.querySelectorAll(".filter__box > button");
const menuList = document.querySelector("div.menu_list");
let menusStored = [];

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
                        <button data-menu-id="${menu.itemId}">Add to Cart</button>
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