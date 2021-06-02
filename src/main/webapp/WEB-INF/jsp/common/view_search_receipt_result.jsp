<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<c:set var="title" value="Все товары"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="view_search_receipt_result_jsp.title"/></h2>
        <form action="controller" name="searchReceipt" onsubmit="return(validate())">
            <input type="hidden" name="command" value="searchReceipt"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="<fmt:message key="search_receipt_jsp.placeholder"/>" name="receipt_pattern" required>
            <button type="submit" class="add_product_btn"><fmt:message key="search_jsp.search.button"/></button>
        </form>
        <hr>
        <c:if test="${empty requestScope.searchReceiptResult}">
            <h2><fmt:message key="view_search_receipt_result_jsp.not.found"/></h2>
        </c:if>
        <c:if test="${not empty requestScope.searchReceiptResult}">
            <table id="search_result_table">
                <tr>
                    <td>ID</td>
                    <td><fmt:message key="receipt.creation.time.column"/></td>
                    <td><fmt:message key="receipt.name.ru.column"/></td>
                    <td><fmt:message key="receipt.name.en.column"/></td>
                    <td><fmt:message key="receipt.address.ru.column"/></td>
                    <td><fmt:message key="receipt.phone.number.column"/></td>
                    <td><fmt:message key="receipt.delivery.column"/></td>
                    <td><fmt:message key="receipt.status.column"/></td>
                </tr>
                <c:forEach items="${requestScope.searchReceiptResult}" var="receipt">
                    <tr>
                        <td><c:out value="${receipt.id}"/></td>
                        <td><c:out value="${receipt.createTime}"/></td>
                        <td><c:out value="${receipt.nameRu}"/></td>
                        <td><c:out value="${receipt.nameEn}"/></td>
                        <td><c:out value="${receipt.addressRu}"/></td>
                        <td><c:out value="${receipt.phoneNumber}"/></td>
                        <c:if test="${sessionScope.lang=='ru'}">
                            <td><c:out value="${receipt.delivery.nameRu}"/></td>
                        </c:if>
                        <c:if test="${sessionScope.lang=='en'}">
                            <td><c:out value="${receipt.delivery.nameEn}"/></td>
                        </c:if>
                        <c:if test="${sessionScope.lang == 'ru'}">
                            <td><c:out value="${receipt.receiptStatus.nameRu}"/></td>
                        </c:if>
                        <c:if test="${sessionScope.lang == 'en'}">
                            <td><c:out value="${receipt.receiptStatus.nameEn}"/></td>
                        </c:if>
                        <c:if test="${sessionScope.userRole.name().toLowerCase() == 'cashier'
                        or sessionScope.userRole.name().toLowerCase() == 'senior_cashier'}">
                            <td><a href="controller?command=chooseReceipt&id=${receipt.id}"><fmt:message key="header.menu.choose.receipt"/></a></td>
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
                                   href="controller?command=searchReceipt&receipt_pattern=${sessionScope.lastSearchReceiptPattern}&currentPage=${requestScope.currentPage-1}"><fmt:message key="pagination.previous"/></a>
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
                                           href="controller?command=searchReceipt&receipt_pattern=${sessionScope.lastSearchReceiptPattern}&currentPage=${i}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${requestScope.currentPage lt requestScope.nOfPages}">
                            <li class="page_item">
                                <a class="page_link"
                                   href="controller?command=searchReceipt&receipt_pattern=${sessionScope.lastSearchReceiptPattern}&currentPage=${requestScope.currentPage+1}"><fmt:message key="pagination.next"/></a>
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
        if (document.searchReceipt.receipt_pattern.value.length > 100) {
            alertify.alert("<fmt:message key="search_receipt_jsp.title"/>", "<fmt:message key="add.product.invalid.length"/>")
            return false;
        }
        return true;
    }
</script>