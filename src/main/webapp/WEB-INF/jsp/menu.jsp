<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Главное меню"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="sidebar">
        <h2><a href="?command=viewSettings">Settings</a></h2>
        <h2><a href="?command=logout">Log out</a></h2>
    </div>
    <div id="content">
        <h2><h2><fmt:message key="settings_jsp.label.localization"/></h2></h2>
        <c:forEach items="${sessionScope.products}" var="product">
            <c:if test="${sessionScope.lang=='ru'}">
                <h6>PRODUCT: <c:out value="${product.nameRu}"/></h6>
            </c:if>
            <c:if test="${sessionScope.lang=='en'}">
                <h6>PRODUCT: <c:out value="${product.nameEn}"/></h6>
            </c:if>
        </c:forEach>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>