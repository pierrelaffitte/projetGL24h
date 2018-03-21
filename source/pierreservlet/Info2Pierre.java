package pierreservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import servlet.Scan;
import utilitaire.Client;

@WebServlet("/Info2Pierre")
public class Info2Pierre extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ArrayList<String> getColnames(String path, String name, char delimiter) throws IOException{
		Scan s = new Scan();
		return s.read(path, name, delimiter);
	}

	public String setY(String myFile) throws IOException {
		ArrayList<String> head = getColnames("resources/", myFile, ',');
		String res = "Select your target variable : </br>"
				+ "<select name='y' id='y'>";
		for (String col : head) {
			if(! col.equals("")){
				res += "<option value="+col+">"+col+"</option>";
			}
		}
		res += "</select></br>";
		return res;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String greetings = "";
		String myFile = request.getParameter("myFile");
		String end = myFile.substring(myFile.length()-4, myFile.length());
		if (end.equals(".csv")) {
			myFile = myFile.replaceAll(".csv", "");
			String var = setY(myFile);		
			greetings = "  <form name=\"formulaire2\" id=\"formulaire2\">\n" + 
					"    <input type='text' name='myFile' value='"+myFile+"'/ disabled=\"disabled\" id=\"monFichierNonModifiable\"></br>"+
					"    Select your method of Machine Learning :\n" + 
					"    </br>\n" + 
					"    <input type=\"radio\" name=\"methode\" value=\"CT\" cheched=\"checked\" required=\"required\" onclick=\"selectNumOfTrees('CT')\"> Classification Tree<br>\n" + 
					"    <input type=\"radio\" name=\"methode\" value=\"RF\" onclick=\"selectNumOfTrees('RF')\" > Random Forest\n" + 
					"    <div id=\"pritNumTrees\"></div>\n" +
					var+
					"    <button value=\"run\" id=\"boutonForm2\" type=\"button\" onclick='runModels()'>RunModel</button></br>\n" + 
					"  </form>\n" +					
					"  <script type=\"text/javascript\">\n" + 
					"  \n" + 
					"  function selectNumOfTrees(choix){\n" + 
					"    if(choix == \"RF\"){\n" + 
					"      document.getElementById(\"pritNumTrees\").innerHTML = \"insert the number of trees to do the random forest :</br>\"+\n" + 
					"      \"<input type='number' id='numTrees' name='numTrees' value='10' min='1' max='100'></input>\";\n" + 
					"    }\n" + 
					"    if (choix == \"CT\"){\n" + 
					"        document.getElementById(\"pritNumTrees\").innerHTML = \"\";\n" + 
					"    }\n" + 
					"  };\n" + 
					"  function runModels(){\n" + 
					"    alert('les modeles');\n" + 
					"   	var monfichier = $('#monFichierNonModifiable').val();\n" + 
					"    var mony = $('#y').val();\n" + 
					"    var mamethode = $('input[name=methode]:checked', '#formulaire2').val();\n" + 
					"    alert(mamethode);\n" + 
					"    if (mamethode == \"CT\"){\n" + 
					"      $.get('Run2', {\n" + 
					"        myFile : monfichier,\n" + 
					"        y : mony,\n" + 
					"        methode : mamethode\n" + 
					"      }, function(responseText) {\n" + 
					"        $('#results').html(responseText);\n" + 
					"        });\n" + 
					"    }\n" + 
					"    if (mamethode == \"RF\"){\n" + 
					"      var nbTrees = $(\"#numTrees\").val();\n" + 
					"      $.get('Run2', {\n" + 
					"        myFile : monfichier,\n" + 
					"        y : mony,\n" + 
					"        methode : mamethode,\n" + 
					"        numTrees: nbTrees,\n" + 
					"      }, function(responseText) {\n" + 
					"        $('#results').html(responseText);\n" + 
					"        });\n" + 
					"    }\n" + 
					"	}\n" + 
					"" + 
					"</script>";
		}else {
			greetings = "<H1>L'import a échoué</H1>\n" +
					"<p>L'extension du fichier"+end+" n'est pas autorisé.</p>" ;
		}
		response.setContentType("text/html");
		response.getWriter().write(greetings);
	}



}