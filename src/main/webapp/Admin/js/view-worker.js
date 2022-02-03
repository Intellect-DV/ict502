const tableBody = document.querySelector("#tbody");

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
                setTimeout(closePopup, 3000);
                getWorker();
            })
            .catch(err => {
                const {error} = err.response.data;

                modalContent.innerText = error;
                modalCard.className = "modal__card failed";
                modalInfo.className = "modal__info active";
                setTimeout(closePopup, 3000);
            })
    })

    getWorker();
})
function getWorker() {
    axios.get("/worker?action=retrieveworker")
        .then(response => {
            // root.innerHTML = response.data;
            let content;
            const {workers, total} = response.data;
            if(total === 0) {
                content =
                `<tr class="table__row">
                    <td colspan="5" style="text-align: center;">None of worker registered below you.</td>
                </tr>`
            } else {
                 content = workers.map(worker => {
                     return `<tr class="table__row">
                                <td>${worker.workerId}</td>
                                <td>${worker.workerUsername}</td>
                                <td>${worker.workerName}</td>
                                <td>${worker.workerEmail}</td>
                                <td>
                                    <button data-worker-id="${worker.workerId}" onclick="deleteWorker(event)">
                                        Delete
                                    </button>
                                </td>
                            </tr>`;
                }).join("");
            }
            tableBody.innerHTML = content;
        })
        .catch(err => {
            const {error} = err.response.data;

            modalContent.innerText = error;
            modalCard.className = "modal__card failed";
            modalInfo.className = "modal__info active";
            setTimeout(closePopup, 3000);
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
