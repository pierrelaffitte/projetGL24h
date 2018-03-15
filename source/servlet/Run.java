package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilitaire.Client;

@WebServlet(name="run", urlPatterns={"/Run"})
public class Run extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String choix = request.getParameter("choix");
		String myFile = request.getParameter("myFile");
		String end = myFile.substring(myFile.length()-4, myFile.length());
		if (end.equals(".csv")) {
			if (choix.equals("import")) {
				Client c = new Client();
				myFile = myFile.replaceAll(".csv", "");
				String path = request.getParameter("path");
				c.importerData(myFile, path);
			}
			/*
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<HTML>\n<BODY>\n" +
					"<H1>Recapitulatif des informations</H1>\n" +
					"<UL>\n" +            
					" <LI>Nom: "
					+ request.getParameter("myFile") + "\n" +
					" <LI>Prenom: "
					+ request.getParameter("choix") + "\n" +
					"</UL>\n" +                
					"</BODY></HTML>");
					*/
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("");
			
		}else {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<HTML>\n<BODY>\n" +
					"<H1>L'import a échoué</H1>\n" +
					"<p>L'extension du fichier"+end+" n'est pas autorisé.</p>" +                
					"</BODY></HTML>");
		}

	}
}