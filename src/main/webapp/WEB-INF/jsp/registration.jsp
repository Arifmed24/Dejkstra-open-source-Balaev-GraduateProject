<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="icon" href="/img/train.png">
    <title>Registration</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.7/css/materialize.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.7/js/materialize.min.js"></script>
    <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="/resources/css/materialize.min.css" media="screen,projection"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body>
<div class="row"></div>
<div class="row"></div>
<div class="row"></div>
<div class="container z-depth-3">
    <div class="card-panel teal lime">
        <form action="/registration" method="post">
            <h3 class="center-align white-text">REGISTRATION</h3>
            <p class="teal-text lighten-2"><b>Login</b></p>

            <c:choose>
                <c:when test="${!empty errorLog}">
                    <input type="text" name="login">
                    <span class="red-text"><b><i><sup><c:out value="${errorLog}"/></sup></i></b></span>
                </c:when>
                <c:otherwise>
                    <input type="text" name="login">
                </c:otherwise>
            </c:choose>

            <p class="teal-text lighten-2"><b>Password</b></p>
            <c:choose>
                <c:when test="${!empty errorPass}">
                    <input type="password" name="password">
                    <span class="red-text"><b><i><sup><c:out value="${errorPass}"/></sup></i></b></span>
                </c:when>
                <c:otherwise>
                    <input type="password" name="password">
                </c:otherwise>
            </c:choose>
            <p class="teal-text lighten-2"><b>Full name</b></p>
            <c:choose>
                <c:when test="${!empty errorName}">
                    <input type="text" name="fullname">
                    <span class="red-text"><b><i><sup><c:out value="${errorName}"/></sup></i></b></span>
                </c:when>
                <c:otherwise>
                    <input type="text" name="fullname">
                </c:otherwise>
            </c:choose>
            <div class="row">
                <div class="col s6">
                    <button type="submit" class="btn waves-light waves-effect">
                        Register
                        <i class="material-icons right">add</i>
                    </button>
                </div>
                <div class="col s6 right-align">
                    <a href="/login" class="btn waves-effect waves-light">
                        Login
                        <i class="material-icons right">send</i>
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script type="text/javascript" src="/js/materialize.min.js"></script>
</body>
</html>
