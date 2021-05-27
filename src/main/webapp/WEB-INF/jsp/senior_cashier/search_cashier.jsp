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
        <h2>SEARCH FOR CASHIER</h2>
        <form action="controller">
            <input type="hidden" name="command" value="viewSearchCashierResult"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="Enter cashiers first name" name="cashier_first_name" required>
            <input type="text" placeholder="Enter cashiers last name" name="cashier_last_name" required>
            <button type="submit" class="add_product_btn">Search</button>
        </form>
        <hr>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>