<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Товары заказа"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>VIEW RECEIPT PRODUCTS</h2>
        <c:if test="${empty requestScope.receiptProductMap}">
            <h2>Current receipt has no products yet</h2>
        </c:if>
        <c:if test="${not empty requestScope.receiptProductMap}">
        <table id="search_result_table">
            <tr>
                <td>Russian name</td>
                <td>English name</td>
                <td>Code</td>
                <td>Amount at the receipt</td>
                <td>Weight</td>
                <td>Category</td>
                <td>Price for one</td>
            </tr>
            <c:forEach items="${requestScope.receiptProductMap}" var="product">
                <tr>
                    <td><c:out value="${product.key.nameRu}"/></td>
                    <td><c:out value="${product.key.nameEn}"/></td>
                    <td><c:out value="${product.key.code}"/></td>
                    <td><c:out value="${product.value.toString()}"/></td>
                    <td><c:out value="${product.key.weight}"/></td>
                    <c:if test="${sessionScope.lang=='ru'}">
                        <td><c:out value="${product.key.category.nameRu}"/></td>
                    </c:if>
                    <c:if test="${sessionScope.lang=='en'}">
                        <td><c:out value="${product.key.category.nameEn}"/></td>
                    </c:if>
                    <td><c:out value="${product.key.price}"/></td>
                    <c:if test="${sessionScope.userRole.name().toLowerCase() == 'cashier'}">
                        <td>
                            <form action="controller" method="post" id="change_receipt_product_amount">
                                <input type="hidden" name="command" value="editReceiptProducts">
                                <input type="hidden" name="product_id" value="${product.key.id}">
                                <input type="hidden" name="receipt_id" value="${sessionScope.currentReceipt.id}">
                                <input type="text" name="newAmount" placeholder="New amount" required>
                                <button type="submit" class="add_product_btn" name="submit">Accept</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </c:if>
        </table>
        <hr>

    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>