<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Генерация отчетов"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="sidebar">
        <form action="generateWeeklyReport" method="post">
            <input type="submit" value="Weekly report">
        </form>
    </div>
    <div id="content">
        <h1>Здравствуйте, ${sessionScope.user.firstName}, вы зашли как ${sessionScope.userRole.name()}!</h1>
        <h1>Выберите отчет</h1>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>