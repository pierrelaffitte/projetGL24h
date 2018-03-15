package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classificationTree.ClassificationTree;
import utilitaire.Client;

@WebServlet(name="run", urlPatterns={"/Run"})
public class Run extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Client c = new Client();
		String myFile = request.getParameter("myFile");
		String method = request.getParameter("methode");
		String y = request.getParameter("y");
		if (method.equals("CT")) {
			c.setAlgo(new ClassificationTree());
			String[] otherArgs = {myFile};
			ArrayList<Object> vec = c.run(myFile, ',', y, otherArgs);
			String var = "<UL>\n";
			for (Object res : vec) {
				var += "<LI>Nom: "+ res+"\n";
			}
			var += "<UL>\n";
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<HTML>\n<BODY>\n" +
					"<H1>Recapitulatif des informations</H1>\n" +
					var+                
					"</BODY></HTML>");
		}
		if (method.equals("RF")) {
			c.setAlgo(new ClassificationTree());
			String[] otherArgs = {request.getParameter("numTrees")};
			ArrayList<Object> vec = c.run(myFile, ',', y, otherArgs);
			String var = "<UL>\n";
			for (Object res : vec) {
				var += "<LI>Nom: "+ res+"\n";
			}
			var += "<UL>\n";
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<HTML>\n<BODY>\n" +
					"<H1>Recapitulatif des informations</H1>\n" +
					var+                
					"</BODY></HTML>");
		}
	}
}