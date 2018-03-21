$(document).ready(function() {
        $('#myFile').blur(function(event) {
                var monchoix = $('#monselect').val();
		var monfichier = $('#myFile').val();
		if (monchoix == "import"){
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
                        $('#form2').html(responseText);
                	});	
		}
                
        });
});
/*	*/
