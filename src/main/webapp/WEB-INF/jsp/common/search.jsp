<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<c:set var="title" value="Search product"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2><fmt:message key="search_jsp.title"/></h2>
        <form action="controller" name="searchProducts" onsubmit="return(validate())">
            <input type="hidden" name="command" value="searchProduct"/>
            <input type="hidden" name="currentPage" value="1"/>
            <label>
                <input type="text" placeholder="<fmt:message key="search_jsp.search.placeholder"/>" name="pattern"
                       required>
            </label>
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
        if (document.searchProducts.pattern.value.length > 100) {
            alertify.alert("<fmt:message key="search_jsp.search.button"/>", "<fmt:message key="search_jsp.pattern.invalid"/>")
            return false;
        }
        return true;
    }
</script>