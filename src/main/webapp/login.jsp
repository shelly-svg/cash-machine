<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Логин"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div id="log_form">
    <form id="login_form" action="controller" method="post">
        <h1>Please fill in this form to login into an account.</h1>
        <hr>
        <input type="hidden" name="command" value="login"/>
        <label>
            <input type="text" placeholder="Enter Login" name="login" required>
        </label>
        <label>
            <input type="password" placeholder="Enter Password" name="password" required>
        </label>
        <hr>
        <button type="submit" class="login_btn" name="submit"><fmt:message
                key="login_jsp.button.login"/></button>
    </form>
</div>
<div id="clear">

</div>

</body>
</html>