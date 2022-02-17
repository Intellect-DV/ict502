const logoutBtn = document.querySelector("#logoutBtn");
let logoutTimeout;

window.addEventListener("DOMContentLoaded", () => {
    if(logoutBtn != null) {
        logoutBtn.addEventListener("click", event => {
            event.preventDefault();
            logoutHandler();
        })
    }

    document.addEventListener("mousemove", resetLogoutTimer);
    document.addEventListener("keydown", resetLogoutTimer);
    logoutTimeout = setTimeout(showLogoutAlert, 1000 * 60 * 20);
})

const logoutHandler = () => {
    axios.get("/worker?action=logout")
        .then(response => {
            const {message} = response.data;

            if(message === "success") {
                window.location.replace("/");
            }
        })
        .catch(err => {
            const {message} = err.response.data;
            console.log("err", err);
            alert(message);
        })
}

const resetLogoutTimer = () => {
    clearTimeout(logoutTimeout);
    logoutTimeout = setTimeout(showLogoutAlert, 1000 * 60 * 20);
}

const showLogoutAlert = () => {
    let mdlInf = document.querySelector(".modal__info");
    let mdlCrd = document.querySelector(".modal__card");
    let mdlCtnt = document.querySelector(".modal__content");

    mdlCtnt.innerText = "You will be logged out of inactivity!";
    mdlCrd.className = "modal__card alert";
    mdlInf.className = "modal__info active";
    setTimeout(() => {
        if(mdlInf.className === "modal__info active") {
            mdlInf.className = "modal__info";
        }
        logoutHandler();
    }, 1000 * 3);
}