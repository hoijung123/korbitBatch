<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<jsp:include page="tran/menu.jsp" flush="false"/>

<P>  The time on the server is ${serverTime}. </P>

<a href="resources/line.html">line.html</a>
</body>
</html>


