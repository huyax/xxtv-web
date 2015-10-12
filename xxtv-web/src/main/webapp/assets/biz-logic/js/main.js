(function($){
    $(function(){
        showDateTime();
        $('.menu-parent').click(function(){
            $('.menu-parent').removeClass('selected');
            if($(this).next().css('display') == 'none')
                $(this).addClass('selected');
            $('.menu-childs-panel[menuid!="' + $(this).attr('menuid') + '"]').animate({height: 'hide'}, "fast");
            $(this).next().animate({height: 'toggle', opacity: 'show'}, "normal");
        });
        $('.menu-child').click(function(){
            $('.menu-child').removeClass('selected');
            $(this).addClass('selected');
            var location = '<a href="'+window.rootPath+'/home" target="contentBody" onclick="home()">首页</a>';
            location += '<i></i>'+ $('.menu-parent[menuid=' + $(this).attr('parentid') + ']').text();
            location+= '<i></i>' + $(this).text();
            $('#location').html(location);
            $('#contentBody').attr('src', $(this).attr('href'));
        });
        $('#usermsg').click(function(){
            $('#location').html('用户信息');
            $('#contentBody').attr('src', window.rootPath+'/user/userMessage');
            $('.menu-child').removeClass('selected');
        });
        $('#logout').click(function(){
            asyncbox.confirm('确定要退出系统吗？','系统提示',function(action){ 
                if(action == 'ok'){
                    document.location = window.rootPath + "/login/logout";
                }
            });
        });
    });
})(jQuery);
function centerResize(width, height) {
    $('#contentBody').css({height:height - 40, width: width});
    var initSizeMethod = document.getElementById("contentBody").contentWindow.initSize;
    if ($.type(initSizeMethod) === 'function') {
        initSizeMethod();
    }
}
function getContentPanelSize() {
    var height = $('#contentBody').height() - 10;
    var width = $('#contentBody').width() - 10;
    return {
        height : height,
        width : width
    };
}
function showDateTime() {
    dateValue += 1000;
    $('#show_date').text($.formatDate(new Date(dateValue), 'YYYY年MM月DD日 hh时mm分ss秒  星期'));
    setTimeout('showDateTime()', 1000);
}
function home(){
    $('#location').html("首页");
}