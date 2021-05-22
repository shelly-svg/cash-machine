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
        <h2>VIEW SEARCH RESULT</h2>
        <form action="controller" name="find_product_by_name_or_code">
            <input type="hidden" name="command" value="search"/>
            <input type="text" placeholder="Enter product name or code" name="pattern" required>
            <button type="submit" class="add_product_btn" name="submit">Search</button>
        </form>
        <hr>
        <c:if test="${empty requestScope.searchResult}">
            <h2>Cannot find any product by your request</h2>
        </c:if>
        <c:if test="${not empty requestScope.searchResult}">
            <table id="search_result_table">
                <tr>
                    <td>Russian name</td>
                    <td>English name</td>
                    <td>Price</td>
                    <td>Code</td>
                    <td>Amount</td>
                    <td>Weight</td>
                    <td>Category</td>
                </tr>
                <c:forEach items="${requestScope.searchResult}" var="product">
                    <c:if test="${sessionScope.lang=='ru'}">
                        <tr>
                            <td><c:out value="${product.nameRu}"/></td>
                            <td><c:out value="${product.nameEn}"/></td>
                            <td><c:out value="${product.price}"/></td>
                            <td><c:out value="${product.code}"/></td>
                            <td><c:out value="${product.amount}"/></td>
                            <td><c:out value="${product.weight}"/></td>
                            <td><c:out value="${requestScope.categories.get(product.categoryId).nameRu}"/></td>
                            <td><a href="controller?command=updateProduct&id=${product.id}">Edit</a></td>
                        </tr>
                    </c:if>
                    <c:if test="${sessionScope.lang=='en'}">
                        <tr>
                            <td><c:out value="${product.nameRu}"/></td>
                            <td><c:out value="${product.nameEn}"/></td>
                            <td><c:out value="${product.price}"/></td>
                            <td><c:out value="${product.code}"/></td>
                            <td><c:out value="${product.amount}"/></td>
                            <td><c:out value="${product.weight}"/></td>
                            <td><c:out value="${requestScope.categories.get(product.categoryId).nameEn}"/></td>
                            <td><a href="controller?command=updateProduct&id=${product.id}">Edit</a></td>
                        </tr>
                    </c:if>
                </c:forEach>
            </table>
            <hr>

        </c:if>


    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>