<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Настройки пользователя"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
  <%@ include file="/WEB-INF/jspf/header.jspf" %>
  <div id="sidebar">
    <h2>НАСТРОЕЧКИ</h2>
  </div>
  <div id="content">
    <h2><h2><fmt:message key="settings_jsp.label.localization"/></h2></h2>
  </div>
  <div id="clear">

  </div>
  <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>