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
<c:if test="%${not empty sessionScope.user}">
    <p>Get back to the main menu? </p><a href="?command=menu">Menu</a>
</c:if>
<p>Get back to the login? <a href="${pageContext.request.contextPath}/login.jsp">Login page</a>.</p>
</body>
</html>
<%--<div class="container signin">
            <p>Already have an account? <a href="#">Sign in</a>.</p>
        </div>--%>

<%--<p>By creating an account you agree to our <a href="#">Terms & Privacy</a>.</p>--%>