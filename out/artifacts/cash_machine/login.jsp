<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<c:if test="${empty sessionScope.lang}">
    <script src='https://www.google.com/recaptcha/api.js?hl=en'></script>
</c:if>
<c:if test="${not empty sessionScope.lang}">
    <script src='https://www.google.com/recaptcha/api.js?hl=${sessionScope.lang}'></script>
</c:if>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Login page"/>
<%--<fmt:message var="title" key="login_jsp.page.title"/>--%>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<c:if test="${not empty sessionScope.user}">
    <meta http-equiv="refresh" content="0 ; url=controller?command=viewMenu"/>
</c:if>

<body>

<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div id="log_form">
    <form id="login_form" action="controller" method="post" name="loginForm" onsubmit="return(validate())">
        <h1><fmt:message key="login_jsp.welcome.text"/></h1>
        <h1></h1>
        <hr>
        <input type="hidden" name="command" value="login"/>
        <label>
            <input type="text" placeholder="<fmt:message key="login_jsp.placeholder.login"/>" name="login">
        </label>
        <label>
            <input type="password" placeholder="<fmt:message key="login_jsp.placeholder.password"/>" name="password">
        </label>
        <hr>
        <div class="g-recaptcha" id="rcaptcha" data-sitekey="6Lfg_PcaAAAAABf9bV-7_G3nbBCTlVWEBj38IyoK">
        </div>
        <hr>
        <button type="submit" class="login_btn" name="submit"><fmt:message
                key="login_jsp.button.login"/></button>
        <hr>
    </form>

</div>

<div id="clear">

</div>

</body>
</html>

<script type="text/javascript">
    function validate() {
        if (document.loginForm.login.value === "") {
            alertify.alert("<fmt:message key="login_jsp.login.invalid"/>", "<fmt:message key="login_jsp.login.invalid.empty"/>");
            return false;
        }
        if (document.loginForm.password.value === "") {
            alertify.alert("<fmt:message key="login_jsp.password.invalid"/>", "<fmt:message key="login_jsp.password.invalid.empty"/>");
            return false;
        }
        if (document.loginForm.login.value.length > 16 || document.loginForm.login.value.length < 5) {
            alertify.alert("<fmt:message key="login_jsp.login.invalid"/>", "<fmt:message key="login_jsp.value.invalid.range"/>");
            return false;
        }
        if (document.loginForm.password.value.length > 16 || document.loginForm.password.value.length < 6) {
            alertify.alert("<fmt:message key="login_jsp.password.invalid"/>", "<fmt:message key="login_jsp.value.invalid.range"/>");
            return false;
        }
        var cyrillic = /[а-яА-ЯёЁІіЇїЪъ]/g
        if (document.loginForm.login.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="login_jsp.login.invalid"/>", "<fmt:message key="login_jsp.value.invalid.cyrillic"/>");
            return false;
        }
        if (document.loginForm.password.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="login_jsp.password.invalid"/>", "<fmt:message key="login_jsp.value.invalid.cyrillic"/>");
            return false;
        }
        return true;
    }
</script>