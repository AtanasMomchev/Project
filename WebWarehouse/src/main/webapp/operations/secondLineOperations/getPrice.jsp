<%@ page import="dao.ProductsDAO" %>

<p>
  <%! ProductsDAO pd = new ProductsDAO(); %>
  <%
  String name = request.getParameter("name");
  %>
</p>
  <html>
   <body>
    <p> The price is
    <%=
        pd.getProductPrice(name)
    %>
    </p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>