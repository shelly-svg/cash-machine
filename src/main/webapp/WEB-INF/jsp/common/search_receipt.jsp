<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Поиск заказа"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="search_receipt_jsp.title"/></h2>
        <form action="controller" name="searchReceipt" onsubmit="return(validate())">
            <input type="hidden" name="command" value="searchReceipt"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="<fmt:message key="search_receipt_jsp.placeholder"/>" name="receipt_pattern" required>
            <button type="submit" class="add_product_btn"><fmt:message key="search_jsp.search.button"/></button>
        </form>
        <hr>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>

<script type="text/javascript">
    function validate() {
        if (document.searchReceipt.receipt_pattern.value.length > 100) {
            alertify.alert("<fmt:message key="search_receipt_jsp.title"/>", "<fmt:message key="add.product.invalid.length"/>")
            return false;
        }
        return true;
    }
</script>