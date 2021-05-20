<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Добавить товар"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>Add new PRODUCT</h2>
        <form action="controller" method="post">
            <input type="hidden" name="command" value="addProduct"/>
            <input type="text" placeholder="Enter product name at Russian" name="name_ru" required>
            <input type="text" placeholder="Enter product name at English" name="name_en" required>
            <input type="text" placeholder="Enter product code" name="code" required>
            <input type="text" placeholder="Enter product price" name="price" required>
            <input type="text" placeholder="Enter product amount" name="amount" required>
            <input type="text" placeholder="Enter product weight" name="weight" required>
            <textarea rows="10" cols="100" name="description_ru" placeholder="Enter russian description of product">
                </textarea>
            <hr>
            <textarea rows="10" cols="100" name="description_en" placeholder="Enter english description of product">
                </textarea>
            <hr>
            <c:if test="${not empty requestScope.categories}">
                <select name="category_id">
                    <c:forEach items="${requestScope.categories}" var="category">
                        <c:if test="${sessionScope.lang == 'ru'}">
                            <option><c:out value="${category.nameRu}"/></option>
                        </c:if>
                        <c:if test="${sessionScope.lang == 'en'}">
                            <option><c:out value="${category.nameEn}"/></option>
                        </c:if>
                    </c:forEach>
                </select>
            </c:if>
            <hr>
            <button type="submit" class="add_product_btn" name="submit">Add new product</button>
        </form>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>