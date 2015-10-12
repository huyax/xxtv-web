var excel = function(){
	var obj = {};
	obj.export = function(action,grid,queryObj){
		var datas = grid.datagrid("options").columns[0];
//		var params = grid.datagrid("options").queryParams;
//		console.info(params);
//		console.info(queryObj);
		var form = $("<form>");
        form.attr('style', 'display:none');
        form.attr('target', '');
        form.attr('method', 'post');
        form.attr('action', window.rootPath+action);
        $('body').append(form);
        var input1 = $('<input>');
        input1.attr('type', 'hidden');
        input1.attr('name', 'title');
        input1.attr('value', grid.datagrid("options").title);
        var input2 = $('<input>');
        input2.attr('type', 'hidden');
        input2.attr('name', 'columns');
        input2.attr('value', JSON.stringify(datas));
        for(var o in queryObj){
        	var inputTemp = $('<input>');
        	inputTemp.attr('type', 'hidden');
        	inputTemp.attr('name', o);
        	inputTemp.attr('value', queryObj[o]);
            form.append(inputTemp);
        }
        form.append(input1);
        form.append(input2);
        form.submit();
        form.remove();
	}
	obj.import = function(browseBtnID,action,grid){
		var dd = JSON.stringify(grid.datagrid("options").columns[0]);
		dd = encodeURIComponent(encodeURIComponent(dd));
		var xx  = new plupload.Uploader({ //实例化一个plupload上传对象
			browse_button : browseBtnID,
			url : window.rootPath+action+'?columns='+dd,
			flash_swf_url : '${base}/assets/common/flupload/Moxie.swf',
			silverlight_xap_url : '${base}/assets/common/flupload/Moxie.xap'
		});
		
		xx.init();
		
		//绑定文件全部上传完成后
		xx.bind('FileUploaded',function(xx,file,response){
			var obj = $.parseJSON(response.response);
			$.messager.alert("上传完成",obj.m);
			grid.datagrid('load', grid.datagrid("options").queryParams);
		});
		
		xx.bind('error',function(xx,errObject){
			$.messager.alert("上传错误","错误代码:"+errObject.code+",错误信息:"+errObject.message);
		});
		
		xx.bind('FilesAdded',function(xx,files){
			xx.start();
		});
		
	}
	
	obj.bound = function(grid){
//		console.info(11);
//		var fields = grid.datagrid("getColumnFields");
//		for(var f in fields){
//			$("#field_"+f).click(function(){
//				alert(f);
//			});
//		}
		var fields = grid.datagrid("getColumnFields");
		$(".datagrid-cell").each(function(i){
			$(this).toggle(
					function(){
						grid.datagrid("getColumnOption",fields[i]).export="0";
//						$(this).css({color: "#ff0011", background: "blue" });
						$(this).css({background:"#FFF68F", textDecoration:"line-through"});
					},
					function(){
						delete grid.datagrid("getColumnOption",fields[i]).export;
						$(this).css({background:"#F5F5F5",textDecoration:"none"});
					}
			);
		});
	}
	
	//设置不导出的列
	obj.doNotSelect = function(/*grid,...*/){
		var grid = arguments[0];
		var fields = grid.datagrid("getColumnFields");
		for(var i = 1; i < arguments.length; i++) {
			grid.datagrid("getColumnOption",fields[arguments[i]]).export="0";
		}
	}
	//恢复设置了不导出的列
	obj.resetSelect = function(/*grid,...*/){
		var grid = arguments[0];
		var fields = grid.datagrid("getColumnFields");
		for(var i = 1; i < arguments.length; i++) {
			delete grid.datagrid("getColumnOption",fields[arguments[i]]).export;
		}
	}
	
	obj.rate = function(value,rowData,rowIndex){
		if(value!=null){
			return "<font>"+(value*100).toFixed(2)+"%</font>";
		}
	}
	
	return obj;
}