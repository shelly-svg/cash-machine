<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<c:set var="title" value="Товары заказа"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="view_receipt_products.title"/></h2>
        <c:if test="${empty requestScope.receiptProductMap}">
            <h2><fmt:message key="view_receipt_products.has.no.products"/></h2>
        </c:if>
        <c:if test="${not empty requestScope.receiptProductMap}">
        <table id="search_result_table">
            <tr>
                <td><fmt:message key="product.name.ru.column"/></td>
                <td><fmt:message key="product.name.en.column"/></td>
                <td><fmt:message key="product.code.column"/></td>
                <td><fmt:message key="view_receipt_products.amount.receipt"/></td>
                <td><fmt:message key="product.amount.column"/></td>
                <td><fmt:message key="product.weight.column"/></td>
                <td><fmt:message key="product.category.column"/></td>
                <td><fmt:message key="product.price.column"/></td>
            </tr>
            <c:forEach items="${requestScope.receiptProductMap}" var="product">
                <tr>
                    <td><c:out value="${product.key.nameRu}"/></td>
                    <td><c:out value="${product.key.nameEn}"/></td>
                    <td><c:out value="${product.key.code}"/></td>
                    <td><c:out value="${product.value.toString()}"/></td>
                    <td><c:out value="${product.key.amount}"/></td>
                    <td><c:out value="${product.key.weight}"/></td>
                    <c:if test="${sessionScope.lang=='ru'}">
                        <td><c:out value="${product.key.category.nameRu}"/></td>
                    </c:if>
                    <c:if test="${sessionScope.lang=='en'}">
                        <td><c:out value="${product.key.category.nameEn}"/></td>
                    </c:if>
                    <td><c:out value="${product.key.price}"/></td>
                    <c:if test="${sessionScope.userRole.name().toLowerCase() == 'cashier'
                    and sessionScope.currentReceipt.receiptStatus.name().toLowerCase() == 'new_receipt'}">
                        <td>
                            <form action="controller" method="post" id="change_receipt_product_amount"
                                  name="changeAmountForm" onsubmit="return(validate(this))">
                                <input type="hidden" name="command" value="editReceiptProducts">
                                <input type="hidden" name="product_id" value="${product.key.id}">
                                <input type="hidden" name="receipt_id" value="${sessionScope.currentReceipt.id}">
                                <input type="hidden" name="storeAmount" value="${product.key.amount}">
                                <input type="hidden" name="oldAmount" value="${product.value.toString()}">
                                <input type="text" name="newAmount"
                                       placeholder="<fmt:message key="edit.product.new.amount"/>" required>
                                <button type="submit" class="add_product_btn" name="submit"><fmt:message
                                        key="edit.product.new.amount.submit"/></button>
                            </form>
                        </td>
                    </c:if>
                    <c:if test="${sessionScope.userRole.name().toLowerCase() == 'senior_cashier'
                    and sessionScope.currentReceipt.receiptStatus.name().toLowerCase() == 'new_receipt'}">
                        <td>
                            <form action="controller" method="post" name="removeProductFromReceipt">
                                <input type="hidden" name="command" value="removeProductFromReceipt">
                                <input type="hidden" name="receipt_id" value="${sessionScope.currentReceipt.id}">
                                <input type="hidden" name="product_id" value="${product.key.id}">
                                <input type="hidden" name="amount" value="${product.value.toString()}">
                                <button type="submit" class="remove_product_btn" name="submit"><fmt:message
                                        key="view_receipt_products.remove.product"/></button>
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

<script type="text/javascript">
    function validate(obj) {
        if (isNaN(obj.newAmount.value)) {
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="edit.product.new.amount.invalid.nan"/>");
            return false;
        }
        const newAmount = parseInt(obj.newAmount.value, 10);
        const oldAmount = parseInt(obj.oldAmount.value, 10);
        const storeAmount = parseInt(obj.storeAmount.value, 10);

        if (newAmount > 999999999) {
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (newAmount <= 0) {
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="edit.receipt.products.command.amount.error.null"/>");
            return false;
        }
        if (newAmount === oldAmount) {
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="edit.receipt.products.command.same.amount"/>");
            return false;
        }

        if (newAmount < oldAmount) {
            return true
        }
        if ((newAmount > oldAmount) && (newAmount > (oldAmount + storeAmount))) {
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="edit.receipt.products.command.amount.error"/>");
            return false;
        }

        return true;
    }
</script>