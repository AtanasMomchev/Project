<%@ page import="controller.*" %>

<p>
  <%! wareHouseController wr = new wareHouseController(); %>
  <%
  int inputStr = Integer.parseInt(request.getParameter("num"));
  %>
</p>
  <html>
   <body>
    <p> The turnover cash is <%=wr.turnoverPriceAndWeight(inputStr)[0]%></p>
     <p> The turnover weight is <%=wr.turnoverPriceAndWeight(inputStr)[1]%></p>
    <a href="../index.jsp">Back to main menu</a>
   </body>
</html>