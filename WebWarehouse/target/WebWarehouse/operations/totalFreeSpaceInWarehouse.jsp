<%@ page import="controller.*" %>

<html>
<body>
    <%! wareHouseController wr = new wareHouseController(); %>
    <p>
    <%
    out.println("The free size is " + wr.totalFreeSpaceInWarehouse()[0]);
    %>
    </p>
    <p>
    <%
    out.println("The free weight is " + wr.totalFreeSpaceInWarehouse()[1]);
    %>
    </p>
    <a href="../index.jsp">Back to main menu</a>
</body>
</html>