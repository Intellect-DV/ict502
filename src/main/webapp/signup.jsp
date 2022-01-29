<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 29/1/2022
  Time: 12:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up | Saliza Eja Cafe</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <%-- Boilerplate - reset css style --%>
    <link rel="stylesheet" href="css/boilerplate.css">
    <link rel="stylesheet" href="css/signup.css">
    <link rel="stylesheet" href="component/modal__info.css">

    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/signup.js" defer></script>
</head>
<body>
    <form class="form-signup">
        <div class="title">
            Sign Up
        </div>
        <div class="input-box">
            <input type="text" name="username" placeholder="Username" autocomplete="off" required>
        </div>
        <div class="input-box">
            <input type="text" name="name" placeholder="Name" autocomplete="off" required>
        </div>
        <div class="input-box">
            <input type="email" name="email" placeholder="Email" autocomplete="off" required>
        </div>
        <div class="input-box">
            <input type="password" name="password" placeholder="Password" autocomplete="off" required>
        </div>
        <div class="input-box">
            <input type="password" name="password-confirm" placeholder="Re-enter Password" autocomplete="off" required>
        </div>
        <div class="action-box">
            <button type="submit">Submit</button>
        </div>
    </form>

    <div class="modal__info">
        <div class="modal__card failed">
            <span class="modal__content">
                Register failed!
            </span>
            <span class="modal__close">
                <i class="fas fa-times"></i>
            </span>
        </div>
    </div>
</body>
</html>
