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
        <h1>Здравствуйте, ${sessionScope.user.firstName}, вы зашли как ${sessionScope.userRole.name()}!</h1>
        <h1><c:out value="${sessionScope.lang}"/></h1>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>