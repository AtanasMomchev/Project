<%@ page import="controller.*" %>

<html>
<body>
<%! wareHouseController wr = new wareHouseController(); %>
<%out.print(wr.get("eggs"));%>
</body>
</html>