display("role"); // 需要翻译的类型
$(function() {
    $('#userEditDialog').dialog({
        buttons:[{text:'保存',handler:function(){
            if(!$('#userEditForm').form('validate')){return;}
            $('#userEditForm')._ajaxForm(function(r){
                if(r.r){$('#userEditDialog').dialog('close');$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }},{text:'关闭',handler:function(){$('#userEditDialog').dialog('close');}}]
    });
    $('#userRoleDialog').dialog({buttons:[{text:'关闭',handler:function(){$('#userRoleDialog').dialog({closed:true});}}]});
    var grid = $('#grid')._datagrid({
        checkOnSelect:false,
        selectOnCheck:false,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        toolbar : [{
                    text : '创建用户',
                    iconCls : 'icon-add',
                    handler : handler_add
                }, '-', {
                    text : '删除所选',
                    iconCls : 'icon-remove',
                    handler : batch_del
                }, '-' ]
    });
    
    var setting = {
		check : {
			enable : true, chkStyle : "checkbox", chkboxType : { "Y" : "s", "N" : "s" }
		},
		data : {
			simpleData : { enable : true, idKey : "id" ,pIdKey : "parentRole"},
			key : { name : "roleName" }
		},
		callback : {
			onCheck : function(event, treeId, treeNode){
				console.info(treeNode);
				$._ajaxPost('user/roleChecked',{userID:$('#roleZtee').data('userID'),roleID:treeNode.id,checked:treeNode.checked},function(r){
					if(r.r){
						var nodes = zTree.getCheckedNodes(true);
						var rowIndex = $('#roleZtee').data('rowIndex');
						var data = $('#grid').datagrid('getRows')[rowIndex];
						data.roles = [];
						for(var i in nodes){
							data.roles.push(nodes[i].id);
						}
						data.roles = data.roles.join(",");
						$('#grid').datagrid('refreshRow', rowIndex);
					}
				});
			}
		},
		view : { showTitle : false, selectedMulti : false, autoCancelSelected : false }
	};
	$.fn.zTree.init($("#roleZtee"), setting, window.roles);
	var zTree = $.fn.zTree.getZTreeObj("roleZtee");
	
    $('#queryButton').click(function(){
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });

//    $('#roleID')._pullDownTree({url:'role/list', width:150, height:200, treeName: 'roleName', treeId:'roleID', treePId : 'parentRole',chkboxType:{ "Y" : "", "N" : ""},onCheck:function(zTree,treeNode,targ, hide){
//        zTree.checkAllNodes(false);
//        zTree.checkNode(treeNode, true, true);
//        var nodes = zTree.getCheckedNodes(true);
//        for(var i in nodes){
//            hide.val(nodes[i].roleID);
//            targ.val(nodes[i].roleName);
//        }
//    },successCallback : function(zTree) {
////        node = zTree.getNodeByParam("roleID", window.user.roleID, null);
////        node.nocheck = true;
////        zTree.updateNode(node);
//    }});
    /*新增用户*/
    function handler_add() {
        $('#userEditForm').attr('action','user/add').resetForm();
        $('#username').removeAttr('readonly');
        $('#id').val('');
//        $('#roleID')._pullDownTree('clear');
        $('#userEditDialog').dialog('open').dialog("setTitle","新增用户");
    }
    /*批量删除*/
    function batch_del() {
        var check = $('#grid').datagrid('getChecked');
        if(check.length > 0){
            $.messager.confirm('操作提示', '确定要删除所选用户？', function(r){
                if (r){
                    var userIds = new Array();
                    for(var i in check){
                        userIds[i] = check[i].id;
                    }
                    $._ajaxPost('user/batch_del',{userIds:userIds.join('|')},function(r){
                        if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
                    });
                }
            });
        }
    }
});
var formatter = {
    status : function(value, rowData, rowIndex) {
        if(value == 1){ return '<font color=green>正常</font>'; } else { return '<font color=red>停用</font>'; }
    },
    roles : function(data, rowData, rowIndex) {
    	var value = data && data.split(",")
        var arr = [];
        for(i in value) {
            arr.push($.fn.display.role[value[i]]);
        }
        return arr.join(',');
    },
    posts : function(value, rowData, rowIndex) {
        var arr = [];
        for(i in value) {
            arr.push($.fn.display.post[value[i]]);
        }
        return arr.join(',');
    },
    opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="updUser('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-blue" onclick="updRole('+rowIndex+');" href="javascript:void(0);">设置角色</a>';
            html+= '<a class="spacing a-red" onclick="delUser('+rowIndex+');" href="javascript:void(0);">删除</a>';
            html+= '<a class="spacing a-green" onclick="resetPassword('+rowIndex+');" href="javascript:void(0);">密码重置</a>';
        return html;
    }
};
/*分配角色*/
function updRole(rowIndex) {
	var data = $('#grid').datagrid('getRows')[rowIndex];
	$('#roleZtee').data('userID', data.id).data('rowIndex', rowIndex);
	var zTree = $.fn.zTree.getZTreeObj("roleZtee");
	zTree.checkAllNodes(false);
	var temp = data.roles && data.roles.split(",");
	for(var i in temp){
		zTree.checkNode(zTree.getNodeByParam('id', temp[i], null), true, false, false);
	}
	$('#userRoleDialog').dialog({closed:false});
}
/*修改用户*/
function updUser(rowIndex) {
    $('#userEditForm').attr('action','user/update').resetForm();
    $('#username').attr('readonly', 'readonly');
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#userEditForm')._jsonToForm(data);
//    $('#roleID')._pullDownTree({checkeds:data.roles, treeName: 'roleName', treeId:'roleID'});
    $('#userEditDialog').dialog('open').dialog('setTitle','修改用户');
}
/*删除用户*/
function delUser(rowIndex) {
    $.messager.confirm('操作提示', '确定要删除该用户？', function(r){
        if (r){
            var data = $('#grid').datagrid('getRows')[rowIndex];
            $._ajaxPost('user/del',{id:data.id}, function(r){
                if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }
    });
}
function resetPassword(rowIndex){
    $.messager.confirm('操作提示', '确定重置该用户密码？', function(r){
        if (r){
            var data = $('#grid').datagrid('getRows')[rowIndex];
            $._ajaxPost('user/resetPassword',{id:data.id}, function(r){
                if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }
    });
}