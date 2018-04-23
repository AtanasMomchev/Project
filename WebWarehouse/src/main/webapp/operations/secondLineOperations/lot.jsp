<%@ page import="controller.*" %>

<p>
  <%! wareHouseController wr = new wareHouseController(); %>
  <%
  int inputStr = Integer.parseInt(request.getParameter("num"));
  %>
</p>
  <html>
   <body>
     <p> Products and their quantity</p>
     <p>
        <%
         for (String[] s : wr.productsAndQuantity(inputStr))
         out.println(s[0] + " " + s[1] + "<br/>");
         %>
     </p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>