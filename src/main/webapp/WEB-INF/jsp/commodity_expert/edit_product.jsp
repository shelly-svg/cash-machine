<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<c:set var="title" value="edit.product.page.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="header.menu.edit.product"/></h2>
        <c:if test="${not empty requestScope.product}">
            <hr>
            <h6><fmt:message key="product.name.ru.column"/></h6>
            <c:out value="${requestScope.product.nameRu}"/>
            <hr>
            <h6><fmt:message key="product.name.en.column"/></h6>
            <c:out value="${requestScope.product.nameEn}"/>
            <hr>
            <h6><fmt:message key="product.code.column"/></h6>
            <c:out value="${requestScope.product.code}"/>
            <hr>
            <h6><fmt:message key="product.price.column"/></h6>
            <c:out value="${requestScope.product.price}"/>
            <hr>
            <h6><fmt:message key="product.amount.column"/></h6>
            <c:out value="${requestScope.product.amount}"/>
            <br/>
            <form action="controller" method="post" name="editProductAmount" onsubmit="return(validate())">
                <input type="hidden" name="command" value="editProduct"/>
                <input type="hidden" name="id" value="${requestScope.product.id}"/>
                <label>
                    <input type="text" placeholder="<fmt:message key="edit.product.new.amount.placeholder"/>"
                           name="amount" required/>
                </label>
                <button type="submit" class="add_product_btn" name="submit"><fmt:message
                        key="edit.product.new.amount.submit"/></button>
            </form>
            <hr>
            <h6><fmt:message key="product.weight.column"/></h6>
            <c:out value="${requestScope.product.weight}"/>
            <hr>
            <h6><fmt:message key="product.description.ru.column"/></h6>
            <c:out value="${requestScope.product.descriptionRu}"/>
            <hr>
            <h6><fmt:message key="product.description.en.column"/></h6>
            <c:out value="${requestScope.product.descriptionEn}"/>
            <hr>
            <h6><fmt:message key="product.category.column"/></h6>
            <c:if test="${sessionScope.lang == 'ru'}">
                <option><c:out value="${requestScope.product.category.nameRu}"/></option>
            </c:if>
            <c:if test="${sessionScope.lang == 'en'}">
                <option><c:out value="${requestScope.product.category.nameEn}"/></option>
            </c:if>
        </c:if>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>

<script type="text/javascript">
    function validate() {
        if (document.editProductAmount.amount.value.length > 10) {
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="edit.product.new.amount.invalid.length"/>");
            return false;
        }
        if (isNaN(document.editProductAmount.amount.value)){
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="edit.product.new.amount.invalid.nan"/>");
            return false;
        }
        if (document.editProductAmount.amount.value < 0){
            alertify.alert("<fmt:message key="edit.product.new.amount"/>", "<fmt:message key="add.product.invalid.numeric.negative"/>");
            return false;
        }
        return true;
    }
</script>