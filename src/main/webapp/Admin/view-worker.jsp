<%--
  Created by IntelliJ IDEA.
  User: Aiman
  Date: 30/1/2022
  Time: 4:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Worker | Saliza Eja Cafe</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@800&family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">
    <%-- Boilerplate - reset css style --%>
    <link rel="stylesheet" href="../css/boilerplate.css">
    <link rel="stylesheet" href="component/background.css">
    <link rel="stylesheet" href="../component/modal__info.css">
    <link rel="stylesheet" href="../component/modal__confirmation.css">

    <link rel="stylesheet" href="css/view-worker.css">

    <%-- Font Awesome --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" integrity="sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />

    <%-- Axios - to make Http request --%>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="js/view-worker.js" defer></script>
    <script src="js/logout.js" defer></script>
</head>
<body>
    <div id="root">
        <table class="table">
            <thead>
            <tr class="table__row">
                <th>Id</th>
                <th>Username</th>
                <th>Name</th>
                <th>Email</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody id="tbody">
            </tbody>
        </table>
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
    <div class="modal__backdrop hide">
        <div class="modal-confirm">
            <div class="header red">
                Delete Worker
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
</body>
</html>
