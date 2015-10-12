$(function() {
    $('#editDialog').dialog({
        buttons:[{text:'保存',handler:function(){
            if(!$('#editForm').form('validate')){return;}
            $('#editForm')._ajaxForm(function(r){
                if(r.r){$('#editDialog').dialog('close');$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }},{text:'关闭',handler:function(){$('#editDialog').dialog('close');}}]
    });
    var grid = $('#grid')._datagrid({
        checkOnSelect:false,
        selectOnCheck:false,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        toolbar : [{
                    text : '添加字典数据',
                    iconCls : 'icon-add',
                    handler : handler_add
                }, '-', {
                    text : '删除所选',
                    iconCls : 'icon-remove',
                    handler : batch_del
                }, '-' ]
    });
    
    $('#queryButton').click(function(){
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });

    /*新增用户*/
    function handler_add() {
        $('#editForm').attr('action','config/add').resetForm();
        $('#edit_id').val('');
        $("#id").val('');
        $('#editDialog').dialog('open').dialog("setTitle","添加新字典数据");
    }
    /*批量删除*/
    function batch_del() {
        var check = $('#grid').datagrid('getChecked');
        if(check.length > 0){
            $.messager.confirm('操作提示', '确定要删除所选字典数据？', function(r){
                if (r){
                    var configs = new Array();
                    for(var i in check){
                    	configs[i] = check[i].id;
                    }
                    $._ajaxPost('config/batchDel',{ids:configs.join('|')},function(r){
                        if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
                    });
                }
            });
        }
    }
});
var formatter = {
    state : function(value, rowData, rowIndex) {
        if(value == 1){ return '<font color=green>正常</font>'; } else { return '<font color=red>停用</font>'; }
    },
    posts : function(value, rowData, rowIndex) {
        var arr = [];
        for(i in value) {
            arr.push($.fn.display.post[value[i]]);
        }
        return arr.join(',');
    },
    opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="updConfig('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delConfig('+rowIndex+');" href="javascript:void(0);">删除</a>';
        return html;
    }
};

/*修改字典表*/
function updConfig(rowIndex) {
    $('#editForm').attr('action','config/update').resetForm();
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#editForm')._jsonToForm(data);
//    $('#roleID')._pullDownTree({checkeds:data.roles, treeName: 'roleName', treeId:'roleID'});
    $('#editDialog').dialog('open').dialog('setTitle','修改字典表');
}
/*删除字典*/
function delConfig(rowIndex) {
    $.messager.confirm('操作提示', '确定要删除该字典表？', function(r){
        if (r){
            var data = $('#grid').datagrid('getRows')[rowIndex];
            $._ajaxPost('config/batchDel',{ids:data.id}, function(r){
                if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }
    });
}
