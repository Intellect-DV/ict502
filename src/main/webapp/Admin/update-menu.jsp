<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 1/2/2022
  Time: 11:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Menu | Saliza Eja Cafe</title>
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

    <link rel="stylesheet" href="css/update-menu.css">

    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/update-menu.js" defer></script>
</head>
<body>
    <form id="form_update_menu" enctype="multipart/form-data" data-menu-id='${param.id}' data-menu-type="${param.type}">
        <div class="title">Update Menu</div>

        <div class="item">
            <label for="name">Name</label>
            <input type="text" id="name" name="name" autocomplete="off" placeholder="Name" required>
        </div>

        <div class="item">
            <label for="price">Price (RM)</label>
            <input type="number" id="price" name="price" min="0.00" max="1000.00" step="0.01" placeholder="Price" autocomplete="off" required>
        </div>

        <div class="item">
            <label for="description">Description</label>
            <input type="text" id="description" name="description" placeholder="Description" autocomplete="off" required>
        </div>

        <button type="submit">Update</button>
    </form>

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
