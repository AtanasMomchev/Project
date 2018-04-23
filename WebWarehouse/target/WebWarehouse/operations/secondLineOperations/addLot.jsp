<%@ page import="dao.LotsDAO" %>

<p>
  <%! LotsDAO ld = new LotsDAO(); %>
  <%
  int size = Integer.parseInt(request.getParameter("size"));
  double weight = Double.parseDouble(request.getParameter("weight"));
  %>
</p>
  <html>
   <body>
    <p> Import was <%try{
    ld.setLot(size,weight);
    out.print(" succeeded!");
    }catch(Exception e){
    out.print(" failed. " + e);
    }%></p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>