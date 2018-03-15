package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilitaire.Client;

@WebServlet(name="info2", urlPatterns={"/Info2"})
public class Info2 extends HttpServlet {

	public ArrayList<String> getColnames(String path, String name, char delimiter) throws IOException{
		Scan s = new Scan();
		return s.read(path, name, delimiter);
	}
	
	public String setY(String myFile) throws IOException {
		ArrayList<String> head = getColnames("resources/", myFile, ',');
		String res = "Select your target variable : </br>"
				+ "<select name='y'>";
		for (String col : head) {
			res += "<option value="+col+">"+col+"</option>";
		}
		res += "</select></br>";
		return res;
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String choix = request.getParameter("choix");
		String myFile = request.getParameter("myFile");
		String end = myFile.substring(myFile.length()-4, myFile.length());
		if (end.equals(".csv")) {
			myFile = myFile.replaceAll(".csv", "");
			if (choix.equals("import")) {
				Client c = new Client();
				String path = request.getParameter("path");
				c.importerData(myFile, path);
			}
			String var = setY(myFile);		
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>\n" + 
					"  <head>\n" + 
					"    <meta name=\"author\" content=\"Pierre Laffite\">\n" + 
					"    <title>Les Forets.com</title>\n" + 
					"  </head>\n" + 
					"  <body>\n" + 
					"  <form name=\"formulaire\" id=\"formulaire\" action=\"/Run\" method=\"POST\">\n" + 
					"    <input type='text' value='"+myFile+"' disabled=\"disabled\" ></input></br>"+
					"    Select your method of Machine Learning :\n" + 
					"    </br>\n" + 
					"    <input type=\"radio\" name=\"Methode\" value=\"CT\" cheched=\"checked\" required=\"required\" onclick=\"selectNumOfTrees('CT')\"> Classification Tree<br>\n" + 
					"    <input type=\"radio\" name=\"Methode\" value=\"RF\" onclick=\"selectNumOfTrees('RF')\" > Random Forest\n" + 
					"    <div id=\"pritNumTrees\"></div>\n" +
					var+
					"    <input type=\"submit\" value=\"run\"></input></br>\n" + 
					"    <div id=\"monchoix\"><div>\n" + 
					"  </form>\n" +					
					"  <script type=\"text/javascript\">\n" + 
					"  function selectNumOfTrees(choix){\n" + 
					"    if(choix == \"RF\"){\n" + 
					"      document.getElementById(\"pritNumTrees\").innerHTML = \"insert the number of trees to do the random forest :</br>\"+\n" + 
					"      \"<input type='number' id='numTrees' name='numTrees' value='10' min='1' max='100'></input>\";\n" + 
					"    }\n" + 
					"    if (choix == \"CT\"){\n" + 
					"        document.getElementById(\"pritNumTrees\").innerHTML = \"\";\n" + 
					"    }\n" + 
					"  }\n" + 
					"  </script>\n" + 
					"  </body>\n" + 
					"</html>\n");
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