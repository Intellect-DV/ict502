const logoutBtn = document.querySelector("#logoutBtn");


window.addEventListener("DOMContentLoaded", () => {
    logoutBtn.addEventListener("click", event => {
        event.preventDefault();
        logoutHandler();
    })

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
    modalContent.innerText = "You will be logged out of inactivity!";
    modalCard.className = "modal__card alert";
    modalInfo.className = "modal__info active";
    setTimeout(() => {
        if(modalInfo.className === "modal__info active") {
            modalInfo.className = "modal__info";
        }
        logoutHandler();
    }, 1000 * 3);
}