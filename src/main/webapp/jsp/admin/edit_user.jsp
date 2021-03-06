<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>

<html>
<head>
    <link href="<c:url value="/css/main.css" />" rel="stylesheet">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>user editor</title>

    <%@include file="/WEB-INF/jspf/locale.jsp" %>

</head>

<c:set var="userRole" scope="session" value="${user.role}"/>
<c:if test="${userRole != 'admin'}">
    <c:redirect url="/mainServlet?command=show_main_page"/>
</c:if>

<body style="font-family: Arial, Helvetica, sans-serif">

<%@include file="/WEB-INF/jspf/header.jsp" %>
<%@include file="/WEB-INF/jspf/admin_menu.jsp" %>

<br>

<table style="width: 90%; margin-left: auto; margin-right: auto;">
    <tr>
        <td style="text-align: center">
            <h2>Редактор пользователя</h2>
        </td>
    </tr>
</table>

<table style="width: 90%; margin-left: auto; margin-right: auto;">
    <tr style="text-align: center">
        <td style="width: 20%">${name}</td>
        <td style="width: 20%">${surname}</td>
        <td style="width: 20%">${login}</td>
        <td style="width: 15%">email</td>
        <td style="width: 10%">role</td>
        <td style="width: 10%">status</td>
        <td style="width: 5%">points</td>
    </tr>
    <tr style="text-align: center">
        <td>
            <input type="text" id="userName" name="submit_name" value="${userInfo.name}"><br>
        </td>
        <td>
            <input type="text" id="userSurname" name="submit_surname" value="${userInfo.surname}"><br>
        </td>
        <td>
            <input type="text" id="userLogin" name="submit_login" value="${userInfo.login}">
        </td>
        <td>
            <input type="text" id="userEmail" name="submit_email" value="${userInfo.email}">
        </td>
        <td>
            <select id="roleSelector" name="submit_role" size="1">
                <option value="${userInfo.role}" selected disabled hidden>${userInfo.role}</option>
                <option value="admin">admin</option>
                <option value="user">user</option>
            </select>
        </td>
        <td>
            <select id="statusSelector" name="submit_status" size="1">
                <option value="${userInfo.status}" selected disabled hidden>${userInfo.status}</option>
                <option value="active">active</option>
                <option value="deleted">deleted</option>
            </select>
        </td>
        <td>
            <input type="text" id="userPoints" name="submit_points" value="${userInfo.points}">
        </td>
    </tr>
    <tr style="text-align: center">
        <td>
            <c:set var="message" value="${messages['nameResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
        <td>
            <c:set var="message" value="${messages['surnameResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
        <td>
            <c:set var="message" value="${messages['loginResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
        <td>
            <c:set var="message" value="${messages['emailResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
        <td>
            <c:set var="message" value="${messages['roleResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
        <td>
            <c:set var="message" value="${messages['statusResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
        <td>
            <c:set var="message" value="${messages['pointsResult']}"/>
            <%@include file="/WEB-INF/jspf/error_handler.jsp" %>
        </td>
    </tr>
</table>

<form id="updateUser" action="${absolutePath}/mainServlet" method="get">
    <input type="hidden" name="command" value="update_user">
    <input type="hidden" id="submit_id" name="submit_id" value="${userInfo.id}">
    <input type="hidden" id="submit_name" name="submit_name" value="">
    <input type="hidden" id="submit_surname" name="submit_surname" value="">
    <input type="hidden" id="submit_login" name="submit_login" value="">
    <input type="hidden" id="submit_password" name="submit_password" value="default">
    <input type="hidden" id="submit_email" name="submit_email" value="">
    <input type="hidden" id="submit_role" name="submit_role" value="">
    <input type="hidden" id="submit_status" name="submit_status" value="">
    <input type="hidden" id="submit_points" name="submit_points" value="">
</form>

<table style="width: 90%; margin-left: auto; margin-right: auto;">
    <tr style="text-align: right">
        <td>
            <input type="button" value="${updateButton}" onclick="submitButton('updateUser')" style="width: 200px">
        </td>
    </tr>
</table>

<script>

    function submitButton(formName) {
        var userName = document.getElementById("userName").value;
        var userSurname = document.getElementById("userSurname").value;
        var userLogin = document.getElementById("userLogin").value;
        var userEmail = document.getElementById("userEmail").value;
        var userRole = document.getElementById("roleSelector").value;
        var userStatus = document.getElementById("statusSelector").value;
        var userPoints = document.getElementById("userPoints").value;

        document.getElementById("submit_name").value = userName;
        document.getElementById("submit_surname").value = userSurname;
        document.getElementById("submit_login").value = userLogin;
        document.getElementById("submit_email").value = userEmail;
        document.getElementById("submit_role").value = userRole;
        document.getElementById("submit_status").value = userStatus;
        document.getElementById("submit_points").value = userPoints;

        if (userName != "" && userSurname != "" && userLogin != "" &&
            userEmail != "" && userRole != "" && userStatus != "" && userPoints != "") {

            document.getElementById(formName).submit();
        }
    }

</script>

</body>
</html>
