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
    <link rel="stylesheet" href="css/sign-in.css">
    <link rel="stylesheet" href="component/modal__info.css">

    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/sign-in.js" defer></script>
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
        <form data-action="customer" id="form-signin">
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

    <div class="modal__info">
        <div class="modal__card">
            <span class="modal__content">

            </span>
            <span class="modal__close">
                <i class="fas fa-times"></i>
            </span>
        </div>
    </div>
</body>
</html>
