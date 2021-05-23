<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Создать заказ"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>CREATE RECEIPT</h2>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="createReceipt"/>
            <input type="text" placeholder="Enter customers name at russian" name="name_ru" required>
            <input type="text" placeholder="Enter customers name at english" name="name_en" required>
            <input type="text" placeholder="Enter customers ru address" name="address_ru" required>
            <input type="text" placeholder="Enter customers en address" name="address_en" required>
            <textarea rows="10" cols="100" name="description_ru"
                      placeholder="Enter russian description of the receipt"></textarea>
            <hr>
            <textarea rows="10" cols="100" name="description_en"
                      placeholder="Enter english description of the receipt"></textarea>
            <hr>
            <input type="text" placeholder="Enter customers phone number" name="phone_number" required>
            <hr>
            <c:if test="${not empty requestScope.deliveries}">
                <select name="delivery_id">
                    <c:forEach items="${requestScope.deliveries}" var="delivery">
                        <c:if test="${sessionScope.lang == 'ru'}">
                            <option><c:out value="${delivery.nameRu}"/></option>
                        </c:if>
                        <c:if test="${sessionScope.lang == 'en'}">
                            <option><c:out value="${delivery.nameEn}"/></option>
                        </c:if>
                    </c:forEach>
                </select>
            </c:if>
            <hr>
            <button type="submit" class="add_product_btn" name="submit">Create receipt</button>
        </form>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>