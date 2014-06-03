<%-- 
    Document   : errPage
    Created on : Jun 18, 2013, 10:12:42 PM
    Author     : zoran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Greška</title>
    </head>
    <body style="background-color: #AEE1F2;">
    <center>
        <h1>Greška pri logiranju</h1>
        <a href="${pageContext.servletContext.contextPath}/jsp/login.jsp">Pokušaj ponovno</a>
    </center>
</body>
</html>
