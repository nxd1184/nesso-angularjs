jQuery(document).ready(function() {  
	var menu = $('.navbar');
	$(window).bind('scroll', function(e){
		if($(window).scrollTop() > 140){
			if(!menu.hasClass('scrollOpen')){
				menu.addClass('scrollOpen');
			}
		}else{
			if(menu.hasClass('scrollOpen')){
				menu.removeClass('scrollOpen');
			}
		}
	});
	$("body").on("click", ".option", function(event){
		if($(".option").not(this).hasClass('open')){
			$(".option").removeClass('open');
		}
		$(this).toggleClass('open');
		event.stopPropagation();
	});
	
	$("body").on("click", ".wrap_add_team a.btn-link", function(event){
		if($(".add_team").not(this).hasClass('open')){
			$(".add_team").removeClass('open');
		}
		$(this).toggleClass('open');
		event.stopPropagation();
	});
	$(document).click(function(event){
		$('.option').removeClass('open');
		event.stopPropagation();
	});

	$('.responsive-toggler').click(function(){
		$(this).toggleClass('clicked');
		if($(this).hasClass('clicked')){
			$('body').removeClass('page-sidebar-closed');
			$('.page-sidebar-menu').removeClass('page-sidebar-menu-closed');
			// $('#page-sidebar-menu').css({'width':'250px !important'});
			document.getElementById("page-sidebar-menu").style.width = "235px";
		}
		else{
			$('.page-sidebar-menu').css({'width':'0px'});
			// document.getElementById("page-sidebar-menu").style.width = "0px";
		}
	});
	$( window ).resize(function() {
		if($(window).width() > 1199){
			$('.page-sidebar-menu').css({'width':'initial'});
			$('.menu-toggler.responsive-toggler').removeClass('clicked');
		}
		else{
			$('.page-sidebar-menu').css({'width':'0px'});
		}
	});
})
