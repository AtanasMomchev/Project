<%@ page import="controller.wareHouseController" %>
<%@ page import="model.Stock" %>
<%@ page import="java.util.ArrayList" %>

<p>
  <%! wareHouseController wc = new wareHouseController(); %>
  <%
  int lot = Integer.parseInt(request.getParameter("lot_id"));
  %>
</p>
  <html>
   <body>
    <p> Removing lot  <%try{
    ArrayList<Stock> lots = new ArrayList<Stock>();
    lots = wc.removeLotAndRearrangeProducts(lot);
    out.print(String.format(" succeeded!\n"));
    for(int i=0;i<lots.size();i++){
        out.print(String.format("%d %s imported in lot number %d.\n",lots.get(i).getQuantity(),lots.get(i).getProduct_name(),lots.get(i).getLot_id()));
        }
    }catch(Exception e){
    out.print(" failed\n" + e);
    }%></p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>