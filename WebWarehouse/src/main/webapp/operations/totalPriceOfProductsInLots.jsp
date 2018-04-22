<%@ page import="controller.*" %>

<html>
<body>
    <p>
      <%! wareHouseController wr = new wareHouseController(); %>
      <%
      out.println("The total price of all products in the Warehouse stock is  " + wr.totalPriceOfProductsInLots());
      %>
    </p>
    <a href="../index.jsp">Back to main menu</a>
</body>
</html>