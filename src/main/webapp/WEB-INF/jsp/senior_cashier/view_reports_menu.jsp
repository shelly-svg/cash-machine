<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="view.reports.menu.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="sidebar">
        <form action="generateWeeklyReport" method="post">
            <input type="hidden" name="action" value="download"/>
            <button type="submit" class="weekly_report_btn"><fmt:message key="download.weekly.report.name"/></button>
        </form>
        <hr>
        <form action="generateWeeklyReport" method="post">
            <input type="hidden" name="action" value="sendMail"/>
            <button type="submit" class="weekly_report_btn"><fmt:message key="sendmail.weekly.report.name"/></button>
        </form>
        <hr>
        <form action="controller">
            <input type="hidden" name="command" value="searchCashier"/>
            <button type="submit" class="cashier_report_btn"><fmt:message key="cashier.report.name.first"/></button>
        </form>
    </div>
    <div id="content">
        <c:if test="${empty sessionScope.sendMessage}">
            <h3><fmt:message key="reports.menu.title"/></h3>
        </c:if>
        <c:if test="${not empty sessionScope.sendMessage}">
            <h4>
                <fmt:message key="send.report.message"/>
                <c:remove scope="session" var="sendMessage"/>
            </h4>
        </c:if>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>