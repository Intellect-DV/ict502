<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 11/2/2022
  Time: 6:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Cart | Saliza Eja Cafe</title>
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
    <link rel="stylesheet" href="../component/modal__confirmation.css">

    <link rel="stylesheet" href="css/view-cart.css">

    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" integrity="sha512-9usAa10IRO0HhonpyAIVpjrylPvoDwiPUiKdWk5t3PyolY1cOd4DSE0Ga+ri4AuTroPR5aQvXU9xC6qOPnzFeg==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/view-cart.js" defer></script>
    <script src="js/logout.js" defer></script>
</head>
<body>
    <header class="navigation">
        <%@include file="component/navigation.html"%>
    </header>

    <div class="container">
        <table class="cartbox">
            <thead>
                <tr>
                    <th>No</th>
                    <th>Menu</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total Price</th>
                </tr>
            </thead>
            <tbody id="tbody">

            </tbody>
        </table>
    </div>

    <div class="modal__backdrop hide">
        <div class="modal-confirm">
            <div class="header red">
                Delete Item
            </div>
            <div class="content">
                Are you sure?
            </div>
            <div class="action">
                <button class="btn-confirm red">
                    Yes
                </button>
                <button class="btn-confirm grey">
                    No
                </button>
            </div>
        </div>
    </div>

    <div class="modal__info">
        <div class="modal__card failed">
            <span class="modal__content">

            </span>
            <span class="modal__close">
                <i class="fas fa-times"></i>
            </span>
        </div>
    </div>
</body>
</html>
