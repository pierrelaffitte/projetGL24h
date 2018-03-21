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
		// Définition du client
		Client c = new Client();
		String myFile = request.getParameter("myFile");
		String method = request.getParameter("methode");
		String y = request.getParameter("y");
		ArrayList<Object> vec = new ArrayList<Object>();
		
		// Exécution de la méthode de ML choisie avec les arguments
		if (method.equals("CT")) {
			c.setAlgo(new ClassificationTree());
			String[] otherArgs = {myFile};
			vec = c.run(myFile, ',', y, otherArgs);
		}
		if (method.equals("RF")) {
			c.setAlgo(new ClassificationTree());
			String[] otherArgs = {request.getParameter("numTrees")};
			vec = c.run(myFile, ',', y, otherArgs);
		}
		
		String var = "<center><table>\n";
		
		// Récupère les accuracy et formate les affichages
		Double sp = Double.valueOf(vec.get(0).toString());
		Double wk = Double.valueOf(vec.get(1).toString());
		Double rj = Double.valueOf(vec.get(2).toString());
		
		String sp_val = String.valueOf(Math.round(sp*100*100.0)/100.0) + "%";
		String wk_val = String.valueOf(Math.round(wk*100*100.0)/100.0 + "%");
		String rj_val = String.valueOf(Math.round(rj*100*100.0)/100.0 + "%");
		
		// Variable d'affichage
		var += "<tr><th>SparML</th><td>";
		var += "<progress value=\"" + sp + "\" max=1></progress></td>\n";
		var += "<td>" + sp_val + "</td></tr>\n";
		var += "<tr><th>Weka</th><td>";
		var += "<progress value=\"" + wk + "\" max=1></progress></td>\n";
		var += "<td>" + wk_val + "</td></tr>\n";
		var += "<tr><th>Renjin</th><td>";
		var += "<progress value=\"" + rj + "\" max=1></progress></td>\n";
		var += "<td>" + rj_val + "</td></tr></table></center>\n";
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>\n"+ 
				"<head>\n" + 
				"   <meta charset=\"utf-8\" name=\"author\" content=\"Pierre Laffite\">\n" + 
				"   <link rel=\"stylesheet\" href=\"style.css\" />\n" + 
				"	<title>Les Forets.com</title>\n" + 
				"</head>\n" +
				"<body>\n" +
				"	<div class=\"entete\">\n" + 
				"   	<img src=\"image.jpg\" id=\"logo\">\n" + 
				"    	<img src=\"image.jpg\" id=\"logo2\">\n" + 
				"    	<h1> Les forêts.com </h1>\n" + 
				"    	<h2> Compare and model smartly </h2>\n" + 
				"  	</div>\n" +
				"	<div id=\"menu\">\n" +
				"		<ul id=\"onglets\">\n" +
				" 			<li><a href=\"form.html\">Main Menu</a></li>\n" +
				"			<li><a href=\"javascript:history.go(-1)\">Model</a></li>\n" +
				"		</ul>\n" +
				"	</div>\n" +
				"  	<div class=\"center-on-page\">\n" + 
				"	  	<fieldset>\n" +
				"			<legend> Summary of information : </legend>\n" +
				"			<form>\n" + var + "</form>\n" +
				"	  	</fieldset>\n" + 
				"  	</div>\n" +
				"</body>\n" +
				"<footer>\n" + 
				"    <br>\n" + 
				"</footer>\n" +
				"</html>");
		
	}
}