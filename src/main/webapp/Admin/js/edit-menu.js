const filterBtn = document.querySelectorAll(".filter__item > button");
const menuDiv = document.querySelector("div.menu");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const modalBackdrop = document.querySelector(".modal__backdrop");
const modalBtnYes = document.querySelector(".action > .btn-confirm.red");
const modalBtnNo = document.querySelector(".action > .btn-confirm.grey");

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
                    <div class="menu__title">${menu.itemName}</div>
                    <div class="menu__price">${menu.itemPriceToCurrency}</div>
                    <div class="menu__desc">${menu.itemDescription}</div>
                    <div class="menu__action">
                        <button onclick='window.location.href="update-menu.jsp?id=${menu.itemId}&type=${menu.itemType}"'>Update</button>
                        <button data-menu-id="${menu.itemId}" data-menu-type="${menu.itemType}" data-menu-parent-id="${menu.parentId || -1}" onclick="triggerConfirmPopup(event)">Delete</button>
                    </div>
                </div>`;
    }).join("");

    menuDiv.innerHTML = content;
}

const triggerConfirmPopup = (event) => {
    const {menuId, menuType, menuParentId} = event.target.dataset;

    modalBtnYes.dataset.menuId = menuId;
    modalBtnYes.dataset.menuType = menuType;
    modalBtnYes.dataset.menuParentId = menuParentId;
    modalBackdrop.className = "modal__backdrop";
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => closePopup())

modalBtnNo.addEventListener("click", () => modalBackdrop.className = "modal__backdrop hide")

modalBtnYes.addEventListener("click", () => {
    // delete http request
    const {menuId,menuType} = modalBtnYes.dataset;
    const url = "/menu";
    const params = new URLSearchParams();

    params.append("action","deletemenu")
    params.append("id", menuId);
    params.append("type",menuType);
    if(!(modalBtnYes.dataset.menuParentId === "-1")) {
        params.append("parentId",modalBtnYes.dataset.menuParentId);
    }

    axios.post(url,params)
        .then(response => {
            const {message} = response.data;
            if (message === "The menu has been successfully deleted!") {
                switch (menuType.toLowerCase()) {
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
                modalContent.innerText = message;
                modalCard.className = "modal__card success";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            }
            modalBackdrop.className = "modal__backdrop hide";
        })
        .catch (err => {
            const {error} = err.response.data;
            modalBackdrop.className = "modal__backdrop hide";
            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 3000);
        })
})