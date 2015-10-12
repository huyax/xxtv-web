(function($){
    $(function(){
        var r_m,_ajax = false,login_success = false;
        $('#username').focus();
        $.ajaxSetup({beforeSend:function(request){
            if(_ajax){ request.abort(); }else{ _ajax = true; $('#loginForm input').attr('disabled','disabled'); }
        },complete:function(request,status){
            if(login_success){return;}
            _ajax = false; $('#loginForm input').removeAttr('disabled','disabled'); $('#password').val('');
            if(r_m == 0){ $('#password').focus(); }else if(r_m == 1){ $('#username').focus().val(''); }else{ $('#username').focus().val(''); }
        },error:function(){ alert('发起登录请求失败！'); }});
        $('#loginForm').submit(function(){
            if(!validation()){return false;}
            var data = $('#loginForm').serialize();
            $.post('login/loginCheck', data, function(r) {
                if (r.e) { alert(r.e); if(r.m == 0){r_m = 0;}else if(r.m == 1){r_m = 1;}else{r_m = -1;}
                } else if (r.r) { top.document.location = 'main'; login_success = true;
                } else { alert('登录失败！'); }
            }, 'json');
            return false;
        });
    });
    /*验证登录信息*/
    function validation(){
        if($.trim($('#username').val()) == ''){alert('用户名不能为空！');$('#username').focus();return false;}
        if($.trim($('#password').val()) == ''){alert('密码不能为空！');$('#password').focus();return false;}
        return true;
    }
})(jQuery);