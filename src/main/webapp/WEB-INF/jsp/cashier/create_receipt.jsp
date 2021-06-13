<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<c:set var="title" value="create.receipt.page.title"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="create_receipt_jsp.title"/></h2>
        <form action="controller" method="post" name="createReceiptForm" onsubmit="return(validate())">
            <input type="hidden" name="command" value="createReceipt"/>
            <label>
                <input type="text" placeholder="<fmt:message key="receipt.name.ru.column"/>" name="name_ru" required/>
                <input type="text" placeholder="<fmt:message key="receipt.name.en.column"/>" name="name_en" required/>
                <input type="text" placeholder="<fmt:message key="receipt.address.ru.column"/>" name="address_ru"
                       required/>
                <input type="text" placeholder="<fmt:message key="receipt.address.en.column"/>" name="address_en"/>
                <textarea rows="10" cols="100" name="description_ru"
                          placeholder="<fmt:message key="receipt.description.ru.column"/>"></textarea>
            </label>
            <hr>
            <label>
<textarea rows="10" cols="100" name="description_en"
          placeholder="<fmt:message key="receipt.description.en.column"/>"></textarea>
            </label>
            <hr>
            <label>
                <input type="text" placeholder="<fmt:message key="receipt.phone.number.column"/>" name="phone_number"
                       required/>
            </label>
            <hr>
            <h5><fmt:message key="receipt.delivery.column"/></h5>
            <c:if test="${not empty requestScope.deliveries}">
                <label>
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
                </label>
            </c:if>
            <hr>
            <button type="submit" class="add_product_btn" name="submit"><fmt:message
                    key="create_receipt_jsp.title"/></button>
        </form>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>

<script type="text/javascript">
    function validate() {
        var cyrillic = /[а-яА-ЯёЁІіЇїЪъ]/g
        var phoneNumber = /^\d{10}$/g

        if (document.createReceiptForm.name_ru.value.length > 45) {
            alertify.alert("<fmt:message key="receipt.name.ru.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.createReceiptForm.name_en.value.length > 45) {
            alertify.alert("<fmt:message key="receipt.name.en.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.createReceiptForm.name_en.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="receipt.name.en.column"/>", "<fmt:message key="add.product.invalid.cyrillic"/>");
            return false;
        }
        if (document.createReceiptForm.address_ru.value.length > 150) {
            alertify.alert("<fmt:message key="receipt.address.ru.column"/>", "<fmt:message key="add.product.invalid.length"/>");
            return false;
        }
        if (document.createReceiptForm.address_en.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="receipt.address.en.column"/>", "<fmt:message key="add.product.invalid.cyrillic"/>");
            return false;
        }
        if (document.createReceiptForm.description_en.value.match(cyrillic)) {
            alertify.alert("<fmt:message key="receipt.description.en.column"/>", "<fmt:message key="add.product.invalid.cyrillic"/>");
            return false;
        }
        if (!document.createReceiptForm.phone_number.value.match(phoneNumber)) {
            alertify.alert("<fmt:message key="receipt.phone.number.column"/>", "<fmt:message key="create.receipt.phone.number.invalid"/>");
            return false;
        }
        return true;
    }
</script>