<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<p>mission running</p>
<%
	java.util.Collection<cn.bobdeng.Twitter> missions=cn.bobdeng.RepostThread.getInstance().getMission();
	for(cn.bobdeng.Twitter t:missions){
%>
		<br><%=t.getName()%> {Twitter: <%=t.getUser()%> to: <%=t.getWeibo().getName()%>} 
		
<%
	}
%>
<a href ="https://api.weibo.com/oauth2/authorize?client_id=1266993502&response_type=code&redirect_uri=http://bobdeng.cn/twitter/weibo.jsp">login weibo</a>
<a href ="https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101197156&redirect_uri=http://bobdeng.cn/twitter/qq.jsp&state=1">login qq</a>
