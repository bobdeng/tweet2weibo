package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class mission_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<p>mission running</p>\n");

	java.util.Collection<cn.bobdeng.Twitter> missions=cn.bobdeng.RepostThread.getInstance().getMission();
	for(cn.bobdeng.Twitter t:missions){

      out.write("\n");
      out.write("\t\t<br>");
      out.print(t.getName());
      out.write(" {Twitter: ");
      out.print(t.getUser());
      out.write(" to: ");
      out.print(t.getWeibo().getName());
      out.write("} \n");
      out.write("\t\t<a href=\"add.jsp?type=");
      out.print(request.getParameter("type"));
      out.write("&code=");
      out.print(request.getParameter("code"));
      out.write("&id=");
      out.print(t.getName());
      out.write("&user=");
      out.print(t.getUser());
      out.write("\">update</a>\n");

	}

      out.write("\n");
      out.write("<br>\n");
      out.write("<input type=text value=\"\" id=\"edtUser\">\n");
      out.write("<a href=\"javascript:void(0)\" onclick=\"newMission();\">new mission</a>\n");
      out.write("<script type=\"text/javascript\">\n");
      out.write("function newMission(){\n");
      out.write("\tvar user=document.getElementById(\"edtUser\").value;\n");
      out.write("\tdocument.location.href=\"add.jsp?type=");
      out.print(request.getParameter("type"));
      out.write("&code=");
      out.print(request.getParameter("code"));
      out.write("&user=\"+user;\n");
      out.write("}\n");
      out.write("</script>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else log(t.getMessage(), t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
