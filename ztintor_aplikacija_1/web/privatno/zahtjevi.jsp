<%-- 
    Document   : zahtjevi
    Created on : Jun 18, 2013, 9:50:12 PM
    Author     : zoran
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Zahtjevi</title>
        <link href="../resources/css/default.css" rel="stylesheet" type="text/css" /> 
    </head>
    <body style="background-color: #C5FCC0;">
        <div style=" background-color: #AEE1F2; margin-left: 20%; margin-right: 20%; margin-top: 7px; margin-bottom: 7px;font-size: 20px; ">
            <center>
                <form method="GET" name="forma">
                    <div>
                        <table style="width: 500px;">
                            <th colspan="2">Zahtjevi</th>
                            <br/>
                            <tr><td>Način pretraživanja:</td>
                                <td>
                                    <input type="radio" id="radio_svi" checked="true" name="vrsta" value="1">
                                    <label for="radio_svi">svi</label> 
                                    <input type="radio" id="radio_period" name="vrsta" value="2">
                                    <label for="radio_period">period</label>
                                </td>
                            </tr>
                            <tr><td>Podaci po stranici: </td><td>
                                    <select name="select">
                                        <option value="5">5</option>
                                        <option value="10">10</option>
                                        <option value="20">20</option>
                                        <option value="50">50</option>
                                        <option value="100">100</option>
                                        <option value="0">Svi</option>
                                    </select>
                                </td>

                            </tr>
                            <tr>
                                <td>Početno vrijeme: </td>
                                <td><input type="text" name="pocetno" value="<c:out value="${param.pocetno}"/>"/></td>
                            </tr>
                            <tr>
                                <td>Završno vrijeme: </td>
                                <td><input type="text" name="zavrsno" value="<c:out value="${param.zavrsno}"/>"/></td>
                            </tr>
                            <tr><td colspan="2"><center><input type="submit" value="Prikaz"/></center></td></tr>
                            <br/>
                        </table>
                    </div>
                </form>   
                <sql:setDataSource var="konekcija"
                                   user="${applicationScope.BP_Konfiguracija.user_username}"
                                   password="${applicationScope.BP_Konfiguracija.user_password}"
                                   driver="${applicationScope.BP_Konfiguracija.driver_database}"
                                   url="${applicationScope.BP_Konfiguracija.server_database}${applicationScope.BP_Konfiguracija.user_database}"
                                   />
                <c:if test="${pageContext.request.method=='GET'}">
                    <c:choose>
                        <c:when test="${param.vrsta=='1'}">                       
                            <sql:query dataSource="${konekcija}" var="upit">SELECT * from ztintor_zahtjevi</sql:query>
                        </c:when>
                        <c:when test="${param.vrsta=='2'}">
                            <sql:query dataSource="${konekcija}" var="upit">SELECT * from ztintor_zahtjevi where datum BETWEEN concat(substring('<c:out value="${param.pocetno}"/>',1,13),':',substring('<c:out value="${param.pocetno}"/>',15,2),':',substring('<c:out value="${param.pocetno}"/>',18,2)) AND concat(substring('<c:out value="${param.zavrsno}"/>',1,13),':',substring('<c:out value="${param.zavrsno}"/>',15,2),':',substring('<c:out value="${param.zavrsno}"/>',18,2)) ORDER BY datum ASC</sql:query>
                        </c:when>
                    </c:choose>
                    <display:table name="${upit.rows}" pagesize="${param.select}" style="border:1px solid; text-align:center; background-color: #C5FCC0;">
                        <display:column property="id" title="id" style="border:1px solid; background-color: #DBD872;"/>
                        <display:column property="komanda" title="Komanda" style="border:1px; background-color: #F5F5CE;"/>   
                        <display:column property="datum" title="Datum" style="border:1px solid; background-color: #DBD872;"/>
                        <display:column property="uspjeh" title="Uspjeh" style="border:1px; background-color: #F5F5CE;"/>  
                    </display:table>      
                </c:if>     
            </center>
        </div>
    <center><a href="${pageContext.servletContext.contextPath}/privatno/index.jsp">Index</a><br/></center>
</body>
</html>
