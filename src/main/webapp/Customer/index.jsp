<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 29/1/2022
  Time: 9:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Saliza Eja Cafe</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <%-- Boilerplate - reset css style --%>
    <link rel="stylesheet" href="../css/boilerplate.css">
    <link rel="stylesheet" href="component/background.css">
    <link rel="stylesheet" href="component/navigation.css">

    <link rel="stylesheet" href="css/index.css">

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/logout.js" defer></script>
</head>
<body>
    <header class="navigation">
        <nav class="navbar">
            <div class="navbar__brand">
                <a class="title" href="/Customer/">SEFOS</a>
            </div>
            <div class="navbar__list">
                <div class="navbar__item">
                    <a href="#">Home</a>
                </div>
                <div class="navbar__item">
                    <a href="#">Cart</a>
                </div>
                <div class="navbar__item">
                    Profile
                    <div class="dropdown_menu">
                        <div class="dropdown__inner">
                            <ul class="dropdown__list">
                                <li>
                                    <a href="#">Settings</a>
                                </li>
                                <li>
                                    <a href="#" id="logoutBtn">Logout</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    </header>
</body>
</html>
