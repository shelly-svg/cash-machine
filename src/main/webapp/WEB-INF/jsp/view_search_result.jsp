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
            <input type="hidden" name="currentPage" value="1"/>
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
                    <tr>
                        <td><c:out value="${product.nameRu}"/></td>
                        <td><c:out value="${product.nameEn}"/></td>
                        <td><c:out value="${product.price}"/></td>
                        <td><c:out value="${product.code}"/></td>
                        <td><c:out value="${product.amount}"/></td>
                        <td><c:out value="${product.weight}"/></td>
                        <c:if test="${sessionScope.lang=='ru'}">
                            <td><c:out value="${requestScope.categories.get(product.categoryId).nameRu}"/></td>
                        </c:if>
                        <c:if test="${sessionScope.lang=='en'}">
                            <td><c:out value="${requestScope.categories.get(product.categoryId).nameEn}"/></td>
                        </c:if>
                        <td><a href="controller?command=editProduct&id=${product.id}">Edit</a></td>
                    </tr>
                </c:forEach>

            </table>
            <hr>
            <c:if test="${requestScope.nOfPages>1}">
                <nav aria-label="Navigation for products">
                    <ul class="pagination">
                        <c:if test="${requestScope.currentPage != 1}">
                            <li class="page_item">
                                <a class="page_link"
                                   href="controller?command=search&pattern=${sessionScope.lastSearchPattern}&currentPage=${requestScope.currentPage-1}">Previous</a>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${requestScope.nOfPages}" var="i">
                            <c:choose>
                                <c:when test="${requestScope.currentPage eq i}">
                                    <li class="page_item_active"><a class="page_link">
                                            ${i} <span class="sr_only">(Current)</span>
                                    </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page_item">
                                        <a class="page_link"
                                           href="controller?command=search&pattern=${sessionScope.lastSearchPattern}&currentPage=${i}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${requestScope.currentPage lt requestScope.nOfPages}">
                            <li class="page_item">
                                <a class="page_link"
                                   href="controller?command=search&pattern=${sessionScope.lastSearchPattern}&currentPage=${requestScope.currentPage+1}">Next</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>

        </c:if>


    </div>

    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>