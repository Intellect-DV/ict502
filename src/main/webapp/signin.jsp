<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 29/1/2022
  Time: 1:45 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign In | Saliza Eja Cafe</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <%-- Boilerplate - reset css style --%>
    <link rel="stylesheet" href="css/boilerplate.css">
    <link rel="stylesheet" href="css/signin.css">
</head>
<body>
    <div class="container">
        <div class="toggle">
            <span>Customer</span>
            <div class="toggle__box">
                <input type="checkbox" id="toggle__ch" class="toggle__ch">
                <label class="toggle__btn" for="toggle__ch"></label>
            </div>
            <span>Worker</span>
        </div>
        <form action="" id="form-signin">
            <div class="title">Sign In</div>
            <div class="inputbox">
                <input type="text" name="username" placeholder="Username" autocomplete="off" required>
            </div>
            <div class="inputbox">
                <input type="password" name="password" placeholder="Password" autocomplete="off" required>
            </div>
            <div class="btn-box">
                <button type="submit">Login</button>
            </div>
        </form>
    </div>

    <script type="text/javascript">
        const form = document.querySelector("#form-signin");
        const toggleBtn = document.querySelector("#toggle__ch");

        window.addEventListener("DOMContentLoaded", () => {
            toggleBtn.addEventListener("change", event => {
                if(toggleBtn.checked) {
                    form.action = "/worker/"; // todo - worker servlet
                } else {
                    form.action = "/customer/"; // todo - customer servlet
                }
            })
        })
    </script>
</body>
</html>
