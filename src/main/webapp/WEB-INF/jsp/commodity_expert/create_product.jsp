<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<fmt:message var="titleE" key="product.create.new.product"/>
<c:set var="title" value="Добавление товара"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="product.creating.new.product"/></h2>
        <form action="controller" method="post" name="addProductForm" onsubmit="return(validate())">
            <input type="hidden" name="command" value="createProduct"/>
            <input type="text" placeholder="<fmt:message key="product.name.ru.column"/>" name="name_ru" required>
            <input type="text" placeholder="<fmt:message key="product.name.en.column"/>" name="name_en" required>
            <input type="text" placeholder="<fmt:message key="product.code.column"/>" name="code" required>
            <input type="text" placeholder="<fmt:message key="product.price.column"/>" name="price" required>
            <input type="text" placeholder="<fmt:message key="product.amount.column"/>" name="amount" required>
            <input type="text" placeholder="<fmt:message key="product.weight.column"/>" name="weight" required>
            <textarea rows="10" cols="100" name="description_ru"
                      placeholder="<fmt:message key="product.description.ru.column"/>"></textarea>
            <hr>
            <textarea rows="10" cols="100" name="description_en"
                      placeholder="<fmt:message key="product.description.en.column"/>"></textarea>
            <hr>
            <c:if test="${not empty requestScope.categories}">
                <select name="category_id">
                    <c:forEach items="${requestScope.categories}" var="category">
                        <c:if test="${sessionScope.lang == 'ru'}">
                            <option><c:out value="${category.value.nameRu}"/></option>
                        </c:if>
                        <c:if test="${sessionScope.lang == 'en'}">
                            <option><c:out value="${category.value.nameEn}"/></option>
                        </c:if>
                    </c:forEach>
                </select>
            </c:if>
            <hr>
            <button type="submit" class="add_product_btn" name="submit"><fmt:message
                    key="product.create.new.product"/></button>
        </form>
    </div>
    <div id="clear">
    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>

<fmt:message key="login_jsp.button.login"/>
<script type="text/javascript">
    function validate() {
        var cyrillic = /[а-яА-ЯёЁІіЇїЪъ]/g

        if (document.addProductForm.name_ru.value.length > 100) {
            alertify.alert("<fmt:message key="product.name.ru.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.name_en.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="product.name.en.column"/>", "<fmt:message key="add.product.invalid.cyrillic"/>");
            return false;
        }
        if (document.addProductForm.name_en.value.length > 100) {
            alertify.alert("<fmt:message key="product.name.en.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.code.value.length > 128) {
            alertify.alert("<fmt:message key="product.code.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.code.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="product.code.column"/>", "<fmt:message key="add.product.invalid.cyrillic"/>");
            return false;
        }
        if (isNaN(document.addProductForm.price.value)) {
            alertify.alert("<fmt:message key="product.price.column"/>", "<fmt:message key="add.product.invalid.numeric"/>");
            return false;
        }
        if (document.addProductForm.price.value.length > 10) {
            alertify.alert("<fmt:message key="product.price.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.price.value < 0){
            alertify.alert("<fmt:message key="product.price.column"/>", "<fmt:message key="add.product.invalid.numeric.negative"/>");
            return false;
        }
        if (isNaN(document.addProductForm.amount.value)) {
            alertify.alert("<fmt:message key="product.amount.column"/>", "<fmt:message key="add.product.invalid.numeric"/>");
            return false;
        }
        if (document.addProductForm.amount.value.length > 10) {
            alertify.alert("<fmt:message key="product.amount.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.amount.value < 0){
            alertify.alert("<fmt:message key="product.amount.column"/>", "<fmt:message key="add.product.invalid.numeric.negative"/>");
            return false;
        }
        if (isNaN(document.addProductForm.weight.value)) {
            alertify.alert("<fmt:message key="product.weight.column"/>", "<fmt:message key="add.product.invalid.numeric"/>");
            return false;
        }
        if (document.addProductForm.weight.value.length > 10) {
            alertify.alert("<fmt:message key="product.weight.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.weight.value < 0){
            alertify.alert("<fmt:message key="product.weight.column"/>", "<fmt:message key="add.product.invalid.numeric.negative"/>");
            return false;
        }
        if (document.addProductForm.description_ru.value.length > 65000) {
            alertify.alert("<fmt:message key="product.description.ru.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.addProductForm.description_en.value.length > 65000) {
            alertify.alert("<fmt:message key="product.description.en.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        return true;
    }
</script>