<%
	String type=request.getParameter("type");
	if(type.equals("qq"))
	{
		cn.bobdeng.RepostThread.getInstance().qqLogin(request.getParameter("code"),null,request.getParameter("user"));
	}else{
		cn.bobdeng.RepostThread.getInstance().weiboLogin(request.getParameter("code"),null,request.getParameter("user"));
	}
%>
<%
	response.sendRedirect("list.jsp");
%>
