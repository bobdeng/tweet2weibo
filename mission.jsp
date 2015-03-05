<p>mission running</p>
<%
	java.util.Collection<cn.bobdeng.Twitter> missions=cn.bobdeng.RepostThread.getInstance().getMission();
	for(cn.bobdeng.Twitter t:missions){
%>
		<br><%=t.getName()%> {Twitter: <%=t.getUser()%> to: <%=t.getWeibo().getName()%>} 
		<a href="add.jsp?type=<%=request.getParameter("type")%>&code=<%=request.getParameter("code")%>&id=<%=t.getName()%>&user=<%=t.getUser()%>">update</a>
<%
	}
%>
<br>
<input type=text value="" id="edtUser">
<a href="javascript:void(0)" onclick="newMission();">new mission</a>
<script type="text/javascript">
function newMission(){
	var user=document.getElementById("edtUser").value;
	document.location.href="add.jsp?type=<%=request.getParameter("type")%>&code=<%=request.getParameter("code")%>&user="+user;
}
</script>