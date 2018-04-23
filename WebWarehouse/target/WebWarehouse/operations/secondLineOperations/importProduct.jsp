<%@ page import="controller.wareHouseController" %>
<%@ page import="model.Stock" %>
<%@ page import="java.util.ArrayList" %>

<p>
  <%! wareHouseController wc = new wareHouseController(); %>
  <%
  String name = request.getParameter("name");
  int quantity = Integer.parseInt(request.getParameter("quantity"));
  %>
</p>
  <html>
   <body>
    <p> Import  <%try{
    ArrayList<Stock> lots = new ArrayList<Stock>();
    lots = wc.importProduct(name,quantity);
    out.print(String.format(" succeeded!\n"));
    for(int i=0;i<lots.size();i++){
        out.print(String.format("%d %s input in lot number %d.\n",lots.get(i).getQuantity(),lots.get(i).getProduct_name(),lots.get(i).getLot_id()));
        }
    }catch(Exception e){
    out.print(" failed\n" + e);
    }%></p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>