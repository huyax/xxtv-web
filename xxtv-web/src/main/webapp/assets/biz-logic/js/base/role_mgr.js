$(function() {
	$('#roleEditDialog').dialog({
		buttons:[{text:'保存',handler:function(){
			if(!$('#roleEditForm').form('validate')){return;}
			$('#roleEditForm').attr('action','role/add')._ajaxForm(function(r){
				var node = $('#roleEditForm')._formToJson();
				if(r.r){
					var parentNode = roleTree.getNodeByParam('id', node.parentRole, null);
					node.id = r.id;
					roleTree.addNodes(parentNode, node);
					roleTree.selectNode(roleTree.getNodeByParam('id', node.id));
					$('#roleEditDialog').dialog('close');
				}
				asyncbox.tips(r.m,r.r ? 'success' : 'error');
			});
		}},{text:'关闭',handler:function(){$('#roleEditDialog').dialog('close');$('#roleEditForm').resetForm();}}]
	});

	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if ($("#addBtn_"+treeNode.tId).length > 0) return;
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='新增' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_"+treeNode.tId);
		if (btn) btn.bind("click", function(){
			$('#roleEditForm').resetForm();
			$('#parentRole').val(treeNode.id);
			$('#parentRoleName').val(treeNode.roleName);
			$('#roleEditDialog').dialog('setTitle', '新增角色').dialog('open');
			return false;
		});
	}
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
	}

	var roleSetting = {
			view : {
				showTitle : false,
				selectedMulti : false,
				autoCancelSelected : false,
				addHoverDom : addHoverDom,
				removeHoverDom : removeHoverDom,
				dblClickExpand : false,
				fontCss : function(treeId, treeNode){
					if(treeNode.status == 0){
						return {color:"red"};
					} else {
						return {color:"black"};
					}
				}
			},
			edit: {
				enable: true,
				renameTitle: '编辑',
				removeTitle: '删除',
				showRenameBtn: function(treeId, treeNode){return true;},
				showRemoveBtn: function(treeId, treeNode){if (treeNode.isParent) {return false;} return true;},
				drag:{isCopy : false,isMove : false}
			},
			data : {
				simpleData : { enable : true, idKey : "id", pIdKey : "parentRole" },
				key : { name : "roleName"}
			},
			callback : {
				onDblClick : function(event, treeId, treeNode){
					$._ajaxPost('role/roleMenu',{id: treeNode.id},function(r){
						if(r.r) {
							$('#titleRoleName').text(' - ' + treeNode.roleName);
							$('#titleRoleName').data('id', treeNode.id).data('trigger',false);
							var nodes = menuTree.transformToArray(menuTree.getNodes());
							for(var i in nodes) {
								menuTree.checkNode(nodes[i], false, false, false);
							}
							for(var i in r.d){
								var n = menuTree.getNodeByParam("id", r.d[i].menuID, null);
								if(n == null ) continue ;
								menuTree.checkNode(n, true, false, false);
							}
						} else {
							asyncbox.tips(r.m, 'error');
						}
					});
				},
				beforeRemove:function(treeId, treeNode) {
					roleTree.selectNode(treeNode);
					$.messager.confirm('提醒','确定要删除该角色吗？',function(r) {
						if(r){
							$._ajaxPost('role/del',{id:treeNode.id},function(r){
								if(r.r){roleTree.removeNode(treeNode);}
								asyncbox.tips(r.m,r.r ? 'success' : 'error');
							});
						}
					});
					return false;
				},
				beforeEditName : function(treeId, treeNode) {
					roleTree.selectNode(treeNode);
					treeNode.oldName = treeNode.roleName;
					return true;
				},
				onRename : function(event, treeId, treeNode, isCancel) {
					if(treeNode.oldName != treeNode.roleName) {
						$._ajaxPost('role/update',treeNode,function(r){
							if(r.r){}
							asyncbox.tips(r.m,r.r ? 'success' : 'error');
						});
					}
				}
			}
	};
	$.fn.zTree.init($("#rolePanel"), roleSetting, window.roles);
	var roleTree = $.fn.zTree.getZTreeObj("rolePanel");
	roleTree.expandAll(true);

	var setting = {
			check : {
				enable : true,
				chkStyle : "checkbox",
				chkboxType : { "Y" : "ps", "N" : "ps" },
				autoCheckTrigger : true
			},
			data : {
				simpleData : { enable : true, idKey : "id", pIdKey : "parentID" },
				key : {name : "showName", url : "mUrl" }
			},
			callback : {
				onClick : function(event, treeId, treeNode) {
					if (treeNode.isParent) {
						menuTree.expandNode(treeNode, !treeNode.open, false, true);
					}
				},
				onCheck:function(event, treeId, treeNode){
					$._ajaxPost('role/menuChecked',{roleID:$('#titleRoleName').data('id'),menuID:treeNode.id,checked:treeNode.checked},function(r){console.log(r.m);});
				},
				beforeCheck:function(treeId, treeNode){
					if($('#titleRoleName').data('id')){
						return true;
					}else {
						asyncbox.tips('请双击需要分配菜单的角色！', 'error');
						return false;
					}
				}
			},
			view : { showTitle : false, selectedMulti : false, autoCancelSelected : false }
	};
	$.fn.zTree.init($("#menuPanel"), setting, window.menus);
	var menuTree = $.fn.zTree.getZTreeObj("menuPanel");
	menuTree.expandAll(true);
});
function custom(width, height) {
	$('#menuMgrPanel,#roleMgrPanel').height(height - 29);
	$('#rightPanel').css({'margin-left':$('#roleMgrPanel').width() + 7});
}