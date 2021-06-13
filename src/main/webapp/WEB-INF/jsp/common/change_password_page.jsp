<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="change.password.page.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">

    <div id="settings_content">

        <div id="change_password_container">
            <form action="controller" method="post" name="changePasswordForm" onsubmit="return(validate())">
                <input type="hidden" name="command" value="changePassword"/>
                <h4><fmt:message key="change.password.page.old.pass.title"/></h4>
                <label>
                    <input type="password" placeholder="<fmt:message key="change.password.page.old.pass.title"/>"
                           name="oldPassword"/>
                </label>
                <h4><fmt:message key="change.password.page.new.pass.title"/></h4>
                <label>
                    <input type="password" placeholder="<fmt:message key="change.password.page.new.pass.placeholder"/>"
                           name="newPassword" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"/>
                </label>
                <h4><fmt:message key="change.password.page.new.pass.repeat.title"/></h4>
                <label>
                    <input type="password" placeholder="<fmt:message key="change.password.page.new.pass.repeat.title"/>"
                           name="repPassword" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$"/>
                </label>
                <input type="hidden" name="command" value="changeUserLocale"/>

                <button type="submit" class="change_lang_btn"><fmt:message key="change.password.page.btn"/></button>
            </form>
        </div>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>

<script type="text/javascript">
    function validate() {
        if (document.changePasswordForm.newPassword.value !== document.changePasswordForm.repPassword.value) {
            alertify.alert("<fmt:message key="change.password.page.new.pass.repeat.title"/>", "<fmt:message key="change.password.command.new.password.rep.isn`t.equal"/>");
            return false;
        }
        return true;
    }
</script>