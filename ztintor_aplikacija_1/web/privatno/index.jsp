<%-- 
    Document   : index
    Created on : Jun 18, 2013, 1:31:22 PM
    Author     : zoran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Aplikacija 1 - index</title>
        <link href="../resources/css/default.css" rel="stylesheet" type="text/css" />
    </head>
    <body style="background-color: #C5FCC0;font-size: 20px;text-align:center;">
        <h1>Index</h1>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnika">Dnevnik</a> <br/><br/>
        <a href="${pageContext.servletContext.contextPath}/PregledMeteoPodataka">Meteo podaci</a> <br/><br/>
        <a href="${pageContext.servletContext.contextPath}/PregledZahtjeva">Zahtjevi</a> <br/><br/>
        <a href="${pageContext.servletContext.contextPath}/OdjavaKorisnika">Odjava</a> <br/><br/>
        <a href="${pageContext.servletContext.contextPath}/PrijavaKorisnika">Nazad na prijavu</a> <br/>
        <a href="${pageContext.servletContext.contextPath}/Dokumentacija">Dokumentacija</a> <br/>
    </body>
</html>
