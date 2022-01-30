<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <tbody> <c:forEach var="worker" items="${requestScope.workers}">
        <tr class="table__row">
            <td>${worker.getWorkerId()}</td>
            <td>${worker.getWorkerUsername()}</td>
            <td>${worker.getWorkerName()}</td>
            <td>${worker.getWorkerEmail()}</td>
            <td>
                <button data-worker-id="${worker.getWorkerId()}" onclick="deleteWorker(event)">
                    Delete
                </button>
            </td>
        </tr> </c:forEach> <c:if test="${requestScope.workers.isEmpty()}">
        <tr class="table__row">
            <td colspan="5" style="text-align: center;">None of worker registered below you.</td>
        </tr> </c:if>
    </tbody>
</table>