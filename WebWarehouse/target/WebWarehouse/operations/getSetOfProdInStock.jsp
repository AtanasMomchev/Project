<%@ page import="dao.*" %>

<html>
<body>
    <%! StockDAO sd = new StockDAO(); %>
    <p> List of all available products: </p>
    <p>
    <%
        for (String s : sd.getSetOfProdInStock())
            out.println(s + "<br/>");
    %>
    </p>
    <a href="../index.jsp">Back to main menu</a>
</body>
</html>