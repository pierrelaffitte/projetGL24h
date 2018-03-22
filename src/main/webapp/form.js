$(document).ready(function() {
	$('#y').blur(function(event) {
                var monfichier = $('#monFichierNonModifiable').val();
		var mony = $('#y').val();
		$.get('Run2', {
		        myFile : monfichier,
			y : mony,
                }, function(responseText) {
                        $('#results').text(responseText);
                	});	
		}
                
        });
});
