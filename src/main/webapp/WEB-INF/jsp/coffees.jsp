<%@ page contentType="text/html; charset = UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>CoffeeList</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/coffees.css" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/coffees.js" defer></script>
</head>
<body>
    <div id="coffeeList" class="pages">
        <div id="basket"></div>
        <div id="coffees"></div>
    </div>
    <div id="order" class="pages"></div>
    <div id="confirmOrder" class="pages"></div>
    <div id="congratulation" class="pages"></div>
    <div id="login" class="pages"></div>
    <div id="admin" class="pages"></div>
    <div id="orderDetails" class="pages"></div>
</body>
</html>