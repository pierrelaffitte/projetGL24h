$(document).ready(function() {
        $('#form1').onclick(function(event) {
                var monchoix = $('#monselect').val();
		var monfichier = $('#myFile').val();
		if (monchoix == "choose"){
			var monchemin = $('#path').val();
			$.get('Info2Pierre', {
		        choix : monchoix,
			myFile : monfichier,
			path : monchemin
                }, function(responseText) {
                        $('#form2').text(responseText);
                	});

		}else{
			$.get('Info2Pierre', {
		        choix : monchoix,
			myFile : monfichier,
                }, function(responseText) {
                        $('#form2').text(responseText);
                	});	
		}
                
        });
        $('#userName').blur(function(event) {
                var name = $('#userName').val();
                $.get('GetUserServlet', {
                        userName : name
                }, function(responseText) {
                        $('#form2').text(responseText);
                });
        });
});
