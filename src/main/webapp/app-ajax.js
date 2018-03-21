$(document).ready(function() {
        $('#userName').blur(function(event) {
                var name = $('#userName').val();
                $.get('GetUserServlet', {
                        userName : name
                }, function(responseText) {
                        $('#form2').text(responseText);
                });
        });
});
