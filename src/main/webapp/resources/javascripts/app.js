$(function() {
	$('.js-toggle').bind('click', function(event) {
		$('.js-sidebar, .js-content').toggleClass('is-toggled');
		event.preventDefault();
	});	
});

$(function() {
	$('.js-toggle-menu-user').bind('click', function(event) {
		$('.lb-user-menu').toggleClass('lb-user-menu-visible');
		event.preventDefault();
		event.stopPropagation();
	});

    $(document).click(function (e) {
        if (!$(e.target).closest('.hover').length) {
            $('.lb-user-menu').removeClass("lb-user-menu-visible");
        }
    });
});