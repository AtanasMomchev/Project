<%@ page import="controller.*" %>

<p>
  <%! wareHouseController wr = new wareHouseController(); %>
  <%
  int inputStr = Integer.parseInt(request.getParameter("num"));
  %>
</p>
  <html>
   <body>
    <p> The average transaction per day is <%=wr.averageTransactionPerDay(inputStr)%></P>
    <a href="../index.jsp">Back to main menu</a>
   </body>
</html>