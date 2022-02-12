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
                <tr>
                    <td>1</td>
                    <td>
                        <div class="cart__flex">
                            <div class="cart__menu_img">
                                <img src="http://localhost:85/upload/sirap.jpg" alt="Sirap">
                            </div>
                            <div class="cart__menu_detail">
                                Sirap
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="cart__price">
                            RM 2.00
                        </div>
                    </td>
                    <td>
                        <div class="cart__quantity">
                            <button class="btn_quantity minus">
                                <i class="fa-solid fa-minus"></i>
                            </button>
                            <span class="quantity">
                                4
                            </span>
                            <button class="btn_quantity plus">
                                <i class="fa-solid fa-plus"></i>
                            </button>
                            <a href="#" class="btn_remove_cart">
                                <i class="fa-solid fa-trash-can"></i>
                            </a>
                        </div>
                    </td>
                    <td>
                        <div class="cart__total_price">
                            RM 8.00
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="cart__grandtotal_display">
                        <div>
                            Total Amount
                        </div>
                    </td>
                    <td class="cart__grandtotal">
                        <span>
                            RM 18.00
                        </span>
                    </td>
                </tr>
            </tbody>
        </table>
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
