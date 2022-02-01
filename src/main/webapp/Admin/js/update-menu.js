const formUpdate = document.querySelector("#form_update_menu");
const menuId = formUpdate.dataset.menuId;
const menuType = formUpdate.dataset.menuType;
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
    if(menuId == null || menuId === "" || menuType == null || menuType === "") return;

    axios.get(`/menu?action=getmenuinfo&id=${menuId}&type=${menuType}`)
        .then(response => {
            const {content} = response.data;
            inputName.value = content.menuName;
            inputPrice.value = content.menuPrice;
            inputDescription.value = content.menuDescription;
        })
        .catch(err => {
            const {error} = err.response.data;
            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 3000);
        })
}

const updateMenuInfo = (event) => {
    event.preventDefault();
}

const closePopup = () => {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}