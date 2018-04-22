<%@ page import="controller.*" %>

<html>
<body>
    <p>
    <%! wareHouseController wr = new wareHouseController(); %>
      <%
      out.println("The total taken size is " + wr.totalTakenSpaceInWarehouse()[0]);
      %>
    </p>
    <p>
      <%
      out.println("The total taken weight is " + wr.totalTakenSpaceInWarehouse()[1]);
      %>
    </p>
    <a href="../index.jsp">Back to main menu</a>
</body>
</html>