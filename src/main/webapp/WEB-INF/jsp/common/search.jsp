<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/alertify.jspf" %>

<html>

<c:set var="title" value="search.product.page.title"/>
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
                       required/>
            </label>
            <button type="submit" class="add_product_btn"><fmt:message key="search_jsp.search.button"/></button>
        </form>
        <hr>
    </div>
    <c:if test="${sessionScope.userRole.name().toLowerCase() == 'cashier'}">
        <hr>
        <a href="controller?command=viewSearchProductPage&sort=byName">Sort by name</a>
        <a href="controller?command=viewSearchProductPage&sort=byPrice">Sort by price</a>
        <a href="controller?command=viewSearchProductPage">Default</a>
        <hr>
        <c:if test="${not empty requestScope.allProducts}">
            <table id="search_result_table">
                <tr>
                    <td><fmt:message key="product.name.ru.column"/></td>
                    <td><fmt:message key="product.name.en.column"/></td>
                    <td><fmt:message key="product.price.column"/></td>
                    <td><fmt:message key="product.code.column"/></td>
                    <td><fmt:message key="product.amount.column"/></td>
                    <td><fmt:message key="product.weight.column"/></td>
                    <td><fmt:message key="product.category.column"/></td>
                </tr>
                <c:forEach items="${requestScope.allProducts}" var="product">
                    <tr>
                        <td><c:out value="${product.nameRu}"/></td>
                        <td><c:out value="${product.nameEn}"/></td>
                        <td><c:out value="${product.price}"/></td>
                        <td><c:out value="${product.code}"/></td>
                        <td><c:out value="${product.amount}"/></td>
                        <td><c:out value="${product.weight}"/></td>
                        <c:if test="${sessionScope.lang=='ru'}">
                            <td><c:out value="${product.category.nameRu}"/></td>
                        </c:if>
                        <c:if test="${sessionScope.lang=='en'}">
                            <td><c:out value="${product.category.nameEn}"/></td>
                        </c:if>
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
                                   href="controller?command=viewSearchProductPage&currentPage=${requestScope.currentPage-1}&sort=${requestScope.sort}"><fmt:message
                                        key="pagination.previous"/></a>
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
                                           href="controller?command=viewSearchProductPage&currentPage=${i}&sort=${requestScope.sort}">${i}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${requestScope.currentPage lt requestScope.nOfPages}">
                            <li class="page_item">
                                <a class="page_link"
                                   href="controller?command=viewSearchProductPage&currentPage=${requestScope.currentPage+1}&sort=${requestScope.sort}"><fmt:message
                                        key="pagination.next"/></a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>

        </c:if>
    </c:if>

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