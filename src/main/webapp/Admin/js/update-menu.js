const formUpdate = document.querySelector("#form_update_menu");
const {menuId, menuType} = formUpdate.dataset;
const inputName = document.querySelector("input[name='name']");
const inputPrice = document.querySelector("input[name='price']")
const inputDescription = document.querySelector("input[name='description']");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

window.addEventListener("DOMContentLoaded", () => {
    modalClose.addEventListener("click", () => closePopup());
    formUpdate.addEventListener("submit", (event) => updateMenuInfo(event));

    getMenuInfo();
})

const getMenuInfo = () => {
    if(menuId == null || menuId === "" || menuType == null || menuType === "") {
        modalContent.innerText = "URL parameter does not have id or/and type of menu";
        modalCard.className = "modal__card failed";
        modalInfo.className = "modal__info active";
        setTimeout(closePopup, 3000);
        return;
    }

    axios.get(`/menu?action=getMenuInfo&id=${menuId}&type=${menuType}`)
        .then(response => {
            const {content} = response.data;
            inputName.value = content.menuName;
            inputPrice.value = content.menuPrice;
            inputDescription.value = content.menuDescription;
        })
        .catch(err => {
            const {error} = err.response.data;
            modalContent.innerText = error;
            modalCard.className = "modal__card alert";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 3000);
        })
}

const updateMenuInfo = (event) => {
    event.preventDefault();

    if(menuId == null || menuId === "" || menuType == null || menuType === "") {
        modalContent.innerText = "URL parameter does not have id or/and type of menu";
        modalCard.className = "modal__card alert";
        modalInfo.className = "modal__info active";
        setTimeout(closePopup, 3000);
        return;
    }

    const url = "/menu";
    const formData = new FormData(formUpdate);
    const params = new URLSearchParams();

    params.append("action","updateMenuInfo");
    params.append("id", menuId);
    params.append("menu-type", menuType);
    for(let key of formData.keys()) {
        params.append(key, String(formData.get(key)));
    }

    axios.post(url, params)
        .then(response => {
            const {message} = response.data;
            modalContent.innerText = message;
            modalCard.className = "modal__card success";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 1600);
            setTimeout(()=> {
                window.location.href = "edit-menu.jsp";
            },1700);
        })
        .catch(err => {
            const {error} = err.response.data;
            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 3000);
        })
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}