<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page import="com.my.db.entities.Role" %>

<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Результат поиска"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>VIEW SEARCH CASHIER RESULT</h2>
        <form action="controller">
            <input type="hidden" name="command" value="viewSearchCashierResult"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="Enter cashiers first name" name="cashier_first_name" required>
            <input type="text" placeholder="Enter cashiers last name" name="cashier_last_name" required>
            <button type="submit" class="add_product_btn">Search</button>
        </form>
        <hr>

        <c:if test="${empty requestScope.searchCashiersResult}">
            <h2>Cannot find any cashier by your request</h2>
        </c:if>
        <c:if test="${not empty requestScope.searchCashiersResult}">
            <table id="search_result_table">
                <tr>
                    <td>id</td>
                    <td>first name</td>
                    <td>last name</td>
                    <td>role</td>
                </tr>
                <c:forEach items="${requestScope.searchCashiersResult}" var="user">
                    <tr>
                        <td><c:out value="${user.id}"/></td>
                        <td><c:out value="${user.firstName}"/></td>
                        <td><c:out value="${user.lastName}"/></td>
                        <td><c:out value="${Role.getRole(user)}"/></td>
                        <c:if test="${sessionScope.userRole.name().toLowerCase() == 'senior_cashier'}">
                            <td>
                                <form action="generateCashierReport" method="post">
                                    <input type="hidden" name="id" value="${user.id}">
                                    <button type="submit" class="cashier_report_btn">Choose</button>
                                </form>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>

            </table>
            <hr>
            <c:if test="${requestScope.nOfPages>1}">
                <nav aria-label="Navigation for products">
                    <ul class="pagination">
                        <c:if test="${requestScope.currentPage != 1}">
                            <li class="page_item">
                                <a class="page_link"
                                   href="controller?command=viewSearchCashierResult&cashier_first_name=${sessionScope.lastSearchCashierFName}&cashier_last_name=${sessionScope.lastSearchCashierLName}&currentPage=${requestScope.currentPage-1}">Previous</a>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${requestScope.nOfPages}" var="i">
                            <c:choose>
                                <c:when test="${requestScope.currentPage eq i}">
                                    <li class="page_item_active"><a class="page_link">
                                            ${i} <span class="sr_only">(Current)</span>
                                    </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page_item">
                                        <a class="page_link"
                                           href="controller?command=viewSearchCashierResult&cashier_first_name=${sessionScope.lastSearchCashierFName}&cashier_last_name=${sessionScope.lastSearchCashierLName}&currentPage=${i}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${requestScope.currentPage lt requestScope.nOfPages}">
                            <li class="page_item">
                                <a class="page_link"
                                   href="controller?command=viewSearchCashierResult&cashier_first_name=${sessionScope.lastSearchCashierFName}&cashier_last_name=${sessionScope.lastSearchCashierLName}&currentPage=${requestScope.currentPage+1}">Next</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>

        </c:if>


    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>