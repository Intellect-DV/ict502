<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 30/1/2022
  Time: 12:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Change Password | Saliza Eja Cafe</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <%-- Boilerplate - reset css style --%>
    <link rel="stylesheet" href="../css/boilerplate.css">
    <link rel="stylesheet" href="component/background.css">
    <link rel="stylesheet" href="component/navigation.css">

    <link rel="stylesheet" href="css/settings.css">

    <link rel="stylesheet" href="../component/modal__info.css">
    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/settings.js" defer></script>
</head>
<body>
    <form class="form_profile" data-action="updatepassword">
        <div class="form_profile__title">Profile Settings</div>
        <div class="form_profile__item">
            <label for="current-password">Current Password: </label>
            <input type="password" id="current-password" name="current-password" placeholder="Current Password" autocomplete="off" required>
        </div>
        <div class="form_profile__item">
            <label for="new-password">New Password: </label>
            <input type="password" id="new-password" name="new-password" placeholder="Enter Password" autocomplete="off" required>
        </div>
        <div class="form_profile__item">
            <label for="confirm-password">Confirm Password: </label>
            <input type="password" id="confirm-password" name="confirm-password" placeholder="Re-enter Password" autocomplete="off" required>
        </div>

        <button type="submit">Change Password</button>
        <span class="form_profile__link"><a href="settings.jsp"> Go Back </a></span>
    </form>

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
