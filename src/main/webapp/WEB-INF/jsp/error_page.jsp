<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<html>
<c:set var="title" value="error.page.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<body>

<c:if test="${not empty sessionScope.errorMessage and empty exception and empty code}">
    <h3>${sessionScope.errorMessage}</h3>
</c:if>
<c:if test="${empty sessionScope.errorMessage}">
    <h3><fmt:message key="error.page.empty.error.message"/></h3>
</c:if>
<c:remove scope="session" var="errorMessage"/>
<hr>
<c:if test="${not empty sessionScope.user}">
    <p><fmt:message key="error.page.not.empty.user"/></p><a href="controller?command=viewMenu">Menu</a>
</c:if>
<c:if test="${empty sessionScope.user}">
    <p><fmt:message key="error.page.empty.user"/><a href="${pageContext.request.contextPath}/login.jsp">Login page</a>.
    </p>
</c:if>
</body>
</html>
