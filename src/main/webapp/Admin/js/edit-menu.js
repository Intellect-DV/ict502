const filterBtn = document.querySelectorAll(".filter__item > button");
const menuDiv = document.querySelector("div.menu");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    for(let i = 0; i <  filterBtn.length; i++) {
        filterBtn[i].addEventListener("click", event => {
            filterBtn[0].removeAttribute("class");
            filterBtn[1].removeAttribute("class");
            filterBtn[2].removeAttribute("class");

            filterBtn[i].classList = "active";
            switch (filterBtn[i].dataset.menuType) {
                case "maincourse":
                    getMainCourseMenu();
                    break;
                case "beverage":
                    getBeverageMenu();
                    break;
                case "dessert":
                    getDessertMenu();
                    break;
            }
        })
    }

    getMainCourseMenu();
})

const getMainCourseMenu = () => {
    axios.get("/menu?action=getmenus&type=maincourse")
        .then(response => {
            generateMenuHTML(response.data);
        })
        .catch(err => {
            menuDiv.innerHTML = "";
            const {error} = err.response.data;
            if(error === "None of menu type") {
                showAlertPopup(error)
            }
        })
}

const getBeverageMenu = () => {
    axios.get("/menu?action=getmenus&type=beverage")
        .then(response => {
            generateMenuHTML(response.data);
        })
        .catch(err => {
            menuDiv.innerHTML = "";
            const {error} = err.response.data;
            if(error === "None of menu type") {
                showAlertPopup(error)
            }
        })
}

const getDessertMenu = () => {
    axios.get("/menu?action=getmenus&type=dessert")
        .then(response => {
            generateMenuHTML(response.data);
        })
        .catch(err => {
            menuDiv.innerHTML = "";
            const {error} = err.response.data;
            if(error === "None of menu type") {
                showAlertPopup(error)
            }
        })
}

const showAlertPopup  = (error) => {
    modalContent.innerText = error;
    modalCard.className = "modal__card alert";
    modalInfo.className = "modal__info active";
    setTimeout(closePopup, 3000);
}

const generateMenuHTML = (data) => {
    let content = data.map(menu => {
        return `<div class="menu__item">
                    <div class="menu__image">
                         <img src="${menu.itemPicUrl}" alt="${menu.itemName}" />
                    </div>
                    <div class="menu__title">${menu.itemType}</div>
                    <div class="menu__price">${menu.itemPriceToCurrency}</div>
                    <div class="menu__desc">${menu.itemDescription}</div>
                    <div class="menu__action">
                        <button onclick='window.location.href="update-menu.jsp?id=${menu.itemId}"'>Update</button>
                        <button data-menu-id="${menu.itemId}" data-menu-type="${menu.itemType}" onclick="triggerPopup(event)">Delete</button>
                    </div>
                </div>`;
    })

    menuDiv.innerHTML = content;
}

function closePopup() {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => {
    closePopup();
})