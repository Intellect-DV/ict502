<nav class="navbar">
    <div class="navbar__user">
        ${sessionScope.workerObj.getWorkerUsername()}
    </div>
    <div class="navbar__list">
        <a href="./" class="navigation__item">
            <i class="fas fa-th-large"></i> General
        </a>
        <a href="./view-order.jsp" class="navigation__item">
            <i class="fas fa-utensils"></i> Order
        </a>
        <a href="./view-menu.jsp" class="navigation__item">
            <i class="fas fa-clipboard-list"></i> Menu
        </a>
        <a href="./settings.jsp" class="navigation__item line">
            <i class="fas fa-user-cog"></i> Settings
        </a>
        <a href="#" id="logoutBtn" class="navigation__item">
            <i class="fas fa-sign-out-alt"></i> Logout
        </a>
    </div>
</nav>