<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Все товары"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>VIEW SEARCH RECEIPT RESULT</h2>
        <form action="controller" name="find_receipt_by_id_or_date">
            <input type="hidden" name="command" value="searchReceipt"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="Enter receipt id or creation date" name="receipt_pattern" required>
            <button type="submit" class="add_product_btn" name="submit">Search</button>
        </form>
        <hr>
        <c:if test="${empty requestScope.searchReceiptResult}">
            <h2>Cannot find any receipt by your request</h2>
        </c:if>
        <c:if test="${not empty requestScope.searchReceiptResult}">
            <table id="search_result_table">
                <tr>
                    <td>ID</td>
                    <td>Creation time</td>
                    <td>Ru customers name</td>
                    <td>En customers name</td>
                    <td>Address</td>
                    <td>Phone number</td>
                    <td>Delivery</td>
                    <td>Receipt status</td>
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
                            <td><a href="controller?command=chooseReceipt&id=${receipt.id}">Choose</a></td>
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
                                   href="controller?command=searchReceipt&receipt_pattern=${sessionScope.lastSearchReceiptPattern}&currentPage=${requestScope.currentPage-1}">Previous</a>
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
                                   href="controller?command=searchReceipt&receipt_pattern=${sessionScope.lastSearchReceiptPattern}&currentPage=${requestScope.currentPage+1}">Next</a>
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