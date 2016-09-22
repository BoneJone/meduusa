<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Error 404</title>
<!-- Fontit ja stylesheetit -->
<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Raleway:400,300,600">
<link rel="stylesheet" type="text/css" href="css/normalize.css">
<link rel="stylesheet" type="text/css" href="${url}/css/skeleton.css">
<link rel="stylesheet" type="text/css" href="${url}/css/style.css">
<link rel="stylesheet" type="text/css" href="${url}/css/jquery.toast.min.css">
</head>
<body>
<!-- container alkaa -->
<div class="container">

<!-- virhe ilmoitus div -->
<div class="row virhe">
<div class="twelve columns center">
<h1 lang="fi">404 - Sivua ei löydy</h1>
<br><br>
<a class="button button-primary" href="<c:url value="/"/>">Palaa etusivulle</a>
</div>
</div>


</div>

</body>
</html>