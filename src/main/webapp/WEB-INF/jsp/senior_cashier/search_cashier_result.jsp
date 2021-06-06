<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page import="com.my.db.entities.Role" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Результат поиска"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="search.cashier.result.title"/></h2>
        <form action="controller" name="searchCashiers" onsubmit="return(validate())">
            <input type="hidden" name="command" value="viewSearchCashierResult"/>
            <input type="hidden" name="currentPage" value="1"/>
            <label>
                <input type="text" placeholder="<fmt:message key="search.cashier.placeholder.fname"/>"
                       name="cashier_first_name" required/>
            </label>
            <label>
                <input type="text" placeholder="<fmt:message key="search.cashier.placeholder.lname"/>"
                       name="cashier_last_name" required/>
            </label>
            <button type="submit" class="add_product_btn"><fmt:message key="search_jsp.search.button"/></button>
        </form>
        <hr>

        <c:if test="${empty requestScope.searchCashiersResult}">
            <h2><fmt:message key="search.cashier.result.not.found"/></h2>
        </c:if>
        <c:if test="${not empty requestScope.searchCashiersResult}">
            <table id="search_result_table">
                <tr>
                    <td>ID</td>
                    <td><fmt:message key="cashier.fname.column"/></td>
                    <td><fmt:message key="cashier.lname.column"/></td>
                    <td><fmt:message key="cashier.role.column"/></td>
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
                                    <input type="hidden" name="id" value="${user.id}"/>
                                    <button type="submit" class="cashier_report_btn"><fmt:message
                                            key="cashier.report.generate.button"/></button>
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
                                   href="controller?command=viewSearchCashierResult&cashier_first_name=${sessionScope.lastSearchCashierFName}&cashier_last_name=${sessionScope.lastSearchCashierLName}&currentPage=${requestScope.currentPage-1}"><fmt:message
                                        key="pagination.previous"/></a>
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
                                   href="controller?command=viewSearchCashierResult&cashier_first_name=${sessionScope.lastSearchCashierFName}&cashier_last_name=${sessionScope.lastSearchCashierLName}&currentPage=${requestScope.currentPage+1}"><fmt:message
                                        key="pagination.next"/></a>
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

<script type="text/javascript">
    function validate() {
        if (document.searchCashiers.cashier_first_name.value.length > 45) {
            alertify.alert("<fmt:message key="cashier.fname.column"/>", "<fmt:message key="add.product.invalid.length"/>")
            return false;
        }
        if (document.searchCashiers.cashier_last_name.value.length > 45) {
            alertify.alert("<fmt:message key="cashier.lname.column"/>", "<fmt:message key="add.product.invalid.length"/>")
            return false;
        }
        return true;
    }
</script>