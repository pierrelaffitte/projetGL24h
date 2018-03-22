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

@WebServlet(name="run2", urlPatterns={"/Run2"})
public class RunAjax extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Client c = new Client();
		String myFile = request.getParameter("myFile");
		String method = request.getParameter("methode");
		String y = request.getParameter("y");
		response.setContentType("text/html");
		String greetings = myFile+" "+method+" "+y;
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
		response.getWriter().write(greetings);
		
	}
}