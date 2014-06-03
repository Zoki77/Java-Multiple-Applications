<%-- 
    Document   : login
    Created on : Jun 18, 2013, 9:22:59 PM
    Author     : zoran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aplikacija 1 - Prijava</title>
        <link href="../resources/css/default.css" rel="stylesheet" type="text/css" /> 
    </head>
    <body  style="background-color: #AEE1F2;">
    <center>
        <h1>Aplikacija 1 - Prijava</h1>
        <form method="POST" 
              action="${pageContext.servletContext.contextPath}/ProvjeraKorisnika" style=" background-color: #EDF2AE; margin-left: 35%; margin-right: 35%;">
            Korisničko ime: <input name="kor_ime"> <br>
            Lozinka: <input type="password" name="lozinka"> <br>
            <input type="submit" value="Prijava">
        </form>
    </center>
</body>
</html>
