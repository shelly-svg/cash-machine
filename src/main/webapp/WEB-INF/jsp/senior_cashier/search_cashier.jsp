<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Поиск кассиров"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="search.cashier.title"/></h2>
        <form action="controller" name="searchCashiers" onsubmit="return(validate())">
            <input type="hidden" name="command" value="viewSearchCashierResult"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="<fmt:message key="search.cashier.placeholder.fname"/>" name="cashier_first_name" required>
            <input type="text" placeholder="<fmt:message key="search.cashier.placeholder.lname"/>" name="cashier_last_name" required>
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
        if (document.searchCashiers.cashier_first_name.value.length > 45) {
            alertify.alert("<fmt:message key="cashier.fname.column"/>", "<fmt:message key="add.product.invalid.length"/>")
            return false;
        }
        if (document.searchCashiers.cashier_last_name.value.length > 45) {
            alertify.alert("<fmt:message key="cashier.lname.column"/>", "<fmt:message key="add.product.invalid.length"/>")
            return false;
        }
        return true;
    }
</script>