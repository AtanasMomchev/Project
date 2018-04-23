 <%@ page import="dao.*" %>
 <%! StockDAO sd = new StockDAO(); %>
  <%
  String inputStr = request.getParameter("name");
  %>
</p>

 <html>
   <body>
     <p> Total quantity</p>
     <p>
        <%
        out.println(sd.productQuantityInStock(inputStr));
        %>
     </p>
     <p>
     <p> Quantity by lots</p>
        <%
        for (Integer i : sd.getLotsIdsFromStockSet())
            out.println(sd.getQuantityOfProductInOneLot(i, inputStr) + " in lot with Id " + i + "<br/>");
         %>
      </p>
    <a href="../../index.jsp">Back to main menu</a>
   </body>
</html>