/*

(function($){
    $(function(){
    	$('.menu-child').click(function(){
            $('.menu-child').removeClass('selected');
            $(this).addClass('selected');
            var location = '<a href="'+window.rootPath+'/home" target="contentBody" onclick="home()">首页</a>';
            location += '<i></i>'+ $('.menu-parent[menuid=' + $(this).attr('parentid') + ']').text();
            location+= '<i></i>' + $(this).text();
            $('#location').html(location);
            $('#contentBody').attr('src', $(this).attr('href'));
        });
    });
})(jQuery);*/