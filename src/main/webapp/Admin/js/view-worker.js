const root = document.querySelector("#root");

const modalInfo = document.querySelector(".modal__info");
const modalCard = document.querySelector(".modal__card");
const modalContent = document.querySelector(".modal__content");
const modalClose = document.querySelector(".modal__close");

const modalBackdrop = document.querySelector(".modal__backdrop");
const modalBtnYes = document.querySelector(".action > .btn-confirm.red");
const modalBtnNo = document.querySelector(".action > .btn-confirm.grey");

window.addEventListener("DOMContentLoaded", () => {
    modalBtnNo.addEventListener("click", () => {
        modalBackdrop.className = "modal__backdrop hide";
    })

    modalBtnYes.addEventListener("click", () => {
        modalBackdrop.className = "modal__backdrop hide";

        const url = "/worker";
        const params = new URLSearchParams({workerId: modalBtnYes.dataset.workerId});
        params.append("action", "delete")

        axios.post(url, params)
            .then(response => {
                const {message} = response.data;

                modalContent.innerText = message;
                modalCard.className = "modal__card success";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 5000);
                getWorker();
            })
            .catch(err => {
                const {error} = err.response.data;

                modalContent.innerText = error;
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 5000);
            })
    })

    getWorker();
})

function getWorker() {
    axios.get("/worker?action=retrieveworker")
        .then(response => {
            root.innerHTML = response.data;
        })
        .catch(err => {
            const {error} = err.response.data;

            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 5000);
        })
}

function deleteWorker(event) {
    const {workerId} = event.target.dataset;

    modalBtnYes.dataset.workerId = workerId;
    modalBackdrop.className = "modal__backdrop";
}

function closePopup() {
    if(modalInfo.className === "modal__info active") {
        modalInfo.className = "modal__info";
    }
}

modalClose.addEventListener("click", () => {
    closePopup();
})
