<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ taglib prefix="my" uri="/WEB-INF/mytags/price.tld" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Просмотр заказа"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="view_current_receipt_jsp.title"/></h2>
        <c:if test="${not empty sessionScope.currentReceipt}">
            <hr>
            <h6>ID</h6>
            <c:out value="${sessionScope.currentReceipt.id}"/>
            <hr>
            <h6><fmt:message key="receipt.creation.time.column"/></h6>
            <c:out value="${sessionScope.currentReceipt.createTime}"/>
            <hr>
            <h6><fmt:message key="receipt.name.ru.column"/></h6>
            <c:out value="${sessionScope.currentReceipt.nameRu}"/>
            <hr>
            <h6><fmt:message key="receipt.name.en.column"/></h6>
            <c:out value="${sessionScope.currentReceipt.nameEn}"/>
            <hr>
            <h6><fmt:message key="receipt.address.ru.column"/></h6>
            <c:out value="${sessionScope.currentReceipt.addressRu}"/>
            <hr>
            <c:if test="${not empty sessionScope.currentReceipt.addressEn}">
                <h6><fmt:message key="receipt.address.en.column"/></h6>
                <c:out value="${sessionScope.currentReceipt.addressEn}"/>
                <hr>
            </c:if>
            <c:if test="${not empty sessionScope.currentReceipt.descriptionRu}">
                <h6><fmt:message key="receipt.description.ru.column"/></h6>
                <c:out value="${sessionScope.currentReceipt.descriptionRu}"/>
                <hr>
            </c:if>
            <c:if test="${not empty sessionScope.currentReceipt.descriptionEn}">
                <h6><fmt:message key="receipt.description.en.column"/></h6>
                <c:out value="${sessionScope.currentReceipt.descriptionEn}"/>
                <hr>
            </c:if>
            <h6><fmt:message key="receipt.phone.number.column"/></h6>
            <c:out value="${sessionScope.currentReceipt.phoneNumber}"/>
            <hr>
            <h6><fmt:message key="receipt.delivery.column"/></h6>
            <c:if test="${sessionScope.lang == 'ru'}">
                <option><c:out value="${sessionScope.currentReceipt.delivery.nameRu}"/></option>
            </c:if>
            <c:if test="${sessionScope.lang == 'en'}">
                <option><c:out value="${sessionScope.currentReceipt.delivery.nameEn}"/></option>
            </c:if>
            <hr>
            <h6><fmt:message key="receipt.status.column"/></h6>
            <c:if test="${sessionScope.lang == 'ru'}">
                <option><c:out value="${sessionScope.currentReceipt.receiptStatus.nameRu}"/></option>
            </c:if>
            <c:if test="${sessionScope.lang == 'en'}">
                <option><c:out value="${sessionScope.currentReceipt.receiptStatus.nameEn}"/></option>
            </c:if>
            <hr>
            <h6><fmt:message key="receipt.user.column"/></h6>
            <c:out value="${requestScope.creator}"/>
            <hr>
            <c:if test="${empty requestScope.currentReceiptProductMap}}"><h6><fmt:message key="view_current_receipt_jsp.has.no.products"/></h6></c:if>
            <c:if test="${not empty requestScope.currentReceiptProductMap}">
                <h6><fmt:message key="view_current_receipt_jsp.title.products"/>:</h6>
                <c:forEach items="${requestScope.currentReceiptProductMap}" var="product">
                    <c:if test="${sessionScope.lang == 'ru'}">
                        <h5><c:out value="${product.key.nameRu}"/>
                    </c:if>
                    <c:if test="${sessionScope.lang == 'en'}">
                        <h5><c:out value="${product.key.nameEn}"/>
                    </c:if>
                    ; <fmt:message key="view_current_receipt_jsp.products.amount"/> = <c:out value="${product.value.toString()}"/></h5>
                </c:forEach>
            </c:if>
            <hr>
            <h5><fmt:message key="view_current_receipt_jsp.receipt.price"/>:</h5>
            <my:price receiptId="${sessionScope.currentReceipt.id}"/>
            <c:if test="${sessionScope.currentReceipt.receiptStatus.name().toLowerCase() == 'new_receipt'
            and sessionScope.userRole.name().toLowerCase() == 'cashier'}">
                <hr>
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="setReceiptStatusClosed">
                    <button type="submit" class="close_receipt_btn" name="submit"><fmt:message key="view_current_receipt_jsp.close.receipt.btn"/></button>
                </form>
            </c:if>
            <c:if test="${sessionScope.currentReceipt.receiptStatus.name().toLowerCase() == 'new_receipt'
            and sessionScope.userRole.name().toLowerCase() == 'senior_cashier'}">
                <hr>
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="setReceiptStatusCanceled">
                    <button type="submit" class="close_receipt_btn" name="submit"><fmt:message key="view_current_receipt_jsp.cancel.receipt.btn"/></button>
                </form>
            </c:if>
        </c:if>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>