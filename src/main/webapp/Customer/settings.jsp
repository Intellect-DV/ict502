<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 11/2/2022
  Time: 6:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Settings | Saliza Eja Cafe</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <%-- Boilerplate - reset css style --%>
    <link rel="stylesheet" href="../css/boilerplate.css">
    <link rel="stylesheet" href="component/background.css">
    <link rel="stylesheet" href="component/navigation.css">
    <link rel="stylesheet" href="../component/modal__info.css">

    <link rel="stylesheet" href="css/settings.css">

    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/settings.js" defer></script>
    <script src="js/logout.js" defer></script>
</head>
<body>
    <header class="navigation">
        <%@include file="component/navigation.html"%>
    </header>

    <div class="container">
        <form class="form_profile" data-action="updateprofile">
            <div class="form_profile__title">Profile Settings</div>
            <div class="form_profile__item">
                <label for="username">Username: </label>
                <input type="text" id="username" name="username" placeholder="Username" autocomplete="off" required value="${sessionScope.customerObj.getCustomerUsername()}">
            </div>
            <div class="form_profile__item">
                <label for="name">Name: </label>
                <input type="text" id="name" name="name" placeholder="Name" autocomplete="off" required value="${sessionScope.customerObj.getCustomerName()}">
            </div>
            <div class="form_profile__item">
                <label for="email">Email: </label>
                <input type="email" id="email" name="email" placeholder="Email" autocomplete="off" required value="${sessionScope.customerObj.getCustomerEmail()}">
            </div>

            <button type="submit">Update</button>
            <span class="form_profile__link"><a href="change-password.jsp"> Change Password </a></span>
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
