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
            <button type="submit" class="weekly_report_btn">Generate weekly report</button>
        </form>
        <hr>
        <form action="controller">
            <input type="hidden" name="command" value="searchCashier">
            <button type="submit" class="cashier_report_btn">Make cashier report</button>
        </form>
    </div>
    <div id="content">
        <h3>Здравствуйте, ${sessionScope.user.firstName}, вы зашли как ${sessionScope.userRole.name()}!</h3>
        <h3>Выберите отчет</h3>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>