<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="settings.page.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="settings_content">
        <h2><fmt:message key="settings_jsp.settings"/></h2>
        <hr>
        <c:if test="${not empty sessionScope.userMessage}">
            <h5><fmt:message key="${sessionScope.userMessage}"/></h5>
            <c:remove scope="session" var="userMessage"/>
            <hr>
        </c:if>
        <div id="right_settings_block">
        </div>

        <div id="settings_container">
            <div id="change_language_form">
                <fmt:message key="settings_jsp.lang.message"/> - <c:out value="${sessionScope.user.localeName}"/>

                <form action="controller" method="post">
                    <input type="hidden" name="command" value="changeUserLocale"/>
                    <button type="submit" class="change_lang_btn"><fmt:message
                            key="settings_jsp.change_lang_btn"/></button>
                </form>
            </div>
            <div id="change_password_form">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="sendConfirmationLink"/>
                    <button type="submit" class="change_lang_btn"><fmt:message
                            key="settings_jsp.change_pass_btn"/></button>
                </form>
            </div>
        </div>

        <div id="left_settings_block">
        </div>

        <div id="clear">

        </div>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>