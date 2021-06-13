<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="name" required="true" rtexprvalue="true" %>

<c:if test="${empty sessionScope.lang}">
    <fmt:setLocale value="en"/>
</c:if>
<c:if test="${not empty sessionScope.lang}">
    <fmt:setLocale value="${sessionScope.lang}"/>
</c:if>
<fmt:setBundle basename="resources"/>

<h2><fmt:message key="welcome.message"/>, ${name}!</h2>