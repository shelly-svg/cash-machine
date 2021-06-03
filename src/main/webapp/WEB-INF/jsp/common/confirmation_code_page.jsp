<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Введите код"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="settings_content">
        <h2>Введите код, который был отправлен вам на почту</h2>

        <div id="settings_container">
            <div id="confirm_code_form">
                <form action="controller" method="post">
                    <input type="text" name="">
                    <input type="hidden" name="command" value="changeUserLocale"/>

                    <button type="submit" class="change_lang_btn">Подтвердить</button>
                </form>
            </div>
        </div>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>