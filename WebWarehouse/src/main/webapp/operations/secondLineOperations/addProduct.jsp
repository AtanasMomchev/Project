<%@ page import="dao.ProductsDAO" %>

<p>
  <%! ProductsDAO pd = new ProductsDAO(); %>
  <%
  String name = request.getParameter("name");
  int size = Integer.parseInt(request.getParameter("size"));
  double weight = Double.parseDouble(request.getParameter("weight"));
  double price = Double.parseDouble(request.getParameter("price"));
  %>
</p>
  <html>
   <body>
    <p> Import was <%try{
    pd.setProduct(name,size,weight,price);
    out.print(" succeeded!");
    }catch(Exception e){
    out.print(" failed. " + e);
    }%></p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>