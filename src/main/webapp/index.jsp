<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="style.css" />
		<title>Forets.com</title>
		<script src="http://code.jquery.com/jquery-1.10.2.js"
			type="text/javascript"></script>
	</head>
	<body>
		<div class="entete">
	    <img src="image.jpg" id="logo">
	    <img src="image.jpg" id="logo2">
	    <h1> Les forêts.com </h1>
	    <h2> Compare and model smartly </h2>
	  </div>
		<table class="on-left">
			<tr><td>
				<fieldset>
			  	<legend> Select a dataset or import a new file : </legend>
				  <form name="formulaire1" id="formulaire1">
						<div class="style-radio">
							Choose your data :
							<input type="radio" required="required" name="choix" onclick="afficher('choose');" value ="choose" checked="checked">Choose
							<input type="radio" name="choix" onclick="afficher('import');" value="import">Import
						</div>
						<div></br></div>
						<div id="champ1">
							<select id='myFile' name ="myFile" required="required">
								<option disabled selected value> -- select an option -- </option>
								<option value='iris.csv'>iris.csv</option>
								<option value='statsFSEVary.csv' >statsFSEVary.csv</option>
								<option value='mushrooms.csv'>mushrooms.csv</option>
							</select>
						</div>
						</br>
						<button type="button" onclick="loadDatas()">Load</button></br>
					</form>
				</fieldset>
			</td></tr>
			<tr><td>
				<fieldset>
					<div id="form2"></div>
				</fieldset>
			</td></tr>
		</table>
		<table class="on-right">
			<tr><td>
				<fieldset>
					<div id="results"><input id='y' name="y" type="hidden"></div>
				</fieldset>
			</td></tr>
		</table>

		<script type="text/javascript">
			function afficher(choix){
				if(choix == "choose"){
					document.getElementById("champ1").innerHTML = "<select id='myFile' name='myFile' required='required'><option disabled selected value> -- select an option -- </option>"+
					"<option value='iris.csv' onclick=\"setValue('iris.csv');\">iris.csv</option>"+
					"<option value='statsFSEVary.csv' onclick=\"setValue('statsFSEVary.csv');\">statsFSEVary.csv</option>"+
					"<option value='mushrooms.csv' onclick=\"setValue('mushrooms.csv');\">mushrooms.csv</option></select>";
				}
				if(choix == "import"){
					document.getElementById("champ1").innerHTML ="<input type='file' id='myFile' name='myFile' required='required' accept='.csv'></input>"+
					"</br>Precise your path file : </br>"+
					"<input type='text' name='path' id='path'></input>";
				}
			};

			function loadDatas(){
				var monchoix = $('input[name=choix]:checked', '#formulaire1').val();
				var monfichier = $('#myFile').val();
				if (monchoix == "import"){
					var monchemin = $('#path').val();
					$.get('InfoAjax', {
						choix : monchoix,
						myFile : monfichier,
						path : monchemin
					}, function(responseText) {
						$('#form2').html(responseText);
					});
				}else{
					$.get('InfoAjax', {
						choix : monchoix,
						myFile : monfichier,
					}, function(responseText) {
						$('#form2').html(responseText);
					});
				}
			}
		</script>
	</body>
	<footer>
		<br>
	</footer>
</html>
