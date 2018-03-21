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

@WebServlet(name="info", urlPatterns={"/Info"})
public class Info extends HttpServlet {

	/**
	 * Retourne les noms des colonnes pour un fichier csv
	 * @param path chemin du fichier
	 * @param name nom du fichier
	 * @param delimiter délimiteur du fichier
	 * @return liste des noms de colonnes
	 * @throws IOException exception de lecture
	 */
	public ArrayList<String> getColnames(String path, String name, char delimiter) throws IOException{
		Scan s = new Scan();
		return s.read(path, name, delimiter);
	}
	
	/**
	 * Modification de la liste des variables
	 * @param myFile nom du fichier csv
	 * @return code html mis à jour
	 * @throws IOException exception de lecture
	 */
	public String setY(String myFile) throws IOException {
		ArrayList<String> head = getColnames("resources/", myFile, ',');
		String res = "</br> Select your target variable : </br>"
				+ "<select name='y'>";
		for (String col : head) {
			if(! col.equals("")){
				res += "<option value="+col+">"+col+"</option>";
			}
		}
		res += "</select></br></br>";
		return res;
	}
	
	@Override
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
					"<head>\n" + 
					"	<meta charset=\"utf-8\" name=\"author\" content=\"Pierre Laffite\">\n" + 
					"	<link rel=\"stylesheet\" href=\"style.css\" />\n"+
					"	<title>Les Forets.com</title>\n" + 
					"</head>\n" + 
					
					"<body>\n" + 
					"	<div class=\"entete\">\n" +
				    "		<img src=\"image.jpg\" id=\"logo\">\n" +
				    "		<img src=\"image.jpg\" id=\"logo2\">\n" +
				    "		<h1> Les forêts.com </h1>\n" +
				    "		<h2> Compare and model smart </h2>\n" +
				    "	</div>\n" +
				    "	<div class=\"center-on-page\">\n" + 
				    "		<fieldset>\n" + 
				    "	 	 	<legend> Select a method of Machine Learning and its arguments : </legend>\n"+
					"  			<form name=\"formulaire\" id=\"formulaire\" action=\"/Run\" method=\"POST\">\n" + 
					"    			Selected file : <input type='text' name='myFile' value='"+myFile+"'/></br>"+
					" 				</br>\n"+
					"    			Select your method of Machine Learning :</br>\n" + 
					"    			<input type=\"radio\" name=\"methode\" value=\"CT\" cheched=\"checked\" required=\"required\" onclick=\"selectNumOfTrees('CT')\"> Classification Tree<br>\n" + 
					"    			<input type=\"radio\" name=\"methode\" value=\"RF\" onclick=\"selectNumOfTrees('RF')\" > Random Forest\n" + 
					"				</br>\n" +		
					"    			<div id=\"pritNumTrees\"></div>\n" + var+
					"    				<input type=\"submit\" value=\"run\"></input></br>\n" + 
					"  				</div>\n" +
					"  			</form>\n" +	
					" 		</fieldset>\n" + 
					"	</div>\n" +
					
					"  <script type=\"text/javascript\">\n" + 
					"  function selectNumOfTrees(choix){\n" + 
					"    if(choix == \"RF\"){\n" + 
					"      document.getElementById(\"pritNumTrees\").innerHTML = \"</br> Insert the number of trees to do the random forest :</br>\"+\n" + 
					"      \"<input type='number' id='numTrees' name='numTrees' value='10' min='1' max='100'></input>\";\n" + 
					"    }\n" + 
					"    if (choix == \"CT\"){\n" + 
					"        document.getElementById(\"pritNumTrees\").innerHTML = \"\";\n" + 
					"    }\n" + 
					"  }\n" + 
					"  </script>\n" + 
					"  </body>\n" + 
					"  <footer>\n" + 
					"    <br>\n" + 
					"  </footer>\n" +
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