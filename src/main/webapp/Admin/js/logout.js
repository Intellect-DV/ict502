const logoutBtn = document.querySelector("#logoutBtn");

window.addEventListener("DOMContentLoaded", () => {
    logoutBtn.addEventListener("click", event => {
        event.preventDefault();

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
    })
})