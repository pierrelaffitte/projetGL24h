<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Forets.com</title>

<script src="http://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script src="form1.js" type="text/javascript"></script>
</head>
<body>

	<table>
	<tr><td>
	<table>
	<tr><td>
	<form name="formulaire" id="formulaire">
    Choose your data :</br>
    <input type="radio" required="required" name="choix" onclick="afficher('choose');" value ="choose" checked="checked">choose
    <input type="radio" name="choix" onclick="afficher('import');" value="import">import
    <div id="champ1">
      <select id='myFile' name ="myFile" required="required">
        <option disabled selected value> -- select an option -- </option>
        <option value='iris.csv'>iris.csv</option>
        <option value='statsFSEVary.csv' >statsFSEVary.csv</option>
        <option value='mushrooms.csv'>mushrooms.csv</option>
      </select>
    </div> <input type="submit" value="run" id="form1"></input></br>
  </form>
  <script type="text/javascript">
  function afficher(choix)
  {
    if(choix == "choose"){
      document.getElementById("champ1").innerHTML = "<select id='monselect' name='myFile' required='required'><option disabled selected value> -- select an option -- </option>"+
      "<option value='iris.csv' onclick=\"setValue('iris.csv');\">iris.csv</option>"+
      "<option value='statsFSEVary.csv' onclick=\"setValue('statsFSEVary.csv');\">statsFSEVary.csv</option>"+
      "<option value='mushrooms.csv' onclick=\"setValue('mushrooms.csv');\">mushrooms.csv</option></select>";
    }
    if(choix == "import"){
      document.getElementById("champ1").innerHTML ="<input type='file' id='myFile' name='myFile' required='required' accept='.csv'></input>"+
      "</br>Precise your path file : </br>"+
      "<input type='text' name='path'></input>";
    }
  }
  </script></td></tr>
	<tr><td><strong>Formulaire 2</strong>:
	<div id="form2"></div></td></tr>
	</table>
	</td><td> <strong>Results 2</strong>:
	<div id="results"><input id='y' name="y" type="hidden"></div></div>
	</td></tr>
	</table>	
</body>
</html>