<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>

<html>

<c:set var="title" value="main.menu.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="sidebar">
    </div>
    <div id="content">
        <h2><fmt:message key="header.menu.main.menu"/></h2>
    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>