<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>
<%@ page pageEncoding="UTF-8" %>

<html>

<c:set var="title" value="Добавить товар"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<div id="container">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="sidebar">
        <h2><a href="?command=viewSettings">Settings</a></h2>
        <h2><a href="?command=logout">Log out</a></h2>
    </div>
    <div id="content">

        <h2>Add new PRODUCT</h2>

        <div id="add_product_form">
            <form action="controller" method="post">
                <input type="hidden" name="command" value="addProduct"/>
                <input type="text" placeholder="Enter product name at Russian" name="name_ru" required>
                <input type="text" placeholder="Enter product name at English" name="name_en" required>
                <input type="text" placeholder="Enter product code" name="code" required>
                <input type="text" placeholder="Enter product price" name="price" required>
                <input type="text" placeholder="Enter product amount" name="amount" required>
                <input type="text" placeholder="Enter product weight" name="weight" required>
                <textarea rows="5" cols="25" name="description_ru" placeholder="Enter russian description of product">
                </textarea>
                <textarea rows="5" cols="25" name="description_en" placeholder="Enter english description of product">
                </textarea>

                <input type="password" placeholder="Enter Password" name="password" required>
                </label>
                <hr>
                <button type="submit" class="login_btn" name="submit"><fmt:message
                        key="login_jsp.button.login"/></button>
            </form>
        </div>

    </div>
    <div id="clear">

    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>

</body>

</html>