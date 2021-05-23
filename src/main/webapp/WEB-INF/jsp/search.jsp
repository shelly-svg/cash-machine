<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Все товары"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div id="add_product_form">
        <h2>VIEW ALL PRODUCTS</h2>
        <form action="controller" name="find_product_by_name_or_code">
            <input type="hidden" name="command" value="searchProduct"/>
            <input type="hidden" name="currentPage" value="1"/>
            <input type="text" placeholder="Enter product name or code" name="pattern" required>
            <button type="submit" class="add_product_btn" name="submit">Search</button>
        </form>
        <hr>
    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>