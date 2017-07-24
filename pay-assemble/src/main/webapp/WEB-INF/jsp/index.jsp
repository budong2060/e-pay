<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html lang="en">

<body>
    <h1>${message}</h1>

    <spring:url value="/test.txt" htmlEscape="true" var="springUrl" />

    <p><a href="${springUrl}">URL generated by Spring</a></p>
    <p><a href="${url}">URL generated by JSTL</a></p>
</body>

</html>