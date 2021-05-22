<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<html>
<head>
    <title>ERROR</title>
</head>
<body>
<c:if test="${not empty errorMessage and empty exception and empty code}">
    <h3>Error message: ${errorMessage}</h3>
</c:if>
<hr>
<c:if test="${not empty sessionScope.user}">
    <p>Get back to the main menu? </p><a href="?command=viewMenu">Menu</a>
</c:if>
<c:if test="${empty sessionScope.user}">
    <p>Get back to the login? <a href="${pageContext.request.contextPath}/login.jsp">Login page</a>.</p>
</c:if>
</body>
</html>
