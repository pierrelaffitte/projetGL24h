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
import randomForest.RandomForest;
import utilitaire.Client;

@WebServlet(name="runAjax", urlPatterns={"/RunAjax"})
public class RunAjax extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*Client c = new Client();
		String myFile = request.getParameter("myFile");
		String method = request.getParameter("methode");
		String y = request.getParameter("y");
		response.setContentType("text/html");
		//String greetings = myFile+" "+method+" "+y;
		
		if (method.equals("CT")) {
			System.out.println("CT");
			c.setAlgo(new ClassificationTree());
			String[] otherArgs = {myFile};
			ArrayList<Object> vec = c.run(myFile, ',', y, otherArgs);
			String var = "<UL>\n";
			for (Object res : vec) {
				var += "<LI>Nom: "+ res+"</Li>	\n";
			}
			var += "</UL>\n";
			greetings +="\n"+var;
		}
		if (method.equals("RF")) {
			System.out.println("RF");
			c.setAlgo(new RandomForest());
			String[] otherArgs = {request.getParameter("numTrees")};
			ArrayList<Object> vec = c.run(myFile, ',', y, otherArgs);
			String var = "<UL>\n";
			for (Object res : vec) {
				var += "<LI>Nom: "+ res+"</Li>\n";
			}
			var += "</UL>\n";
			greetings +=otherArgs[0]+"\n"+var;
		}
		c = new Client();
		response.setContentType("text/html");
		response.getWriter().write(greetings);*/
		
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
		
		String var = "<fieldset>\n";
		var += "<legend> Summary of information : </legend>\n";
		var += "<form class=\"reduce\">\n";
		var += "<center><table>\n";
		
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
		var += "</form>\n";
		var += "</fieldset>\n";
		
		response.setContentType("text/html");
		response.getWriter().write(var);;
		
	}
}