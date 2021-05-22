<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Редактировать товар"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>EDIT PRODUCT</h2>
        <c:if test="${not empty requestScope.product}">
            <hr>
            <h6>Product name ru</h6>
            <c:out value="${requestScope.product.nameRu}"/>
            <hr>
            <h6>Product name en</h6>
            <c:out value="${requestScope.product.nameEn}"/>
            <hr>
            <h6>Product code</h6>
            <c:out value="${requestScope.product.code}"/>
            <hr>
            <h6>Product price</h6>
            <c:out value="${requestScope.product.price}"/>
            <hr>
            <h6>Product amount</h6>
            <c:out value="${requestScope.product.amount}"/>
            <br/>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="editProduct">
                <input type="hidden" name="id" value="${requestScope.product.id}">
                <input type="text" placeholder="Enter new amount of product" name="amount" required>
                <button type="submit" class="add_product_btn" name="submit">Accept</button>
            </form>
            <hr>
            <h6>Product weight</h6>
            <c:out value="${requestScope.product.weight}"/>
            <hr>
            <h6>Product description on ru</h6>
            <c:out value="${requestScope.product.descriptionRu}"/>
            <hr>
            <h6>Product description on en</h6>
            <c:out value="${requestScope.product.descriptionEn}"/>
            <hr>
            <h6>Product category</h6>
            <c:if test="${sessionScope.lang == 'ru'}">
                <option><c:out value="${requestScope.category.nameRu}"/></option>
            </c:if>
            <c:if test="${sessionScope.lang == 'en'}">
                <option><c:out value="${requestScope.category.nameEn}"/></option>
            </c:if>
        </c:if>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>