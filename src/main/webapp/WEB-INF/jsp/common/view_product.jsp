<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Просмотр товара"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="view_product_jsp.title"/></h2>
        <c:if test="${not empty requestScope.product}">
            <hr>
            <h6><fmt:message key="product.name.ru.column"/></h6>
            <c:out value="${requestScope.product.nameRu}"/>
            <hr>
            <h6><fmt:message key="product.name.en.column"/></h6>
            <c:out value="${requestScope.product.nameEn}"/>
            <hr>
            <h6><fmt:message key="product.code.column"/></h6>
            <c:out value="${requestScope.product.code}"/>
            <hr>
            <h6><fmt:message key="product.price.column"/></h6>
            <c:out value="${requestScope.product.price}"/>
            <hr>
            <h6><fmt:message key="product.amount.column"/></h6>
            <c:out value="${requestScope.product.amount}"/>
            <hr>
            <h6><fmt:message key="product.weight.column"/></h6>
            <c:out value="${requestScope.product.weight}"/>
            <hr>
            <c:if test="${not empty requestScope.product.descriptionRu}">
                <h6><fmt:message key="product.description.ru.column"/></h6>
                <c:out value="${requestScope.product.descriptionRu}"/>
                <hr>
            </c:if>
            <c:if test="${not empty requestScope.product.descriptionEn}">
                <h6><fmt:message key="product.description.en.column"/></h6>
                <c:out value="${requestScope.product.descriptionEn}"/>
                <hr>
            </c:if>
            <h6><fmt:message key="product.category.column"/></h6>
            <c:if test="${sessionScope.lang == 'ru'}">
                <option><c:out value="${requestScope.product.category.nameRu}"/></option>
            </c:if>
            <c:if test="${sessionScope.lang == 'en'}">
                <option><c:out value="${requestScope.product.category.nameEn}"/></option>
            </c:if>
        </c:if>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>