<META http-equiv="Content-Type" content="text/html; charset=euc-kr" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="false"%>
<%@page import="korbit.tran.util.*"%>
<jsp:include page="menu.jsp" flush="false" />
<html>
<head>
<title>Home</title>
</head>
<script type="text/javascript">
	function change(currency_pair) {
		location.href = "listOrdersOpen?currency_pair=" + currency_pair;
	}
	
	function ordersCancel(currency_pair,id) {
		var loc = "ordersCancel?currency_pair=" + currency_pair + "&id=" + id;
		//alert(loc);
		location.href = loc;
		//alert(location.href);
	}
</script>


<body>
	<h1>OrdersOpen</h1>
	<select name="currency_pair" id="currency_pair"
		onchange="change(this.value);">
		<option value="eth_krw"
			<c:if test="${currency_pair eq 'eth_krw'}">selected</c:if> >eth_krw</option>
		<option value="etc_krw" <c:if test="${currency_pair eq 'etc_krw'}">selected</c:if> >etc_krw</option>
	</select>

	<table border="1">
		<tr>
			<td width="100">id</td>
			<td width="50">native_total</td>
			<td width="50">open</td>
			<td width="100">price</td>
			<td width="100">timestamp</td>
			<td width="50">total</td>
			<td width="50">type</td>
			<td width="50">취소</td>
			<c:forEach var="sub" items="${ordersOpenList}">
				<tr>
					<td>${sub.id}</td>
					<td><fmt:formatNumber value="${sub.native_total.value}"
							pattern="###" /></td>
					<td>${sub.open.value}</td>
					<td align="right"><fmt:formatNumber value="${sub.price.value}"
							pattern="###,###" /></td>
					<td>${sub.dateTime}</td>
					<td>${sub.total.value}</td>
					<td>${sub.type}</td>
					<td><a href="javascript:ordersCancel('${currency_pair}','${sub.id}');">${sub.id}</a></td>
				</tr>
			</c:forEach>
	</table>

</body>

</html>